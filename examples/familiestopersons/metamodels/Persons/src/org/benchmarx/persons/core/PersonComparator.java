package org.benchmarx.persons.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.benchmarx.compare.EMFOrderedStringComparator;

import Persons.PersonRegister;
import Persons.PersonsPackage;

public class PersonComparator extends EMFOrderedStringComparator<PersonRegister> {

	@Override
	public void registerEClasses() {
		registerEClass(PersonsPackage.Literals.PERSON_REGISTER);
		registerEClass(PersonsPackage.Literals.PERSON);
		registerEClass(PersonsPackage.Literals.MALE);
		registerEClass(PersonsPackage.Literals.FEMALE);
	}


	@Override
	public void registerAttributes() {
		registerEAttribute(PersonsPackage.Literals.PERSON__NAME);
		registerEAttribute(PersonsPackage.Literals.PERSON__BIRTHDAY);
	}

	@Override
	public void registerEDataTypeToStringConverter() {
		registerEDataTypeToStringConverter(PersonsPackage.Literals.PERSON__BIRTHDAY, o -> new SimpleDateFormat("yyyy-mm-dd").format((Date) o));
	}

	@Override
	public void registerEReferences() {
		registerEReference(PersonsPackage.Literals.PERSON_REGISTER__PERSONS);
	}
}
