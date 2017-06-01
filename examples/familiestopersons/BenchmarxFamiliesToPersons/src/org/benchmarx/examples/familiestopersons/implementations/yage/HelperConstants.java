package org.benchmarx.examples.familiestopersons.implementations.yage;

import java.util.Date;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

public interface HelperConstants
{
   public static final String BOUVIER = "Bouvier";

   public static final String NAME = "name";

   public static final String SURNAME = "surname";

   public static final String FAMILYMEMBER = "FamilyMember";

   public static final String ROD = "Rod";

   public static final String MAUDE = "Maude";

   public static final String NED = "Ned";

   public static final String STRING = "String";

   public static final String TODD = "Todd";

   public static final String FLANDERS = "Flanders";

   public static final String MOTHER = "mother";

   public static final String MARGE = "Marge";

   public static final String FATHER = "father";

   public static final String HOMER = "Homer";

   public static final String SON = "sons";

   public static final String MAGGIE = "Maggie";

   public static final String BART = "Bart";

   public static final String DAUGHTER = "daughters";

   public static final String SIMPSON = "Simpson";

   public static final String LISA = "Lisa";

   public static final String SKINNER = "Skinner";

   public static final String PERSON_REGISTER = "PersonRegister";

   public static final String PERSONS = "persons";

   public static final String FAMILY_REGISTER = "FamilyRegister";

   public static final String FAMILY = "Family";

   public static final String INVERSE = "Inverse";

   public static final String MALE = "Male";

   public static final String FEMALE = "Female";

   public static final String FAMILIES = "families";

   public static final String SEYMOUR = "Seymour";

   public static final String BIRTHDAY = "birthday";

   public static final Date BIRTHDAY_EDEFAULT = (Date) EcoreFactory.eINSTANCE
      .createFromString(EcorePackage.eINSTANCE.getEDate(), "0000-1-1");

}
