package org.benchmarx.compare;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.benchmarx.Comparator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.xtext.xbase.lib.Pair;
import static org.junit.Assert.*;

public abstract class EMFOrderedStringComparator<T> implements Comparator<T> {

	private final static String className = "EMFOrderedStringGenerator";

	// registered eClasses
	private List<EClass> eClasses;

	// registered refs
	private List<EReference> eRefs;

	// registered EAttributes
	private List<EAttribute> eAttributes;

	// register EDataType to string converter
	private Map<EAttribute, Function<Object, String>> eClassToStringMap;

	private EMFOrderedStringTemplates template;

	public EMFOrderedStringComparator() {
		template = new EMFOrderedStringTemplates();
		
		eClasses = new ArrayList<>();
		eRefs = new ArrayList<>();
		eAttributes = new ArrayList<>();
		eClassToStringMap = new HashMap<>();

		registerEClasses();
		registerEReferences();
		registerAttributes();
		registerEDataTypeToStringConverter();
	}

	public String generateString(EObject in) {
		EClass objClass = in.eClass();

		// not registered
		if (!eClasses.contains(objClass)) {
			Logger.getLogger(className).log(Level.WARNING,
					objClass.getName() + " was not registered for String comparisson");
			return null;
		}

		// collect referenced elements
		List<String> mappingString = eRefs.stream().map(ref -> extractReference(in, ref)).filter(a -> a != null)
				.collect(Collectors.toList());

		// sort everything
		Collections.sort(mappingString);

		List<String> attributeMappingConcatString = eAttributes.stream().map(attr -> extractAttribute(in, attr))
				.filter(a -> a != null).collect(Collectors.toList());
		Collections.sort(attributeMappingConcatString);

		return template.objToString(objClass.getName(), mappingString, attributeMappingConcatString);
	}

	private String extractAttribute(EObject in, EAttribute attr) {
		if (!in.eClass().getEAllAttributes().contains(attr))
			return null;

		Object obj = in.eGet(attr);
		if (obj == null || obj instanceof EObjectContainmentEList)
			return template.attributeToString(attr.getName(), "null");

		return generateStringFromAttribute(attr, obj);
	}

	private String extractReference(EObject in, EReference ref) {
		if (ref.isMany())
			return extractManyReference(in, ref);
		return extractSimpleReference(in, ref);
	}

	private String extractSimpleReference(EObject in, EReference ref) {
		if (!in.eClass().getEReferences().contains(ref))
			return null;

		Object obj = in.eGet(ref);
		if (obj == null || obj instanceof EObjectContainmentEList)
			return template.singleMappingToString(ref.getName(), "null");

		if (obj instanceof EObject)
			return template.singleMappingToString(ref.getName(), generateString((EObject) obj));

		Logger.getLogger(className).log(Level.SEVERE,
				"The referenced element " + obj + " from eRef " + ref.getName() + " is not an EObject");
		return null;
	}

	private String extractManyReference(EObject in, EReference ref) {
		if (!in.eClass().getEReferences().contains(ref))
			return null;

		Object obj = in.eGet(ref);
		if (obj == null)
			return template.manyMappingToString(ref.getName(), new ArrayList<>());

		if (obj instanceof EList) {
			EList<EObject> list = (EList<EObject>) obj;
			List<String> strings = list.stream().map(eObj -> generateString(eObj)).collect(Collectors.toList());
			Collections.sort(strings);
			return template.manyMappingToString(ref.getName(), strings);
		}
		
		Logger.getLogger(className).log(Level.SEVERE,
				"The referenced element " + obj + " from eRef " + ref.getName() + " is not an EList");
		return null;
	}

	public String generateStringFromAttribute(EAttribute attr, Object obj) {
		Function<Object, String> func = eClassToStringMap.get(attr);
		if (func == null)
			return template.attributeToString(attr.getName(), obj.toString());

		return template.attributeToString(attr.getName(), func.apply(obj));

	}

	public abstract void registerEClasses();

	public abstract void registerEReferences();

	public abstract void registerAttributes();

	public abstract void registerEDataTypeToStringConverter();

	public void registerEClass(EClass eClass) {
		eClasses.add(eClass);
	}

	public void registerEReference(EReference eRef) {
//		if(!eRef.isContainment())
//			Logger.getLogger(className).log(Level.WARNING, "Ignoring reference " + eRef.getEContainingClass().getName() + "::" + eRef.getName() + " because it is no containment and may lead to deadlocks");
		eRefs.add(eRef);
	}

	public void registerEAttribute(EAttribute attr) {
		eAttributes.add(attr);
	}

	public void registerEDataTypeToStringConverter(EAttribute attr, Function<Object, String> converter) {
		eClassToStringMap.put(attr, converter);
	}

	public void compare(T expected, T actual) {
		String expectedStr = generateString((EObject) expected);
		String actualStr = generateString((EObject) actual);
		assertEquals(expectedStr, actualStr);
	}
}
