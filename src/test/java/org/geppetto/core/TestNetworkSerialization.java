/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011 - 2015 OpenWorm.
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
/**
 * Tests for networks
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 */
package org.geppetto.core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.ParameterNode;
import org.geppetto.core.model.runtime.VisualGroupElementNode;
import org.geppetto.core.model.runtime.VisualGroupNode;
import org.geppetto.core.model.runtime.VisualObjectReferenceNode;
import org.geppetto.core.model.typesystem.AspectNode;
import org.geppetto.core.model.typesystem.Root;
import org.geppetto.core.model.typesystem.types.ConnectionType;
import org.geppetto.core.model.typesystem.values.CompositeValue;
import org.geppetto.core.model.typesystem.values.ConnectionValue;
import org.geppetto.core.model.typesystem.values.CylinderValue;
import org.geppetto.core.model.typesystem.values.DoubleValue;
import org.geppetto.core.model.typesystem.values.DynamicsSpecificationValue;
import org.geppetto.core.model.typesystem.values.FunctionValue;
import org.geppetto.core.model.typesystem.values.ParameterValue;
import org.geppetto.core.model.typesystem.values.PhysicalQuantityValue;
import org.geppetto.core.model.typesystem.values.QuantityValue;
import org.geppetto.core.model.typesystem.values.SphereValue;
import org.geppetto.core.model.typesystem.values.TextMetadataValue;
import org.geppetto.core.model.typesystem.values.URLMetadataValue;
import org.geppetto.core.model.typesystem.values.Unit;
import org.geppetto.core.model.typesystem.values.ValuesFactory;
import org.geppetto.core.model.typesystem.values.VariableValue;
import org.geppetto.core.model.typesystem.visitor.SerializeTreeVisitor;
import org.geppetto.core.visualisation.model.Point;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestNetworkSerialization {

	@Test
	public void testVisualGroups() {

		Root runtime = new Root("root");

		EntityNode hhcell = new EntityNode("hhcell");
		
		EntityNode purkinje = new EntityNode("purkinje");

		AspectNode electrical = new AspectNode("electrical");
		AspectNode electrical2 = new AspectNode("electrical");

		AspectSubTreeNode visualization = electrical.getSubTree(AspectTreeType.VISUALIZATION_TREE);
		SphereValue sphere = new SphereValue("purkinje");
		visualization.addChild(sphere);

		runtime.addChild(hhcell);
		runtime.addChild(purkinje);
		hhcell.getAspects().add(electrical);
		purkinje.getAspects().add(electrical2);
		electrical.setParent(hhcell);
		electrical2.setParent(purkinje);
		
		VisualGroupNode group = new VisualGroupNode("group");
		group.setName("Group 1");
		group.setHighSpectrumColor("red");
		group.setLowSpectrumColor("yellow");
		group.setType("group");
		
		VisualGroupElementNode soma = new VisualGroupElementNode("soma");
		soma.setDefaultColor("orange");
		PhysicalQuantityValue quantity = new PhysicalQuantityValue();
		quantity.setScalingFactor("10");
		quantity.setValue(new DoubleValue(12));
		soma.setParameter(quantity);
		
		VisualGroupElementNode synapse = new VisualGroupElementNode("synapse");
		synapse.setDefaultColor("orange");
		PhysicalQuantityValue quantity2 = new PhysicalQuantityValue();
		quantity2.setScalingFactor("100");
		quantity2.setValue(new DoubleValue(12));
		synapse.setParameter(quantity2);
		
		group.getVisualGroupElements().add(soma);
		group.getVisualGroupElements().add(synapse);
		
		visualization.addChild(group);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtime.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		/*
		 * Parse serialized tree using gson, if it passes it returns pretty json string
		 * and if it fails to validate serialized tree then it throws exception making test fail. 
		 */
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertNotNull(prettyJsonString);
	}
	
	@Test
	public void testConnection() {

		Root runtime = new Root("root");

		EntityNode hhcell = new EntityNode("hhcell");
		
		EntityNode purkinje = new EntityNode("purkinje");

		AspectNode electrical = new AspectNode("electrical");
		AspectNode electrical2 = new AspectNode("electrical");

		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);

		SphereValue sphere = new SphereValue("purkinje");
		visualization.addChild(sphere);

		runtime.addChild(hhcell);
		runtime.addChild(purkinje);
		hhcell.getAspects().add(electrical);
		purkinje.getAspects().add(electrical2);
		electrical.setParent(hhcell);
		electrical2.setParent(purkinje);

		ConnectionValue con1 = new ConnectionValue("Connection_1",electrical);
		con1.setEntityInstancePath(hhcell.getInstancePath());
		con1.setType(ConnectionType.TO);
		con1.setParent(hhcell);
		con1.setName("Connection1");
		hhcell.getConnections().add(con1);
		VisualObjectReferenceNode visObj = new VisualObjectReferenceNode("Vis");
		visObj.setAspectInstancePath(electrical.getInstancePath());
		visObj.setVisualObjectId(sphere.getId());
		TextMetadataValue text = new TextMetadataValue("Text");
		text.setValue(new DoubleValue(2));
		
		URLMetadataValue url = new URLMetadataValue("URL");
		url.setValue(new DoubleValue(2));
		url.setURL("hhtp://url.com");
		
		FunctionValue function = new FunctionValue("Function");
		function.setExpression("x=y^2");
		function.setName("hello");
		
		con1.getCustomNodes().add(text);
		con1.getCustomNodes().add(url);
		con1.getCustomNodes().add(function);
		con1.getVisualReferences().add(visObj);
		
		ConnectionValue con2 = new ConnectionValue("Connection_2",electrical);
		con2.setEntityInstancePath(purkinje.getInstancePath());
		con2.setType(ConnectionType.FROM);
		con2.setParent(purkinje);
		purkinje.getConnections().add(con2);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtime.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		/*
		 * Parse serialized tree using gson, if it passes it returns pretty json string
		 * and if it fails to validate serialized tree then it throws exception making test fail. 
		 */
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);
		
		Assert.assertNotNull(prettyJsonString);
	}

	@Test
	public void testNetworks() {

		Root runtimeTree = new Root("RuntimeTree");

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

		AspectSubTreeNode model = new AspectSubTreeNode(
				AspectTreeType.MODEL_TREE);
		model.setModified(true);

		DynamicsSpecificationValue dynamics = new DynamicsSpecificationValue(
				"Dynamics");

		PhysicalQuantityValue value = new PhysicalQuantityValue();
		value.setScalingFactor("10");
		value.setUnit(new Unit("ms"));
		value.setValue(new DoubleValue(10));
		dynamics.setInitialConditions(value);

		FunctionValue function = new FunctionValue("Function");
		function.setExpression("y=x+2");
		List<String> argumentsF = new ArrayList<String>();
		argumentsF.add("1");
		argumentsF.add("2");
		function.setArgument(argumentsF);

		dynamics.setDynamics(function);

		ParameterValue parameter = new ParameterValue(
				"Parameter");

		PhysicalQuantityValue value1 = new PhysicalQuantityValue();
		value1.setScalingFactor("10");
		value1.setUnit(new Unit("ms"));
		value1.setValue(new DoubleValue(10));

		parameter.setValue(value1);

		FunctionValue functionNode = new FunctionValue("FunctionNode");
		functionNode.setExpression("y=x^2");
		List<String> arguments = new ArrayList<String>();
		arguments.add("1");
		functionNode.setArgument(arguments);

		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);
		visualization.setModified(true);

		SphereValue sphere = new SphereValue("sphere");
		Point p = new Point();
		p.setX(new Double(3.3));
		p.setY(new Double(4));
		p.setZ(new Double(-1.444));
		sphere.setPosition(p);
		sphere.setRadius(new Double(33));

		CylinderValue cylinder = new CylinderValue("cylinder");
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

		CylinderValue cylinder2 = new CylinderValue("cylinder");
		cylinder2.setPosition(p2);
		cylinder2.setDistal(p3);
		cylinder2.setRadiusBottom(new Double(34.55));
		cylinder2.setRadiusTop(new Double(34.55));

		CylinderValue cylinder3 = new CylinderValue("cylinder");
		cylinder3.setPosition(p2);
		cylinder3.setDistal(p3);
		cylinder3.setRadiusBottom(new Double(34.55));
		cylinder3.setRadiusTop(new Double(34.55));

		CylinderValue cylinder4 = new CylinderValue("cylinder");
		cylinder4.setPosition(p2);
		cylinder4.setDistal(p3);
		cylinder4.setRadiusBottom(new Double(34.55));
		cylinder4.setRadiusTop(new Double(34.55));

		CylinderValue cylinder5 = new CylinderValue("cylinder");
		cylinder5.setPosition(p2);
		cylinder5.setDistal(p3);
		cylinder5.setRadiusBottom(new Double(34.55));
		cylinder5.setRadiusTop(new Double(34.55));

		CompositeValue vg = new CompositeValue("vg");
		vg.addChild(sphere);
		vg.addChild(cylinder);
		vg.addChild(cylinder2);
		vg.addChild(cylinder3);
		vg.addChild(cylinder4);
		vg.addChild(cylinder5);

		CompositeValue vg2 = new CompositeValue("vg2");
		vg2.addChild(cylinder);
		vg2.addChild(cylinder2);
		vg2.addChild(cylinder3);
		vg2.addChild(cylinder4);
		vg2.addChild(cylinder5);
		vg2.addChild(sphere);

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.SIMULATION_TREE);
		simulation.setModified(true);

		CompositeValue hhpop = new CompositeValue("hhpop[0]");

		VariableValue v = new VariableValue("v");
		QuantityValue quantity = new QuantityValue();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));

		QuantityValue quantity2 = new QuantityValue();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));

		v.addQuantity(quantity);
		v.addQuantity(quantity2);

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
		model.setModified(false);
		AspectNode parentAspect = ((AspectNode) model.getParent());
		parentAspect.setModified(false);

		aspectA.addChild(visualization);
		visualization.addChild(vg);
		visualization.addChild(vg2);
		visualization.setModified(false);
		parentAspect = ((AspectNode) visualization.getParent());
		parentAspect.setModified(false);

		aspectA.addChild(simulation);
		simulation.addChild(hhpop);
		simulation.setModified(false);
		parentAspect = ((AspectNode) simulation.getParent());
		parentAspect.setModified(false);
		hhpop.addChild(v);
		hhpop.addChild(a1);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtimeTree.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		/*
		 * Parse serialized tree using gson, if it passes it returns pretty json string
		 * and if it fails to validate serialized tree then it throws exception making test fail. 
		 */
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);
		
		Assert.assertNotNull(prettyJsonString);
	}

	@Test
	public void testNetworksModifiedFlag() {

		Root runtimeTree = new Root("RuntimeTree");

		EntityNode entity1 = new EntityNode("Entity1");
		EntityNode entity2 = new EntityNode("Entity2");
		EntityNode entity3 = new EntityNode("Entity3");
		EntityNode entity4 = new EntityNode("Entity4");
		EntityNode entity5 = new EntityNode("Entity5");

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

		AspectSubTreeNode model = new AspectSubTreeNode(
				AspectTreeType.MODEL_TREE);
		model.setModified(true);

		DynamicsSpecificationValue dynamics = new DynamicsSpecificationValue(
				"Dynamics");

		PhysicalQuantityValue value = new PhysicalQuantityValue();
		value.setScalingFactor("10");
		value.setUnit(new Unit("ms"));
		value.setValue(new DoubleValue(10));
		dynamics.setInitialConditions(value);

		FunctionValue function = new FunctionValue("Function");
		function.setExpression("y=x+2");
		List<String> argumentsF = new ArrayList<String>();
		argumentsF.add("1");
		argumentsF.add("2");
		function.setArgument(argumentsF);

		dynamics.setDynamics(function);

		ParameterValue parameter = new ParameterValue(
				"Parameter");

		PhysicalQuantityValue value1 = new PhysicalQuantityValue();
		value1.setScalingFactor("10");
		value1.setUnit(new Unit("ms"));
		value1.setValue(new DoubleValue(10));

		parameter.setValue(value1);

		FunctionValue functionNode = new FunctionValue("FunctionNode");
		functionNode.setExpression("y=x^2");
		List<String> arguments = new ArrayList<String>();
		arguments.add("1");
		functionNode.setArgument(arguments);

		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);
		visualization.setModified(true);

		SphereValue sphere = new SphereValue("sphere");
		Point p = new Point();
		p.setX(new Double(3.3));
		p.setY(new Double(4));
		p.setZ(new Double(-1.444));
		sphere.setPosition(p);
		sphere.setRadius(new Double(33));

		CylinderValue cylinder = new CylinderValue("cylinder");
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

		CylinderValue cylinder2 = new CylinderValue("cylinder");
		cylinder2.setPosition(p2);
		cylinder2.setDistal(p3);
		cylinder2.setRadiusBottom(new Double(34.55));
		cylinder2.setRadiusTop(new Double(34.55));

		CylinderValue cylinder3 = new CylinderValue("cylinder");
		cylinder3.setPosition(p2);
		cylinder3.setDistal(p3);
		cylinder3.setRadiusBottom(new Double(34.55));
		cylinder3.setRadiusTop(new Double(34.55));

		CylinderValue cylinder4 = new CylinderValue("cylinder");
		cylinder4.setPosition(p2);
		cylinder4.setDistal(p3);
		cylinder4.setRadiusBottom(new Double(34.55));
		cylinder4.setRadiusTop(new Double(34.55));

		CylinderValue cylinder5 = new CylinderValue("cylinder");
		cylinder5.setPosition(p2);
		cylinder5.setDistal(p3);
		cylinder5.setRadiusBottom(new Double(34.55));
		cylinder5.setRadiusTop(new Double(34.55));

		CompositeValue vg = new CompositeValue("vg");
		vg.addChild(sphere);
		vg.addChild(cylinder);
		vg.addChild(cylinder2);
		vg.addChild(cylinder3);
		vg.addChild(cylinder4);
		vg.addChild(cylinder5);

		CompositeValue vg2 = new CompositeValue("vg2");
		vg2.addChild(cylinder);
		vg2.addChild(cylinder2);
		vg2.addChild(cylinder3);
		vg2.addChild(cylinder4);
		vg2.addChild(cylinder5);
		vg2.addChild(sphere);

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.SIMULATION_TREE);
		simulation.setModified(true);

		CompositeValue hhpop = new CompositeValue("hhpop[0]");

		VariableValue v = new VariableValue("v");
		QuantityValue quantity = new QuantityValue();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));

		QuantityValue quantity2 = new QuantityValue();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));

		v.addQuantity(quantity);
		v.addQuantity(quantity2);

		ParameterNode a1 = new ParameterNode("a");

		runtimeTree.addChild(entity1);
		runtimeTree.addChild(entity4);
		entity1.addChild(entity2);
		entity2.addChild(entity3);
		entity4.addChild(entity5);
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
		entity5.updateParentEntitiesFlags(true);

		aspectA.addChild(simulation);
		simulation.addChild(hhpop);
		hhpop.addChild(v);
		hhpop.addChild(a1);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtimeTree.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		/*
		 * Parse serialized tree using gson, if it passes it returns pretty json string
		 * and if it fails to validate serialized tree then it throws exception making test fail. 
		 */
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertNotNull(prettyJsonString);
	}

	@Test
	public void testNetworksModifiedFlag2() {

		Root runtimeTree = new Root("RuntimeTree");

		EntityNode entity1 = new EntityNode("Entity1");
		EntityNode entity2 = new EntityNode("Entity2");
		EntityNode entity3 = new EntityNode("Entity3");
		EntityNode entity4 = new EntityNode("Entity4");
		EntityNode entity5 = new EntityNode("Entity5");

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

		AspectSubTreeNode model = new AspectSubTreeNode(
				AspectTreeType.MODEL_TREE);
		model.setModified(true);

		DynamicsSpecificationValue dynamics = new DynamicsSpecificationValue(
				"Dynamics");

		PhysicalQuantityValue value = new PhysicalQuantityValue();
		value.setScalingFactor("10");
		value.setUnit(new Unit("ms"));
		value.setValue(new DoubleValue(10));
		dynamics.setInitialConditions(value);

		FunctionValue function = new FunctionValue("Function");
		function.setExpression("y=x+2");
		List<String> argumentsF = new ArrayList<String>();
		argumentsF.add("1");
		argumentsF.add("2");
		function.setArgument(argumentsF);

		dynamics.setDynamics(function);

		ParameterValue parameter = new ParameterValue(
				"Parameter");

		PhysicalQuantityValue value1 = new PhysicalQuantityValue();
		value1.setScalingFactor("10");
		value1.setUnit(new Unit("ms"));
		value1.setValue(new DoubleValue(10));

		parameter.setValue(value1);

		FunctionValue functionNode = new FunctionValue("FunctionNode");
		functionNode.setExpression("y=x^2");
		List<String> arguments = new ArrayList<String>();
		arguments.add("1");
		functionNode.setArgument(arguments);

		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);
		visualization.setModified(true);

		SphereValue sphere = new SphereValue("sphere");
		Point p = new Point();
		p.setX(new Double(3.3));
		p.setY(new Double(4));
		p.setZ(new Double(-1.444));
		sphere.setPosition(p);
		sphere.setRadius(new Double(33));

		CylinderValue cylinder = new CylinderValue("cylinder");
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

		CylinderValue cylinder2 = new CylinderValue("cylinder");
		cylinder2.setPosition(p2);
		cylinder2.setDistal(p3);
		cylinder2.setRadiusBottom(new Double(34.55));
		cylinder2.setRadiusTop(new Double(34.55));

		CylinderValue cylinder3 = new CylinderValue("cylinder");
		cylinder3.setPosition(p2);
		cylinder3.setDistal(p3);
		cylinder3.setRadiusBottom(new Double(34.55));
		cylinder3.setRadiusTop(new Double(34.55));

		CylinderValue cylinder4 = new CylinderValue("cylinder");
		cylinder4.setPosition(p2);
		cylinder4.setDistal(p3);
		cylinder4.setRadiusBottom(new Double(34.55));
		cylinder4.setRadiusTop(new Double(34.55));

		CylinderValue cylinder5 = new CylinderValue("cylinder");
		cylinder5.setPosition(p2);
		cylinder5.setDistal(p3);
		cylinder5.setRadiusBottom(new Double(34.55));
		cylinder5.setRadiusTop(new Double(34.55));

		CompositeValue vg = new CompositeValue("vg");
		vg.addChild(sphere);
		vg.addChild(cylinder);
		vg.addChild(cylinder2);
		vg.addChild(cylinder3);
		vg.addChild(cylinder4);
		vg.addChild(cylinder5);

		CompositeValue vg2 = new CompositeValue("vg2");
		vg2.addChild(cylinder);
		vg2.addChild(cylinder2);
		vg2.addChild(cylinder3);
		vg2.addChild(cylinder4);
		vg2.addChild(cylinder5);
		vg2.addChild(sphere);

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.SIMULATION_TREE);
		simulation.setModified(true);

		CompositeValue hhpop = new CompositeValue("hhpop[0]");

		VariableValue v = new VariableValue("v");
		QuantityValue quantity = new QuantityValue();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));

		QuantityValue quantity2 = new QuantityValue();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));

		v.addQuantity(quantity);
		v.addQuantity(quantity2);

		ParameterNode a1 = new ParameterNode("a");

		runtimeTree.addChild(entity1);
		runtimeTree.addChild(entity4);
		entity1.addChild(entity2);
		entity2.addChild(entity3);
		entity4.addChild(entity5);
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

		aspectA.setModified(true);
		aspectB.setModified(true);
		entity2.updateParentEntitiesFlags(true);
		entity5.updateParentEntitiesFlags(true);

		aspectA.addChild(simulation);
		simulation.addChild(hhpop);
		hhpop.addChild(v);
		hhpop.addChild(a1);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtimeTree.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		/*
		 * Parse serialized tree using gson, if it passes it returns pretty json string
		 * and if it fails to validate serialized tree then it throws exception making test fail. 
		 */
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertNotNull(prettyJsonString);

	}
}
