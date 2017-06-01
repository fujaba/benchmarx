package org.benchmarx.examples.familiestopersons.implementations.yage.model;

import org.eclipse.emf.common.util.EList;
import org.fujaba.graphengine.graph.Graph;

import Families.Family;

public class FamilyRegister extends YageEMFAdapter implements Families.FamilyRegister
{

   public FamilyRegister(Graph graph)
   {
      super(graph);
   }


   @Override
   public EList<Family> getFamilies()
   {
      return new ListAdapter<Family>(getGraph());
   }

}
