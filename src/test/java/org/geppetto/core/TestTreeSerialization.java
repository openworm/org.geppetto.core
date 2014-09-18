/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011, 2013 OpenWorm.
 * http://openworm.org
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *     	OpenWorm - http://openworm.org/people.html
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package org.geppetto.core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.CylinderNode;
import org.geppetto.core.model.runtime.DynamicsSpecificationNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.FunctionNode;
import org.geppetto.core.model.runtime.ParameterNode;
import org.geppetto.core.model.runtime.ParameterSpecificationNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.runtime.TextMetadataNode;
import org.geppetto.core.model.state.visitors.SerializeTreeVisitor;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.DoubleValue;
import org.geppetto.core.model.values.ValuesFactory;
import org.geppetto.core.visualisation.model.Point;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestTreeSerialization {

	@Test
	public void testTime() {
		CompositeNode rootNode = new CompositeNode("TIME");
		
		VariableNode stepNode = new VariableNode("step");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(0.05));
		quantity.setUnit("ms");
		stepNode.addPhysicalQuantity(quantity);
		
		VariableNode dummyNode = new VariableNode("time");
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(0.04));
		quantity2.setUnit("ms");
		dummyNode.addPhysicalQuantity(quantity2);
		
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(0.05));
		quantity3.setUnit("ms");
		dummyNode.addPhysicalQuantity(quantity3);
		
		rootNode.addChild(stepNode);
		rootNode.addChild(dummyNode);		
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		
		System.out.println(serialized);
		Assert.assertEquals("{\"TIME\":{\"step\":{\"value\":0.05,\"unit\":\"ms\",\"scale\":null,\"instancePath\":\"step\",\"_metaType\":\"VariableNode\"},\"time\":{\"value\":0.04,\"unit\":\"ms\",\"scale\":null,\"instancePath\":\"time\",\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeNode\"}}", serialized);
	}
	
	@Test
	public void testTreeSerialization() {
		CompositeNode rootNode = new CompositeNode("WATCH_TREE");
		
		VariableNode dummyNode = new VariableNode("dummyFloat");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(50d));
		dummyNode.addPhysicalQuantity(quantity);
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		dummyNode.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode = new VariableNode("dummyDouble");
		
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode.addPhysicalQuantity(quantity3);
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		anotherDummyNode.addPhysicalQuantity(quantity4);
				
		rootNode.addChild(dummyNode);
		rootNode.addChild(anotherDummyNode);
		
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"dummyFloat\":{\"value\":50.0,\"unit\":null,\"scale\":null,\"instancePath\":\"dummyFloat\",\"_metaType\":\"VariableNode\"},\"dummyDouble\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"instancePath\":\"dummyDouble\",\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeNode\"}}", serialized);
	}

	@Test
	public void testTreeWithUnits() {
		CompositeNode rootNode = new CompositeNode("WATCH_TREE");
		
		AValue val = ValuesFactory.getDoubleValue(50d);
		AValue val2 = ValuesFactory.getDoubleValue(100d);
		
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(val);
		quantity.setUnit("V");
		quantity.setScalingFactor("1.E3");
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(val2);
		quantity2.setScalingFactor("1.E3");
		quantity2.setUnit("V");
	
		VariableNode dummyNode = new VariableNode("dummyFloat");
		dummyNode.addPhysicalQuantity(quantity);
		dummyNode.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode = new VariableNode("dummyDouble");
		
		AValue val3= ValuesFactory.getDoubleValue(50d);
		AValue val4 = ValuesFactory.getDoubleValue(100d);
		
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(val3);
		quantity3.setUnit("mV");
		quantity3.setScalingFactor("1.E3");
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(val4);
		quantity4.setScalingFactor("1.E3");
		quantity4.setUnit("mV");
				
		anotherDummyNode.addPhysicalQuantity(quantity3);
		anotherDummyNode.addPhysicalQuantity(quantity4);
		rootNode.addChild(dummyNode);
		rootNode.addChild(anotherDummyNode);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"dummyFloat\":{\"value\":50.0,\"unit\":\"V\",\"scale\":\"1.E3\",\"instancePath\":\"dummyFloat\",\"_metaType\":\"VariableNode\"},\"dummyDouble\":{\"value\":50.0,\"unit\":\"mV\",\"scale\":\"1.E3\",\"instancePath\":\"dummyDouble\",\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeNode\"}}", serialized);
	}
	
	@Test
	public void testTreeNestedSerialization() {
		CompositeNode rootNode = new CompositeNode("WATCH_TREE");
		CompositeNode dummyNode0 = new CompositeNode("hhpop[0]");
		CompositeNode dummyNode1 = new CompositeNode("hhpop[1]");

		VariableNode anotherDummyNode0 = new VariableNode("v");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode0.addPhysicalQuantity(quantity);
		anotherDummyNode0.addPhysicalQuantity(quantity2);
		
		rootNode.addChild(dummyNode0);
		dummyNode0.addChild(anotherDummyNode0);
		
		VariableNode anotherDummyNode1 = new VariableNode("v");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(55d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(65d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity3);
		anotherDummyNode1.addPhysicalQuantity(quantity4);
		
		rootNode.addChild(dummyNode1);
		dummyNode1.addChild(anotherDummyNode1);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"instancePath\":\"hhpop[0].v\",\"_metaType\":\"VariableNode\"},\"instancePath\":\"hhpop[0]\",\"_metaType\":\"CompositeNode\"},{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null,\"instancePath\":\"hhpop[1].v\",\"_metaType\":\"VariableNode\"},\"instancePath\":\"hhpop[1]\",\"_metaType\":\"CompositeNode\"}],\"_metaType\":\"CompositeNode\"}}", serialized);
	}
	
	/**
	 * Skeleton tree test
	 * @
	 */
	@Test
	public void testSkeletonTree() {
		RuntimeTreeRoot runtime = new RuntimeTreeRoot("root");
		
		EntityNode entity_A = new EntityNode("Entity_A");
		
		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);

		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.WATCH_TREE);

		runtime.addChild(entity_A);
		entity_A.getAspects().add(aspect_A);
		aspect_A.setParent(entity_A);
		aspect_A.addChild(model);
		aspect_A.addChild(visualization);
		aspect_A.addChild(simulation);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtime.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);
		
		System.out.println(prettyJsonString);
		
		Assert.assertEquals("{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"ModelTree\":{\"type\":\"ModelTree\",\"modified\":false,\"instancePath\":\"Entity_A.Aspect_A.ModelTree\",\"_metaType\":\"AspectSubTreeNode\"},\"VisualizationTree\":{\"type\":\"VisualizationTree\",\"modified\":false,\"instancePath\":\"Entity_A.Aspect_A.VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"SimulationTree\":{\"type\":\"SimulationTree\",\"modified\":false,\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}", serialized);
	}
	
	/**
	 * ReCreate Sample Tree below
	 * 
	 *  RuntimeTree -> RuntimeTreeRoot -> ACompositeNode
		  Entity1 -> EntityNode -> ACompositeNode
	    	AspectA -> AspectNode -> ACompositeNode
	        	modelTree -> AspectSubTreeNode -> ACompositeNode
	            	BiophysicalProperties -> CompositeNode -> ACompositeNode
	                	a -> ParameterNode -> ASimpleStateNode
	                	b -> ParameterNode -> ASimpleStateNode
	                	c -> VariableNode -> ASimpleStateNode
	                	text -> TextMetadataNode -> AMetaDataNode
	        	visualisationTree -> AspectSubTreeNode -> ACompositeNode
	            		Sphere1 -> Sphere -> AVisualObject
	            		Cylinder1 -> Cylinder -> AVisualObject
	            		Cylinder2
	            	…
	            		CylinderN
	        	simulationTree -> AspectTree -> ACompositeNode
	            	hhpop -> CompositeNode -> ACompositeNode
	                	0  -> CompositeNode -> ACompositeNode
	                    	v -> VariableNode -> SimpleStateNode
	                    	a -> ParameterNode -> ASimpleStateNode
	 * @
	 */
	@Test
	public void testRefactorSampleTree() {
		
		RuntimeTreeRoot runtimeTree = new RuntimeTreeRoot("RuntimeTree");
		
		EntityNode entity1 = new EntityNode("Entity1");
		
		AspectNode aspectA = new AspectNode("AspectA");
		aspectA.setId("12");
		TestSimulator sim = new TestSimulator();
		aspectA.setSimulator(sim);
		TestModelInterpreter modelInt = new TestModelInterpreter();
		aspectA.setModelInterpreter(modelInt);

		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);
		model.setModified(true);
		
		DynamicsSpecificationNode dynamics = new DynamicsSpecificationNode("Dynamics");
		
		PhysicalQuantity value = new PhysicalQuantity();
		value.setScalingFactor("10");
		value.setUnit("ms");
		value.setValue(new DoubleValue(10));
		dynamics.setInitialConditions(value);
		
		FunctionNode function = new FunctionNode("Function");
		function.setExpression("y=x+2");
		List<String> argumentsF = new ArrayList<String>();
		argumentsF.add("1");
		argumentsF.add("2");
		function.setArgument(argumentsF);
		
		dynamics.setDynamics(function);
		
		ParameterSpecificationNode parameter = new ParameterSpecificationNode("Parameter");
		
		PhysicalQuantity value1 = new PhysicalQuantity();
		value1.setScalingFactor("10");
		value1.setUnit("ms");
		value1.setValue(new DoubleValue(10));	
		
		parameter.setValue(value1);
		
		FunctionNode functionNode = new FunctionNode("FunctionNode");
		functionNode.setExpression("y=x^2");
		List<String> arguments = new ArrayList<String>();
		arguments.add("1");
		functionNode.setArgument(arguments);
		
		
		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);
		visualization.setModified(true);
		
		SphereNode sphere = new SphereNode("sphere");
		Point p = new Point();
		p.setX(new Double(3.3));
		p.setY(new Double(4));
		p.setZ(new Double(-1.444));
		sphere.setPosition(p);
		sphere.setRadius(new Double(33));
		
		CylinderNode cylinder = new CylinderNode("cylinder");
		Point p2 = new Point();
		p2.setX(new Double(6.3));
		p2.setY(new Double(8));
		p2.setZ(new Double(-3.999));
		cylinder.setPosition(p2);
		Point p3 = new Point();
		p3.setX(new Double(6.3));
		p3.setY(new Double(8));
		p3.setZ(new Double(-3.999));
		cylinder.setDistal(p3);
		cylinder.setRadiusBottom(new Double(34.55));
		cylinder.setRadiusTop(new Double(34.55));

		CylinderNode cylinder2 = new CylinderNode("cylinder");
		cylinder2.setPosition(p2);
		cylinder2.setDistal(p3);
		cylinder2.setRadiusBottom(new Double(34.55));
		cylinder2.setRadiusTop(new Double(34.55));
		
		CylinderNode cylinder3 = new CylinderNode("cylinder");
		cylinder3.setPosition(p2);
		cylinder3.setDistal(p3);
		cylinder3.setRadiusBottom(new Double(34.55));
		cylinder3.setRadiusTop(new Double(34.55));
		
		CylinderNode cylinder4 = new CylinderNode("cylinder");
		cylinder4.setPosition(p2);
		cylinder4.setDistal(p3);
		cylinder4.setRadiusBottom(new Double(34.55));
		cylinder4.setRadiusTop(new Double(34.55));
		
		CylinderNode cylinder5 = new CylinderNode("cylinder");
		cylinder5.setPosition(p2);
		cylinder5.setDistal(p3);
		cylinder5.setRadiusBottom(new Double(34.55));
		cylinder5.setRadiusTop(new Double(34.55));
		
		CompositeNode vg = new CompositeNode("vg");
		vg.addChild(sphere);
		vg.addChild(cylinder);
		vg.addChild(cylinder2);
		vg.addChild(cylinder3);
		vg.addChild(cylinder4);
		vg.addChild(cylinder5);
		
		CompositeNode vg2 = new CompositeNode("vg2");
		vg2.addChild(cylinder);
		vg2.addChild(cylinder2);
		vg2.addChild(cylinder3);
		vg2.addChild(cylinder4);
		vg2.addChild(cylinder5);
		vg2.addChild(sphere);
		
		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.WATCH_TREE);
		simulation.setModified(true);
		
		CompositeNode hhpop = new CompositeNode("hhpop[0]");
		
		VariableNode v = new VariableNode("v");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		v.addPhysicalQuantity(quantity);
		v.addPhysicalQuantity(quantity2);
		
		ParameterNode a1 = new ParameterNode("a");
		
		runtimeTree.addChild(entity1);
		entity1.getAspects().add(aspectA);
		aspectA.setParent(entity1);
		aspectA.addChild(model);
		model.addChild(parameter);
		model.addChild(dynamics);
		model.addChild(functionNode);
		
		aspectA.addChild(visualization);
		visualization.addChild(vg);
		visualization.addChild(vg2);
		
		aspectA.addChild(simulation);
		simulation.addChild(hhpop);
		hhpop.addChild(v);
		hhpop.addChild(a1); 
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtimeTree.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);
		
		System.out.println(prettyJsonString);

		Assert.assertEquals("{\"RuntimeTree\":{\"Entity1\":{\"AspectA\":{\"ModelTree\":{\"Parameter\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"instancePath\":\"Entity1.AspectA.ModelTree.Parameter\",\"_metaType\":\"ParameterSpecificationNode\"},\"Dynamics\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"_function\":{\"expression\":\"y=x+2\",\"arguments\":{\"0\":\"1\",\"1\":\"2\"}},\"instancePath\":\"Entity1.AspectA.ModelTree.Dynamics\",\"_metaType\":\"DynamicsSpecificationNode\"},\"FunctionNode\":{\"expression\":\"y=x^2\",\"arguments\":{\"0\":\"1\"},\"instancePath\":\"Entity1.AspectA.ModelTree.FunctionNode\",\"_metaType\":\"FunctionNode\"},\"type\":\"ModelTree\",\"modified\":true,\"instancePath\":\"Entity1.AspectA.ModelTree\",\"_metaType\":\"AspectSubTreeNode\"},\"VisualizationTree\":{\"vg\":{\"sphere\":{\"position\":{\"x\":3.3,\"y\":4.0,\"z\":-1.444},\"radius\":\"33.0\",\"_metaType\":\"SphereNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"instancePath\":\"Entity1.AspectA.VisualizationTree.vg\",\"_metaType\":\"CompositeNode\"},\"vg2\":{\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"distal\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"radiusBottom\":\"34.55\",\"radiusTop\":\"34.55\",\"_metaType\":\"CylinderNode\"},\"sphere\":{\"position\":{\"x\":3.3,\"y\":4.0,\"z\":-1.444},\"radius\":\"33.0\",\"_metaType\":\"SphereNode\"},\"instancePath\":\"Entity1.AspectA.VisualizationTree.vg2\",\"_metaType\":\"CompositeNode\"},\"type\":\"VisualizationTree\",\"modified\":true,\"instancePath\":\"Entity1.AspectA.VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"SimulationTree\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"instancePath\":\"Entity1.AspectA.SimulationTree.hhpop[0].v\",\"_metaType\":\"VariableNode\"},\"a\":{\"instancePath\":\"Entity1.AspectA.SimulationTree.hhpop[0].a\",\"_metaType\":\"ParameterNode\"},\"instancePath\":\"Entity1.AspectA.SimulationTree.hhpop[0]\",\"_metaType\":\"CompositeNode\"}],\"type\":\"SimulationTree\",\"modified\":true,\"instancePath\":\"Entity1.AspectA.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"12\",\"simulator\":\"test\",\"modelInterpreter\":\"Test Model interpreter\",\"instancePath\":\"Entity1.AspectA\",\"_metaType\":\"AspectNode\"},\"instancePath\":\"Entity1\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}", serialized);
	}
	
	@Test
	public void testNetworks() {
		
		RuntimeTreeRoot runtimeTree = new RuntimeTreeRoot("RuntimeTree");
		
		EntityNode entity1 = new EntityNode("Entity1");
		EntityNode entity2 = new EntityNode("Entity2");
		EntityNode entity3 = new EntityNode("Entity3");

		AspectNode aspectA = new AspectNode("Aspect1");
		aspectA.setId("12");
		TestSimulator sim = new TestSimulator();
		aspectA.setSimulator(sim);
		TestModelInterpreter modelInt = new TestModelInterpreter();
		aspectA.setModelInterpreter(modelInt);
		
		AspectNode aspectB = new AspectNode("Aspect2");
		aspectA.setId("125");
		aspectA.setSimulator(sim);
		aspectA.setModelInterpreter(modelInt);
		
		AspectNode aspectC = new AspectNode("Aspect3");
		aspectB.setId("123");
		aspectB.setSimulator(sim);
		aspectB.setModelInterpreter(modelInt);

		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);
		model.setModified(true);
		
		DynamicsSpecificationNode dynamics = new DynamicsSpecificationNode("Dynamics");
		
		PhysicalQuantity value = new PhysicalQuantity();
		value.setScalingFactor("10");
		value.setUnit("ms");
		value.setValue(new DoubleValue(10));
		dynamics.setInitialConditions(value);
		
		FunctionNode function = new FunctionNode("Function");
		function.setExpression("y=x+2");
		List<String> argumentsF = new ArrayList<String>();
		argumentsF.add("1");
		argumentsF.add("2");
		function.setArgument(argumentsF);
		
		dynamics.setDynamics(function);
		
		ParameterSpecificationNode parameter = new ParameterSpecificationNode("Parameter");
		
		PhysicalQuantity value1 = new PhysicalQuantity();
		value1.setScalingFactor("10");
		value1.setUnit("ms");
		value1.setValue(new DoubleValue(10));	
		
		parameter.setValue(value1);
		
		FunctionNode functionNode = new FunctionNode("FunctionNode");
		functionNode.setExpression("y=x^2");
		List<String> arguments = new ArrayList<String>();
		arguments.add("1");
		functionNode.setArgument(arguments);
		
		
		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);
		visualization.setModified(true);
		
		SphereNode sphere = new SphereNode("sphere");
		Point p = new Point();
		p.setX(new Double(3.3));
		p.setY(new Double(4));
		p.setZ(new Double(-1.444));
		sphere.setPosition(p);
		sphere.setRadius(new Double(33));
		
		CylinderNode cylinder = new CylinderNode("cylinder");
		Point p2 = new Point();
		p2.setX(new Double(6.3));
		p2.setY(new Double(8));
		p2.setZ(new Double(-3.999));
		cylinder.setPosition(p2);
		Point p3 = new Point();
		p3.setX(new Double(6.3));
		p3.setY(new Double(8));
		p3.setZ(new Double(-3.999));
		cylinder.setDistal(p3);
		cylinder.setRadiusBottom(new Double(34.55));
		cylinder.setRadiusTop(new Double(34.55));

		CylinderNode cylinder2 = new CylinderNode("cylinder");
		cylinder2.setPosition(p2);
		cylinder2.setDistal(p3);
		cylinder2.setRadiusBottom(new Double(34.55));
		cylinder2.setRadiusTop(new Double(34.55));
		
		CylinderNode cylinder3 = new CylinderNode("cylinder");
		cylinder3.setPosition(p2);
		cylinder3.setDistal(p3);
		cylinder3.setRadiusBottom(new Double(34.55));
		cylinder3.setRadiusTop(new Double(34.55));
		
		CylinderNode cylinder4 = new CylinderNode("cylinder");
		cylinder4.setPosition(p2);
		cylinder4.setDistal(p3);
		cylinder4.setRadiusBottom(new Double(34.55));
		cylinder4.setRadiusTop(new Double(34.55));
		
		CylinderNode cylinder5 = new CylinderNode("cylinder");
		cylinder5.setPosition(p2);
		cylinder5.setDistal(p3);
		cylinder5.setRadiusBottom(new Double(34.55));
		cylinder5.setRadiusTop(new Double(34.55));
		
		CompositeNode vg = new CompositeNode("vg");
		vg.addChild(sphere);
		vg.addChild(cylinder);
		vg.addChild(cylinder2);
		vg.addChild(cylinder3);
		vg.addChild(cylinder4);
		vg.addChild(cylinder5);
		
		CompositeNode vg2 = new CompositeNode("vg2");
		vg2.addChild(cylinder);
		vg2.addChild(cylinder2);
		vg2.addChild(cylinder3);
		vg2.addChild(cylinder4);
		vg2.addChild(cylinder5);
		vg2.addChild(sphere);
		
		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.WATCH_TREE);
		simulation.setModified(true);
		
		CompositeNode hhpop = new CompositeNode("hhpop[0]");
		
		VariableNode v = new VariableNode("v");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		v.addPhysicalQuantity(quantity);
		v.addPhysicalQuantity(quantity2);
		
		ParameterNode a1 = new ParameterNode("a");
		
		runtimeTree.addChild(entity1);
		runtimeTree.addChild(entity2);
		entity1.addChild(entity2);
		entity2.addChild(entity3);
		entity2.getAspects().add(aspectB);
		entity1.getAspects().add(aspectA);
		entity3.getAspects().add(aspectC);
		aspectC.setParent(entity3);
		aspectB.setParent(entity2);
		aspectA.setParent(entity1);
		aspectA.addChild(model);
		model.addChild(parameter);
		model.addChild(dynamics);
		model.addChild(functionNode);
		
		aspectA.addChild(visualization);
		visualization.addChild(vg);
		visualization.addChild(vg2);
		
		aspectA.addChild(simulation);
		simulation.addChild(hhpop);
		hhpop.addChild(v);
		hhpop.addChild(a1); 
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtimeTree.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);
		
		System.out.println(prettyJsonString);
	}
}