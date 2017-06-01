package org.benchmarx.examples.familiestopersons.implementations.yage;

import static org.benchmarx.examples.familiestopersons.implementations.yage.HelperConstants.*;

import java.util.Optional;

import org.benchmarx.BXTool;
import org.benchmarx.examples.familiestopersons.testsuite.Decisions;
import org.benchmarx.persons.core.PersonHelper;
import org.fujaba.graphengine.graph.Node;

import Families.FamilyRegister;
import Persons.PersonRegister;

public class YagePersonHelper extends PersonHelper
{

   private YageFAmiliesToPersons tool;


   public YagePersonHelper(BXTool<FamilyRegister, PersonRegister, Decisions> tool)
   {
      this.tool = (YageFAmiliesToPersons) tool;
      // TODO Auto-generated constructor stub
   }


   private Node getNameNode(String name)
   {
      Optional<Node> nameopt = tool.getGraph().getNodes().stream()
         .filter(f -> f.getAttributes().containsKey(NAME) && f.getAttributes().get(NAME).equals(name))
         .findAny();
      if (nameopt.isPresent())
      {
         return nameopt.get();
      }
      else
      {
         Node node = new Node();
         node.setAttribute(NAME, name);
         node.setAttribute(Node.TYPE_ATTRIBUTE, STRING);
         tool.getGraph().addNode(node);
         return node;
      }

   }


   @Override
   public void createRod(PersonRegister register)
   {
      createPerson(ROD, FLANDERS, MALE);
   }


   private void createPerson(String surname, String name, String gender)
   {
      Node nameNode = getNameNode(name);
      Node surnameNode = getNameNode(surname);

      Node node = new Node();

      node.setAttribute(Node.TYPE_ATTRIBUTE, gender);
      node.setAttribute(BIRTHDAY, BIRTHDAY_EDEFAULT.toString());
      node.addEdge(NAME, nameNode);
      node.addEdge(SURNAME, surnameNode);
      tool.getPersonRegister().addEdge(PERSONS, node);
      tool.getGraph().addNode(node);

   }


   @Override
   public void createHomer(PersonRegister register)
   {
      createPerson(HOMER, SIMPSON, MALE);
   }


   @Override
   public void createBart(PersonRegister register)
   {
      createPerson(BART, SIMPSON, MALE);
   }


   @Override
   public void createMarge(PersonRegister register)
   {
      createPerson(MARGE, SIMPSON, FEMALE);
   }


   @Override
   public void createLisa(PersonRegister register)
   {
      createPerson(LISA, SIMPSON, FEMALE);
   }


   @Override
   public void createMaggie(PersonRegister register)
   {
      createPerson(MAGGIE, SIMPSON, FEMALE);
   }


   @Override
   public void createSeymour(PersonRegister register)
   {
      createPerson(SEYMOUR, SKINNER, MALE);
   }


   @Override
   public void changeAllBirthdays(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setBirthdayOfHomer(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setBirthdayOfMarge(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setBirthdayOfMaggie(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setBirthdayOfRod(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setBirthdayOfFatherBart(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setBirthdaysOfSimpson(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setBirthdayOfYoungerBart(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void firstNameChangeOfHomer(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void firstNameChangeOfBart(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void fullNameChangeOfOtherBart(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void fullNameChangeOfFatherBart(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void familyNameChangeOfLisa(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void fullNameChangeOfMarge(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void deleteMarge(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void deleteHomer(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void deleteMaggie(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void idleDelta(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void hippocraticDelta(PersonRegister register)
   {
      throw new UnsupportedOperationException();
   }

}
