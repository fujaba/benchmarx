package org.benchmarx.examples.familiestopersons.implementations.yage;

import static org.benchmarx.examples.familiestopersons.implementations.yage.HelperConstants.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.benchmarx.BXTool;
import org.benchmarx.examples.familiestopersons.testsuite.Decisions;
import org.benchmarx.families.core.FamilyHelper;
import org.fujaba.graphengine.graph.Node;

import Families.FamilyRegister;
import Persons.PersonRegister;

public class YageFamilyHelper extends FamilyHelper
{

   private YageFAmiliesToPersons tool;


   public YageFamilyHelper(BXTool<FamilyRegister, PersonRegister, Decisions> tool)
   {
      this.tool = (YageFAmiliesToPersons) tool;

   }


   @Override
   public void createSkinnerFamily(FamilyRegister register)
   {
      createFamily(SKINNER);
   }


   @Override
   public void createFlandersFamily(FamilyRegister register)
   {
      createFamily(FLANDERS);
   }


   @Override
   public void createFatherNed(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(NED);

      Node family = getFamily(FLANDERS);

      family.addEdge(FATHER, familyMember);

   }


   @Override
   public void createMotherMaude(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(MAUDE);

      Node family = getFamily(FLANDERS);

      family.addEdge(MOTHER, familyMember);
   }


   @Override
   public void createSonTodd(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(TODD);

      Node family = getFamily(FLANDERS);

      family.addEdge(SON, familyMember);
   }


   @Override
   public void createSonRod(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(ROD);

      Node family = getFamily(FLANDERS);

      family.addEdge(SON, familyMember);
   }


   private Node getFamily(String name)
   {
      Optional<Node> nameopt = tool.getGraph().getNodes().stream()
         .filter(f -> f.getAttributes().containsKey(NAME) && f.getAttributes().get(NAME).equals(name))
         .findAny();

      assertTrue("NamedNode not found: " + name, nameopt.isPresent());
      Node namenode = nameopt.get();

      List<Node> familyMemberopt = tool.getGraph().getNodes().stream()
         .filter(f -> f.getAttributes().get(Node.TYPE_ATTRIBUTE).equals(FAMILY) && f.getEdges().containsKey(NAME) && f.getEdges(NAME).contains(namenode))
         .collect(Collectors.toList());

      assertFalse("NamedNode not found: " + name, familyMemberopt.size() == 0);
      Node node = familyMemberopt.get(familyMemberopt.size() - 1);
      return node;
   }


   private Node getNamedNode(String name)
   {
      Optional<Node> nameopt = tool.getGraph().getNodes().stream()
         .filter(f -> f.getAttributes().containsKey(NAME) && f.getAttributes().get(NAME).equals(name))
         .findAny();

      assertTrue("NamedNode not found: " + name, nameopt.isPresent());
      Node namenode = nameopt.get();

      List<Node> familyMemberopt = tool.getGraph().getNodes().stream()
         .filter(f -> f.getEdges().containsKey(NAME) && f.getEdges(NAME).contains(namenode))
         .collect(Collectors.toList());

      assertTrue("NamedNode not found: " + name, familyMemberopt.size() == 1);
      Node node = familyMemberopt.get(0);
      return node;
   }


   @Override
   public void createSimpsonFamily(FamilyRegister register)
   {
      createFamily(SIMPSON);
   }


   private Node createFamily(String name)
   {
      Node node = new Node();
      node.setAttribute(Node.TYPE_ATTRIBUTE, FAMILY);
      Node nameNode = getNameNode(name);
      node.addEdge(NAME, nameNode);
      tool.getGraph().addNode(node);
      tool.getFamiliyRegister().addEdge(FAMILIES, node);
      return node;
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


   private Node createFamilyMember(String name)
   {
      Node node = new Node();
      node.setAttribute(Node.TYPE_ATTRIBUTE, FAMILYMEMBER);

      Node nameNode = new Node();
      nameNode.setAttribute(NAME, name);
      nameNode.setAttribute(Node.TYPE_ATTRIBUTE, STRING);
      node.addEdge(NAME, nameNode);

      tool.getGraph().addNode(nameNode);
      tool.getGraph().addNode(node);
      return node;
   }


   @Override
   public void createFatherHomer(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(HOMER);

      Node family = getFamily(SIMPSON);

      family.addEdge(FATHER, familyMember);

   }


   @Override
   public void createMotherMarge(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(MARGE);

      Node family = getFamily(SIMPSON);

      family.addEdge(MOTHER, familyMember);

   }


   @Override
   public void createSonBart(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(BART);

      Node family = getFamily(SIMPSON);

      family.addEdge(SON, familyMember);

   }


   @Override
   public void createDaughterLisa(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(LISA);

      Node family = getFamily(SIMPSON);

      family.addEdge(DAUGHTER, familyMember);

   }


   @Override
   public void createDaughterMaggie(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(MAGGIE);

      Node family = getFamily(SIMPSON);

      family.addEdge(DAUGHTER, familyMember);
   }


   @Override
   public void createFatherBart(FamilyRegister register)
   {
      Node familyMember = createFamilyMember(BART);

      Node family = getFamily(SIMPSON);

      family.addEdge(FATHER, familyMember);

   }


   @Override
   public void deleteFirstSonBart(FamilyRegister register)
   {
      Node familyMember = getNamedNode(BART);

      tool.getGraph().removeNode(familyMember);

   }


   @Override
   public void renameEmptySimpsonToBouvier(FamilyRegister register)
   {

      Node node = getNamedNode(SIMPSON);

      Node nameSimpsonNode = getNameNode(SIMPSON);

      node.removeEdgesTo(nameSimpsonNode);
      nameSimpsonNode.removeEdgesTo(node);

      Node nameBouvierNode = getNameNode(BOUVIER);
      node.addEdge(NAME, nameBouvierNode);
   }


   @Override
   public void renameSimpsonToBouvier(FamilyRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void moveLisa(FamilyRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void moveMaggieAndChangeRole(FamilyRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void moveMarge(FamilyRegister register)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void deleteFatherHomer(FamilyRegister register)
   {
      Node familyMember = getNamedNode(HOMER);

      Node family = getFamily(SIMPSON);

      family.removeEdgesTo(familyMember);

      System.out.println(familyMember);

   }


   @Override
   public void idleDelta(FamilyRegister register)
   {
   }


   @Override
   public void hippocraticDelta(FamilyRegister register)
   {
      throw new UnsupportedOperationException();
   }

}
