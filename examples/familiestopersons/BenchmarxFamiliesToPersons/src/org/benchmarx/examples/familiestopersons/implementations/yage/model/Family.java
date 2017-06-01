package org.benchmarx.examples.familiestopersons.implementations.yage.model;

import org.eclipse.emf.common.util.EList;
import org.fujaba.graphengine.graph.Graph;

import Families.FamilyMember;
import Families.FamilyRegister;

public class Family extends YageEMFAdapter implements Families.Family
{

   public Family(Graph graph)
   {
      super(graph);
   }


   @Override
   public FamilyMember getFather()
   {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public void setFather(FamilyMember value)
   {
      // TODO Auto-generated method stub

   }


   @Override
   public FamilyMember getMother()
   {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public void setMother(FamilyMember value)
   {
      // TODO Auto-generated method stub

   }


   @Override
   public EList<FamilyMember> getSons()
   {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public EList<FamilyMember> getDaughters()
   {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public String getName()
   {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public void setName(String value)
   {
      // TODO Auto-generated method stub

   }


   @Override
   public FamilyRegister getFamiliesInverse()
   {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public void setFamiliesInverse(FamilyRegister value)
   {
      // TODO Auto-generated method stub

   }

}
