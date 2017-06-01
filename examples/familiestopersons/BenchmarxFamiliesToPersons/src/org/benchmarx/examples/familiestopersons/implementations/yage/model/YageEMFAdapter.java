package org.benchmarx.examples.familiestopersons.implementations.yage.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.benchmarx.examples.familiestopersons.implementations.yage.HelperConstants;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.fujaba.graphengine.graph.Graph;
import org.fujaba.graphengine.graph.Node;

public class YageEMFAdapter implements EObject
{

   private Graph graph;


   public YageEMFAdapter(Graph graph)
   {
      this.graph = graph;
   }


   public static Node emfToYageGraph(Graph graph, EObject object)
   {

      EClass eClass = object.eClass();
      Node node = new Node();
      node.setAttribute(Node.TYPE_ATTRIBUTE, eClass.getName());
      graph.addNode(node);

      for (EAttribute attribute : eClass.getEAllAttributes())
      {
         Object value = object.eGet(attribute);
         if (value instanceof EList)
         {
            System.err.println("collection attributes are not supported in yage");
         }
         else
         {
            if (value instanceof String)
            {
               String string = (String) value;
               if (string.contains(","))
               {
                  String[] split = string.split(",");
                  Node nameNode = getNameNode(graph, attribute, split[0].replaceAll("\\s", ""));
                  node.addEdge(HelperConstants.NAME, nameNode);

                  Node surnameNode = getNameNode(graph, attribute, split[1].replaceAll("\\s", ""));
                  node.addEdge(HelperConstants.SURNAME, surnameNode);
               }
               else
               {
                  Node nameNode = getNameNode(graph, attribute, string);
                  node.addEdge(attribute.getName(), nameNode);
               }
            }
            else if (value instanceof Date)
            {

               node.setAttribute(attribute.getName(), value.toString());
            }
            else
            {
               node.setAttribute(attribute.getName(), value);
            }
         }
      }

      for (EReference reference : eClass.getEAllContainments())
      {
         if (reference.getName().contains("Inverse"))
         {
            continue;
         }

         Object value = object.eGet(reference);

         Object referenceValue = object.eGet(reference);

         if (referenceValue instanceof EList)
         {
            EList<EObject> eList = (EList) referenceValue;

            for (EObject eObject : eList)
            {
               Node referenceNode = getComparingNode(graph, eObject);
               node.addEdge(reference.getName(), referenceNode);
            }

         }
         else
         {
            if (referenceValue != null)
            {
               Node referenceNode = getComparingNode(graph, (EObject) referenceValue);
               node.addEdge(reference.getName(), referenceNode);
            }
         }

      }

      for (EReference reference : eClass.getEAllReferences())
      {
         if (reference.getName().contains("Inverse"))
         {
            continue;
         }
         Object value = object.eGet(reference);

         Object referenceValue = object.eGet(reference);

         if (referenceValue instanceof EList)
         {
            EList<EObject> eList = (EList) referenceValue;

            for (EObject eObject : eList)
            {
               Node referenceNode = getComparingNode(graph, eObject);
               node.addEdge(reference.getName(), referenceNode);
            }

         }
         else
         {
            if (referenceValue != null)
            {
               Node referenceNode = getComparingNode(graph, (EObject) referenceValue);
               node.addEdge(reference.getName(), referenceNode);
            }
         }
      }

      return node;

   }


   private static Node getNameNode(Graph graph, EAttribute attribute, String value)
   {
      Node nameNode = null;
      Optional<Node> nameopt = graph.getNodes().stream()
         .filter(f -> f.getAttributes().containsKey("name") && f.getAttributes().get("name").equals(value))
         .findAny();
      if (nameopt.isPresent())
      {
         nameNode = nameopt.get();
      }
      else
      {
         nameNode = new Node();
         nameNode.setAttribute("name", value);
         nameNode.setAttribute(Node.TYPE_ATTRIBUTE, "String");
         graph.addNode(nameNode);
      }
      return nameNode;
   }


   public static Node getComparingNode(Graph graph, EObject eObject)
   {
      List<Node> collect = graph.getNodes().stream().filter(n -> {
         boolean result = false;
         EClass eClass = eObject.eClass();

         if (!eClass.getName().equals(n.getAttribute(Node.TYPE_ATTRIBUTE)))
         {
            return false;
         }

         for (EAttribute attribute : eClass.getEAllAttributes())
         {

            Object value = eObject.eGet(attribute);
            if (value instanceof EList)
            {
               System.err.println("collection attributes are not supported in yage");
            }
            else if (value instanceof String)
            {

               String string = (String) value;
               if (string.contains(","))
               {
                  String[] split = string.split(",");

                  boolean tmpResult = false, tmpResult2 = false;

                  ArrayList<Node> edges = n.getEdges(HelperConstants.NAME);

                  for (Node node : edges)
                  {
                     tmpResult |= node.getAttribute("name").equals(split[0].replaceAll("\\s", ""));
                  }

                  edges = n.getEdges(HelperConstants.SURNAME);

                  for (Node node : edges)
                  {
                     tmpResult2 |= node.getAttribute("name").equals(split[1].replaceAll("\\s", ""));
                  }

                  result |= tmpResult & tmpResult2;

               }
               else
               {
                  ArrayList<Node> edges = n.getEdges(attribute.getName());

                  for (Node node : edges)
                  {
                     result |= node.getAttribute("name").equals(value);
                  }
               }

            }
            else
            {
               Object attributeVal = n.getAttribute(attribute.getName());
               result |= attributeVal != null && attributeVal.equals(value);
            }
         }
         return result;
      }).collect(Collectors.toList());

      if (collect.size() == 1)
      {
         return collect.get(0);
      }
      else if (collect.size() > 1)
      {
         System.err.println("Ahh found multible matches this shoud not happen \nusing first");
         return collect.get(0);
      }
      else
      {

         // create new

         return emfToYageGraph(graph, eObject);
      }

   }


   @Override
   public EClass eClass()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public Resource eResource()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public EObject eContainer()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public EStructuralFeature eContainingFeature()
   {

      throw new UnsupportedOperationException();
   }


   @Override
   public EReference eContainmentFeature()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public EList<EObject> eContents()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public TreeIterator<EObject> eAllContents()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public boolean eIsProxy()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public EList<EObject> eCrossReferences()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public Object eGet(EStructuralFeature feature)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public Object eGet(EStructuralFeature feature, boolean resolve)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void eSet(EStructuralFeature feature, Object newValue)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public boolean eIsSet(EStructuralFeature feature)
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void eUnset(EStructuralFeature feature)
   {
      throw new UnsupportedOperationException();

   }


   @Override
   public Object eInvoke(EOperation operation, EList<?> arguments) throws InvocationTargetException
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public EList<Adapter> eAdapters()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public boolean eDeliver()
   {
      throw new UnsupportedOperationException();
   }


   @Override
   public void eSetDeliver(boolean deliver)
   {
      throw new UnsupportedOperationException();

   }


   @Override
   public void eNotify(Notification notification)
   {
      throw new UnsupportedOperationException();

   }


   public Graph getGraph()
   {
      return graph;
   }


   public void setGraph(Graph graph)
   {
      this.graph = graph;
   }

   public class ListAdapter<T extends EObject> extends BasicEList<T>
   {

      private Graph graph;


      public ListAdapter(Graph graph)
      {
         this.graph = graph;
         // TODO Auto-generated constructor stub
      }


      @Override
      public boolean add(T object)
      {
         Node comparingNode = YageEMFAdapter.getComparingNode(graph, object);
         return true;
      }
   }
}
