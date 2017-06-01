package org.benchmarx.examples.familiestopersons.implementations.yage.model;

import org.eclipse.emf.common.util.EList;
import org.fujaba.graphengine.graph.Graph;

import Persons.Person;

public class PersonRegister extends YageEMFAdapter implements Persons.PersonRegister
{

   public PersonRegister(Graph graph)
   {
      super(graph);
   }


   @Override
   public EList<Person> getPersons()
   {
      return new ListAdapter<Person>(getGraph());
   }

}
