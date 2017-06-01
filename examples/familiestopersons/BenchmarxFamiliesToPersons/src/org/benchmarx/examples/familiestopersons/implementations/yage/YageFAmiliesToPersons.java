package org.benchmarx.examples.familiestopersons.implementations.yage;

import static org.benchmarx.examples.familiestopersons.implementations.yage.HelperConstants.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.benchmarx.BXTool;
import org.benchmarx.Configurator;
import org.benchmarx.examples.familiestopersons.implementations.yage.model.YageEMFAdapter;
import org.benchmarx.examples.familiestopersons.testsuite.Decisions;
import org.benchmarx.families.core.FamilyHelper;
import org.fujaba.graphengine.GraphEngine;
import org.fujaba.graphengine.Match;
import org.fujaba.graphengine.PatternDumper;
import org.fujaba.graphengine.PatternEngine;
import org.fujaba.graphengine.graph.Graph;
import org.fujaba.graphengine.graph.Node;
import org.fujaba.graphengine.pattern.PatternGraph;
import org.fujaba.graphengine.pattern.PatternNode;

import Families.FamiliesFactory;
import Families.FamilyRegister;
import Persons.PersonRegister;

public class YageFAmiliesToPersons implements BXTool<FamilyRegister, PersonRegister, Decisions>
{

   private Graph graph = new Graph();

   private org.benchmarx.examples.familiestopersons.implementations.yage.model.FamilyRegister familyRegister = new org.benchmarx.examples.familiestopersons.implementations.yage.model.FamilyRegister(graph);

   private org.benchmarx.examples.familiestopersons.implementations.yage.model.PersonRegister personAdapterRegister = new org.benchmarx.examples.familiestopersons.implementations.yage.model.PersonRegister(graph);

   private Configurator<Decisions> configurator;

   private boolean preferExistingFamily = true;

   private boolean preferParentToKid = true;

   private Node familiyRegister;

   private Node personRegister;


   public Node getFamiliyRegister()
   {
      return familiyRegister;
   }


   public void setFamiliyRegister(Node familiyRegister)
   {
      this.familiyRegister = familiyRegister;
   }


   public Node getPersonRegister()
   {
      return personRegister;
   }


   public void setPersonRegister(Node personRegister)
   {
      this.personRegister = personRegister;
   }


   @Override
   public void initiateSynchronisationDialogue()
   {
      graph = new Graph();
      familiyRegister = new Node();
      familiyRegister.setAttribute(Node.TYPE_ATTRIBUTE, FAMILY_REGISTER);
      personRegister = new Node();
      personRegister.setAttribute(Node.TYPE_ATTRIBUTE, PERSON_REGISTER);

      graph.addNode(familiyRegister, personRegister);
      familyRegister = new org.benchmarx.examples.familiestopersons.implementations.yage.model.FamilyRegister(graph);

      preferExistingFamily = true;

      preferParentToKid = true;

   }


   @Override
   public void performAndPropagateSourceEdit(Consumer<FamilyRegister> edit)
   {
      edit.accept(familyRegister);
      applyRules();

   }


   private void applyRules()
   {

      if (configurator != null)
      {
         preferExistingFamily = configurator.decide(Decisions.PREFER_EXISTING_FAMILY_TO_NEW);
         preferParentToKid = configurator.decide(Decisions.PREFER_CREATING_PARENT_TO_CHILD);
      }
      matchPattern(getFamilytoPersonRule(FEMALE, DAUGHTER));
      matchPattern(getFamilytoPersonRule(FEMALE, MOTHER));
      matchPattern(getFamilytoPersonRule(MALE, SON));
      matchPattern(getFamilytoPersonRule(MALE, FATHER));

      matchPattern(getPersonToFamilyRule(MALE));
      matchPattern(getPersonToFamilyRule(FEMALE));

      matchPattern(getDeleteFatherRule());

   }


   private PatternGraph getPersonToFamilyRule(String gender)
   {

      String familyRole = preferParentToKid ? ((gender == MALE) ? FATHER : MOTHER) : ((gender == MALE) ? SON : DAUGHTER);

      PatternNode familyRegister = new PatternNode();
      PatternNode family = new PatternNode();
      PatternNode familyMember = new PatternNode();

      PatternNode name = new PatternNode();
      PatternNode surname = new PatternNode();

      PatternNode person = new PatternNode();
      PatternNode personRegister = new PatternNode();

      PatternNode newFamilyMember = new PatternNode();
      PatternNode newFamily = new PatternNode();

      PatternGraph patternGraph = new PatternGraph("PersonToFamilyRule" + familyRole + gender);
      patternGraph.addPatternNode(name, surname, person, personRegister, familyRegister, family, familyMember, newFamily, newFamilyMember);

      personRegister.addPatternEdge(PERSONS, person);

      familyRegister.addPatternEdge("!=", FAMILIES, family);
      family.setAction("!=");
      family.addPatternEdge("!=", NAME, name);
      family.setAttributeMatchExpression("#{" + Node.TYPE_ATTRIBUTE + "}=='" + FAMILY + "'");

      person.addPatternEdge(SURNAME, surname);
      person.addPatternEdge(NAME, name);
      person.setAttributeMatchExpression("#{" + Node.TYPE_ATTRIBUTE + "}=='" + gender + "'");

      familyMember.setAction("!=");
      familyMember.addPatternEdge("!=", NAME, surname);
      familyMember.setAttributeMatchExpression("#{" + Node.TYPE_ATTRIBUTE + "}=='" + FAMILYMEMBER + "'");

      family.addPatternEdge("!=", familyRole, familyMember);

      familyRegister.addPatternEdge("+", FAMILIES, newFamily);
      newFamily.setAction("+");
      newFamily.addPatternEdge("+", NAME, name);
      newFamily.setPatternAttribute("+", Node.TYPE_ATTRIBUTE, FAMILY);
      newFamily.addPatternEdge("+", familyRole, newFamilyMember);
      newFamilyMember.setAction("+");
      newFamilyMember.addPatternEdge("+", NAME, surname);
      newFamilyMember.setPatternAttribute("+", Node.TYPE_ATTRIBUTE, FAMILYMEMBER);

      return patternGraph;
   }


   private PatternGraph getDeleteFatherRule()
   {
      PatternNode familyRegister = new PatternNode();
      PatternNode family = new PatternNode();
      PatternNode familyMember = new PatternNode();

      PatternNode name = new PatternNode();
      PatternNode surname = new PatternNode();

      PatternNode person = new PatternNode();
      PatternNode personRegister = new PatternNode();

      PatternGraph patternGraph = new PatternGraph("DeleteFatherRule");

      patternGraph.addPatternNode(family, familyMember, name, surname, person, personRegister, familyRegister);

      personRegister.addPatternEdge("person", person);

      familyRegister.addPatternEdge(FAMILIES, family);
      family.addPatternEdge(NAME, name);
      familyMember.addPatternEdge(NAME, surname);

      family.addPatternEdge("!=", SON, familyMember);
      family.addPatternEdge("!=", FATHER, familyMember);
      family.addPatternEdge("!=", MOTHER, familyMember);
      family.addPatternEdge("!=", DAUGHTER, familyMember);

      person.addPatternEdge("surname", surname);
      person.addPatternEdge("name", name);

      person.setAction("-");
      PatternDumper dumper = new PatternDumper(patternGraph);
      dumper.dumpGraph("bla.html");
      return patternGraph;
   }


   private void matchPattern(PatternGraph familyToPersonRule)
   {
      PatternDumper dumper = new PatternDumper(familyToPersonRule);
      dumper.dumpGraph("dump/" + familyToPersonRule.getName() + ".html");
      graph = applyGTR(graph, familyToPersonRule, true);

   }


   Graph applyGTR(Graph g, PatternGraph gtr, boolean single)
   {
      boolean foundOne = false;
      do
      {
         foundOne = false;
         ArrayList<Match> matches = PatternEngine.matchPattern(g, gtr, true);
         if (matches.size() > 0)
         {
            foundOne = true;
            g = PatternEngine.applyMatch(matches.get(0));
            // System.out.println(gtr.getName() + ":\n" + g + "\n");
         }
      }
      while (!single && foundOne);
      return g;
   }


   private PatternGraph getFamilytoPersonRule(String personGenderType, String familyMemberedge)
   {
      PatternNode familyRegister = new PatternNode();
      PatternNode family = new PatternNode();
      PatternNode familyMember = new PatternNode();

      PatternNode name = new PatternNode();
      PatternNode surname = new PatternNode();

      PatternNode person = new PatternNode();
      PatternNode personRegister = new PatternNode();
      PatternNode newPerson = new PatternNode();

      PatternGraph patternGraph = new PatternGraph("FamilytoPersonRule-" + personGenderType + "-" + familyMemberedge);

      patternGraph.addPatternNode(family, familyMember, name, surname, person, newPerson, personRegister, familyRegister);

      person.setAction("!=");
      personRegister.addPatternEdge("!=", "person", person);

      familyRegister.addPatternEdge(FAMILIES, family);
      family.addPatternEdge(NAME, name);
      familyMember.addPatternEdge(NAME, surname);

      family.addPatternEdge(familyMemberedge, familyMember);

      person.addPatternEdge("surname", surname);
      person.addPatternEdge("name", name);

      newPerson.setAction("+");
      newPerson.setPatternAttribute("+", Node.TYPE_ATTRIBUTE, personGenderType);

      newPerson.setPatternAttribute("+", BIRTHDAY, BIRTHDAY_EDEFAULT.toString());

      newPerson.addPatternEdge("+", "surname", surname);
      newPerson.addPatternEdge("+", "name", name);
      personRegister.addPatternEdge("+", "persons", newPerson);
      return patternGraph;
   }


   @Override
   public void performAndPropagateTargetEdit(Consumer<PersonRegister> edit)
   {
      edit.accept(personAdapterRegister);
      applyRules();

   }


   @Override
   public void performIdleSourceEdit(Consumer<FamilyRegister> edit)
   {
      edit.accept(familyRegister);
      applyRules();

   }


   @Override
   public void performIdleTargetEdit(Consumer<PersonRegister> edit)
   {
      edit.accept(personAdapterRegister);
      applyRules();

   }


   @Override
   public void setConfigurator(Configurator<Decisions> configurator)
   {
      this.configurator = configurator;

   }


   @Override
   public void assertPostcondition(FamilyRegister source, PersonRegister target)
   {
      Graph src = new Graph();
      YageEMFAdapter.emfToYageGraph(src, source);
      //
      Graph tgt = new Graph();
      YageEMFAdapter.emfToYageGraph(tgt, target);

      boolean isomorphicSrc = GraphEngine.isIsomorphicSubGraph(src, graph);
      boolean isomorphicTgt = GraphEngine.isIsomorphicSubGraph(tgt, graph);

      assertTrue("source not match", isomorphicSrc);
      assertTrue("target not match", isomorphicTgt);

   }


   public static void main(String[] args)
   {
      FamilyRegister familyRegister = FamiliesFactory.eINSTANCE
         .createFamilyRegister();

      FamilyHelper helper = new FamilyHelper();
      helper.createSimpsonFamily(familyRegister);
      ;

      helper.createFatherHomer(familyRegister);

      helper.createSonBart(familyRegister);

      helper.createDaughterLisa(familyRegister);

      helper.createMotherMarge(familyRegister);

      helper.createFlandersFamily(familyRegister);

      helper.createMotherMaude(familyRegister);

      helper.createFatherNed(familyRegister);

      helper.createSonRod(familyRegister);

      helper.createSonTodd(familyRegister);

      Graph graph = new Graph();

      YageEMFAdapter.emfToYageGraph(graph, familyRegister);

   }


   @Override
   public void assertPrecondition(FamilyRegister source, PersonRegister target)
   {
      Graph src = new Graph();
      YageEMFAdapter.emfToYageGraph(src, source);
      //
      Graph tgt = new Graph();
      YageEMFAdapter.emfToYageGraph(tgt, target);

      boolean isomorphicSrc = GraphEngine.isIsomorphicSubGraph(src, graph);
      boolean isomorphicTgt = GraphEngine.isIsomorphicSubGraph(tgt, graph);

      assertTrue("source not match", isomorphicSrc);
      assertTrue("target not match", isomorphicTgt);

   }


   public Graph getGraph()
   {
      return graph;
   }

}
