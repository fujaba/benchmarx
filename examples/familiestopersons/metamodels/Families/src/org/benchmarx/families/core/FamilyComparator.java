package org.benchmarx.families.core;

import java.util.Date;

import org.benchmarx.compare.EMFOrderedStringComparator;

import Families.FamiliesPackage;
import Families.FamilyRegister;

public class FamilyComparator extends EMFOrderedStringComparator<FamilyRegister> {

	@Override
	public void registerEClasses() {
		registerEClass(FamiliesPackage.Literals.FAMILY_REGISTER);
		registerEClass(FamiliesPackage.Literals.FAMILY);
		registerEClass(FamiliesPackage.Literals.FATHER);
		registerEClass(FamiliesPackage.Literals.FAMILY_MEMBER);
		registerEClass(FamiliesPackage.Literals.MOTHER);
		registerEClass(FamiliesPackage.Literals.PARENT);
	}


	@Override
	public void registerAttributes() {
		registerEAttribute(FamiliesPackage.Literals.FAMILY__NAME);
		registerEAttribute(FamiliesPackage.Literals.FAMILY_MEMBER__NAME);
	}

	@Override
	public void registerEDataTypeToStringConverter() {
	}

	@Override
	public void registerEReferences() {
		registerEReference(FamiliesPackage.Literals.FAMILY_REGISTER__FAMILIES);
		registerEReference(FamiliesPackage.Literals.FAMILY__FATHER);
		registerEReference(FamiliesPackage.Literals.FAMILY__MOTHER);
		registerEReference(FamiliesPackage.Literals.FAMILY__SONS);
	}
}
