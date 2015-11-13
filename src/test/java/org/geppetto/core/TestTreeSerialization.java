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
 * Tests for simulation trees, visualization trees and emulates serializing some common used simulations 
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 */
package org.geppetto.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.typesystem.AspectNode;
import org.geppetto.core.model.typesystem.Root;
import org.geppetto.core.model.typesystem.values.AValue;
import org.geppetto.core.model.typesystem.values.CompositeValue;
import org.geppetto.core.model.typesystem.values.DoubleValue;
import org.geppetto.core.model.typesystem.values.DynamicsSpecificationValue;
import org.geppetto.core.model.typesystem.values.FunctionValue;
import org.geppetto.core.model.typesystem.values.ParameterValue;
import org.geppetto.core.model.typesystem.values.ParticleValue;
import org.geppetto.core.model.typesystem.values.PhysicalQuantityValue;
import org.geppetto.core.model.typesystem.values.QuantityValue;
import org.geppetto.core.model.typesystem.values.SkeletonAnimationValue;
import org.geppetto.core.model.typesystem.values.SphereValue;
import org.geppetto.core.model.typesystem.values.Unit;
import org.geppetto.core.model.typesystem.values.ValuesFactory;
import org.geppetto.core.model.typesystem.values.VariableValue;
import org.geppetto.core.model.typesystem.visitor.SerializeTreeVisitor;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestTreeSerialization
{
	@Test
	public void testTreeSerialization()
	{
		Root runtime = new Root("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);

		VariableValue dummyNode = new VariableValue("dummyFloat");
		dummyNode.setUnit(new Unit("ms"));

		QuantityValue quantity = new QuantityValue();
		quantity.setValue(ValuesFactory.getDoubleValue(50d));
		dummyNode.addQuantity(quantity);

		QuantityValue quantity2 = new QuantityValue();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		dummyNode.addQuantity(quantity2);

		VariableValue anotherDummyNode = new VariableValue("dummyDouble");
		anotherDummyNode.setUnit(new Unit("ms"));

		QuantityValue quantity3 = new QuantityValue();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode.addQuantity(quantity3);

		QuantityValue quantity4 = new QuantityValue();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		anotherDummyNode.addQuantity(quantity4);

		runtime.addChild(entity_A);
		entity_A.getAspects().add(aspect_A);
		aspect_A.setParent(entity_A);
		aspect_A.addChild(simulation);
		simulation.addChild(dummyNode);
		simulation.addChild(anotherDummyNode);

		simulation.setModified(true);
		aspect_A.setModified(true);
		entity_A.setModified(true);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtime.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertEquals(
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"SimulationTree\":{\"dummyFloat\":{\"timeSeries\":{\"quantity0\":{\"value\":50.0,\"scale\":null},\"quantity1\":{\"value\":100.0,\"scale\":null}},\"watched\":\"false\",\"unit\":\"ms\",\"id\":\"dummyFloat\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyFloat\",\"_metaType\":\"VariableNode\"},\"dummyDouble\":{\"timeSeries\":{\"quantity0\":{\"value\":20.0,\"scale\":null},\"quantity1\":{\"value\":100.0,\"scale\":null}},\"watched\":\"false\",\"unit\":\"ms\",\"id\":\"dummyDouble\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyDouble\",\"_metaType\":\"VariableNode\"},\"type\":\"SimulationTree\",\"id\":\"SimulationTree\",\"name\":\"Simulation\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);

	}

	@Test
	public void testTreeSerializationSingleValue()
	{
		Root runtime = new Root("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);

		VariableValue dummyNode = new VariableValue("dummyFloat");
		dummyNode.setUnit(new Unit("ms"));

		QuantityValue quantity = new QuantityValue();
		quantity.setValue(ValuesFactory.getDoubleValue(50d));
		dummyNode.addQuantity(quantity);

		VariableValue anotherDummyNode = new VariableValue("dummyDouble");
		anotherDummyNode.setUnit(new Unit("ms"));

		QuantityValue quantity3 = new QuantityValue();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode.addQuantity(quantity3);

		runtime.addChild(entity_A);
		entity_A.getAspects().add(aspect_A);
		aspect_A.setParent(entity_A);
		aspect_A.addChild(simulation);
		simulation.addChild(dummyNode);
		simulation.addChild(anotherDummyNode);

		simulation.setModified(true);
		aspect_A.setModified(true);
		entity_A.setModified(true);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtime.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertEquals(
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"SimulationTree\":{\"dummyFloat\":{\"timeSeries\":{\"quantity0\":{\"value\":50.0,\"scale\":null}},\"watched\":\"false\",\"unit\":\"ms\",\"id\":\"dummyFloat\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyFloat\",\"_metaType\":\"VariableNode\"},\"dummyDouble\":{\"timeSeries\":{\"quantity0\":{\"value\":20.0,\"scale\":null}},\"watched\":\"false\",\"unit\":\"ms\",\"id\":\"dummyDouble\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyDouble\",\"_metaType\":\"VariableNode\"},\"type\":\"SimulationTree\",\"id\":\"SimulationTree\",\"name\":\"Simulation\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	@Test
	public void testTreeWithUnits()
	{

		Root runtime = new Root("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);

		AValue val = ValuesFactory.getDoubleValue(50d);

		QuantityValue quantity = new QuantityValue();
		quantity.setValue(val);
		quantity.setScalingFactor("1.E3");

		VariableValue dummyNode = new VariableValue("dummyFloat");
		dummyNode.setUnit(new Unit("V"));
		dummyNode.addQuantity(quantity);

		AValue val3 = ValuesFactory.getDoubleValue(50d);

		QuantityValue quantity3 = new QuantityValue();
		quantity3.setValue(val3);
		quantity3.setScalingFactor("1.E3");

		VariableValue anotherDummyNode = new VariableValue("dummyDouble");
		anotherDummyNode.addQuantity(quantity3);
		anotherDummyNode.setUnit(new Unit("mV"));

		runtime.addChild(entity_A);
		entity_A.getAspects().add(aspect_A);
		aspect_A.setParent(entity_A);
		aspect_A.addChild(simulation);
		simulation.addChild(dummyNode);
		simulation.addChild(anotherDummyNode);

		simulation.setModified(true);
		aspect_A.setModified(true);
		entity_A.setModified(true);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtime.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertEquals(
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"SimulationTree\":{\"dummyFloat\":{\"timeSeries\":{\"quantity0\":{\"value\":50.0,\"scale\":\"1.E3\"}},\"watched\":\"false\",\"unit\":\"V\",\"id\":\"dummyFloat\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyFloat\",\"_metaType\":\"VariableNode\"},\"dummyDouble\":{\"timeSeries\":{\"quantity0\":{\"value\":50.0,\"scale\":\"1.E3\"}},\"watched\":\"false\",\"unit\":\"mV\",\"id\":\"dummyDouble\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyDouble\",\"_metaType\":\"VariableNode\"},\"type\":\"SimulationTree\",\"id\":\"SimulationTree\",\"name\":\"Simulation\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	@Test
	public void emulateJLemsSimulation()
	{

		Root runtime = new Root("root");

		EntityNode hhcell = new EntityNode("hhcell");

		AspectNode electrical = new AspectNode("electrical");

		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);

		SphereValue sphere = new SphereValue("hhcell");
		visualization.addChild(sphere);

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);

		CompositeValue hhpop = new CompositeValue("hhpop[0]");
		CompositeValue bio = new CompositeValue("bioPhys1");
		CompositeValue membrane = new CompositeValue("membraneProperties");
		CompositeValue naChans = new CompositeValue("naChans");
		CompositeValue na = new CompositeValue("na");
		CompositeValue m = new CompositeValue("m");

		VariableValue v = new VariableValue("v");
		QuantityValue quantity = new QuantityValue();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));

		QuantityValue quantity2 = new QuantityValue();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));

		VariableValue spiking = new VariableValue("spiking");

		VariableValue q = new VariableValue("q");

		v.addQuantity(quantity);
		spiking.addQuantity(quantity);
		q.addQuantity(quantity);

		simulation.addChild(hhpop);
		hhpop.addChild(v);
		hhpop.addChild(spiking);
		hhpop.addChild(bio);
		bio.addChild(membrane);
		membrane.addChild(naChans);
		naChans.addChild(na);
		na.addChild(m);
		m.addChild(q);

		runtime.addChild(hhcell);
		hhcell.getAspects().add(electrical);
		electrical.setParent(hhcell);
		electrical.addChild(simulation);

		simulation.setModified(true);
		electrical.setModified(true);
		electrical.setModified(true);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtime.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertEquals(
				"{\"root\":{\"hhcell\":{\"electrical\":{\"SimulationTree\":{\"hhpop\":[{\"v\":{\"timeSeries\":{\"quantity0\":{\"value\":20.0,\"scale\":null}},\"watched\":\"false\",\"unit\":null,\"id\":\"v\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].v\",\"_metaType\":\"VariableNode\"},\"spiking\":{\"timeSeries\":{\"quantity0\":{\"value\":20.0,\"scale\":null}},\"watched\":\"false\",\"unit\":null,\"id\":\"spiking\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].spiking\",\"_metaType\":\"VariableNode\"},\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"na\":{\"m\":{\"q\":{\"timeSeries\":{\"quantity0\":{\"value\":20.0,\"scale\":null}},\"watched\":\"false\",\"unit\":null,\"id\":\"q\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties.naChans.na.m.q\",\"_metaType\":\"VariableNode\"},\"id\":\"m\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties.naChans.na.m\",\"_metaType\":\"CompositeNode\"},\"id\":\"na\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties.naChans.na\",\"_metaType\":\"CompositeNode\"},\"id\":\"naChans\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties.naChans\",\"_metaType\":\"CompositeNode\"},\"id\":\"membraneProperties\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties\",\"_metaType\":\"CompositeNode\"},\"id\":\"bioPhys1\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1\",\"_metaType\":\"CompositeNode\"},\"id\":\"hhpop[0]\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0]\",\"_metaType\":\"CompositeNode\"}],\"type\":\"SimulationTree\",\"id\":\"SimulationTree\",\"name\":\"Simulation\",\"instancePath\":\"hhcell.electrical.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"electrical\",\"instancePath\":\"hhcell.electrical\",\"_metaType\":\"AspectNode\"},\"id\":\"hhcell\",\"instancePath\":\"hhcell\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	@Test
	public void emulateSmallLiquidSimulation()
	{

		Root runtime = new Root("root");

		EntityNode small = new EntityNode("small");

		AspectNode fluid = new AspectNode("fluid");

		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);

		CompositeValue elastic = new CompositeValue("Elastic");
		CompositeValue liquid = new CompositeValue("Liquid");
		CompositeValue boundary = new CompositeValue("Boundary");

		ParticleValue p0 = new ParticleValue("p[0]");
		ParticleValue p1 = new ParticleValue("p[1]");
		ParticleValue p2 = new ParticleValue("p[2]");

		visualization.addChild(elastic);
		visualization.addChild(liquid);
		visualization.addChild(boundary);

		fluid.addChild(visualization);
		visualization.setModified(true);

		liquid.addChild(p0);
		liquid.addChild(p1);
		liquid.addChild(p2);

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);

		CompositeValue particle0 = new CompositeValue("particle[0]");
		CompositeValue particle = new CompositeValue("particle[1]");
		CompositeValue particle2 = new CompositeValue("particle[2]");
		CompositeValue position = new CompositeValue("position");

		VariableValue anotherDummyNode0 = new VariableValue("v");
		QuantityValue quantity = new QuantityValue();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));

		QuantityValue quantity2 = new QuantityValue();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));

		anotherDummyNode0.addQuantity(quantity);
		anotherDummyNode0.addQuantity(quantity2);

		VariableValue anotherDummyNode1 = new VariableValue("v");
		QuantityValue quantity3 = new QuantityValue();
		quantity3.setValue(ValuesFactory.getDoubleValue(55d));

		QuantityValue quantity4 = new QuantityValue();
		quantity4.setValue(ValuesFactory.getDoubleValue(65d));

		anotherDummyNode1.addQuantity(quantity3);
		anotherDummyNode1.addQuantity(quantity4);

		simulation.addChild(particle0);
		simulation.addChild(particle);
		particle2.addChild(position);
		simulation.addChild(particle2);
		particle.addChild(position);
		position.addChild(anotherDummyNode1);

		runtime.addChild(small);
		small.getAspects().add(fluid);
		fluid.setParent(small);
		fluid.addChild(simulation);

		simulation.setModified(true);
		fluid.setModified(true);
		small.updateParentEntitiesFlags(true);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		runtime.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		// Assert.assertEquals("{\"root\":{\"small\":{\"fluid\":{\"VisualizationTree\":{\"Elastic\":{\"id\":\"Elastic\",\"instancePath\":\"small.fluid.VisualizationTree.Elastic\",\"_metaType\":\"CompositeNode\"},\"Liquid\":{\"p[0]\":{\"position\":{},\"id\":\"p[0]\",\"instancePath\":\"small.fluid.VisualizationTree.Liquid.p[0]\",\"_metaType\":\"ParticleNode\"},\"p[1]\":{\"position\":{},\"id\":\"p[1]\",\"instancePath\":\"small.fluid.VisualizationTree.Liquid.p[1]\",\"_metaType\":\"ParticleNode\"},\"p[2]\":{\"position\":{},\"id\":\"p[2]\",\"instancePath\":\"small.fluid.VisualizationTree.Liquid.p[2]\",\"_metaType\":\"ParticleNode\"},\"id\":\"Liquid\",\"instancePath\":\"small.fluid.VisualizationTree.Liquid\",\"_metaType\":\"CompositeNode\"},\"Boundary\":{\"id\":\"Boundary\",\"instancePath\":\"small.fluid.VisualizationTree.Boundary\",\"_metaType\":\"CompositeNode\"},\"type\":\"VisualizationTree\",\"id\":\"VisualizationTree\",\"name\":\"Visualization\",\"instancePath\":\"small.fluid.VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"SimulationTree\":{\"particle\":[{},{\"position\":{\"v\":{\"timeSeries\":{\"quantity0\":{\"value\":55.0,\"unit\":null,\"scale\":null},\"quantity1\":{\"value\":65.0,\"unit\":null,\"scale\":null}},\"watched\":\"false\",\"id\":\"v\",\"instancePath\":\"small.fluid.SimulationTree.particle[1].position.v\",\"_metaType\":\"VariableNode\"},\"id\":\"position\",\"instancePath\":\"small.fluid.SimulationTree.particle[1].position\",\"_metaType\":\"CompositeNode\"},\"id\":\"particle[1]\",\"instancePath\":\"small.fluid.SimulationTree.particle[1]\",\"_metaType\":\"CompositeNode\"}],\"type\":\"SimulationTree\",\"id\":\"SimulationTree\",\"name\":\"Simulation\",\"instancePath\":\"small.fluid.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"fluid\",\"instancePath\":\"small.fluid\",\"_metaType\":\"AspectNode\"},\"id\":\"small\",\"instancePath\":\"small\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
		// serialized);
	}

	/**
	 * Skeleton tree test. One entity, one aspect, and all subtrees modified
	 */
	@Test
	public void testSkeletonRuntimeTree()
	{
		Root runtime = new Root("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);

		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);

		runtime.addChild(entity_A);
		entity_A.getAspects().add(aspect_A);
		entity_A.setModified(true);
		aspect_A.setParent(entity_A);
		aspect_A.setModified(true);
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
		Assert.assertEquals(
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"ModelTree\":{\"type\":\"ModelTree\",\"id\":\"ModelTree\",\"name\":\"Model\",\"instancePath\":\"Entity_A.Aspect_A.ModelTree\",\"_metaType\":\"AspectSubTreeNode\"},\"VisualizationTree\":{\"type\":\"VisualizationTree\",\"id\":\"VisualizationTree\",\"name\":\"Visualization\",\"instancePath\":\"Entity_A.Aspect_A.VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"SimulationTree\":{\"type\":\"SimulationTree\",\"id\":\"SimulationTree\",\"name\":\"Simulation\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	/**
	 * Skeleton tree test. Emulate one entity, one aspect and model tree modified only
	 */
	@Test
	public void testSkeletonRuntimeTreeOnlyModel()
	{
		Root runtime = new Root("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);

		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);

		visualization.setModified(false);

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);

		simulation.setModified(false);

		runtime.addChild(entity_A);
		entity_A.getAspects().add(aspect_A);
		entity_A.setModified(true);
		aspect_A.setParent(entity_A);
		aspect_A.setModified(true);
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
		Assert.assertEquals(
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"ModelTree\":{\"type\":\"ModelTree\",\"id\":\"ModelTree\",\"name\":\"Model\",\"instancePath\":\"Entity_A.Aspect_A.ModelTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	/**
	 * Skeleton tree test. One entity, one aspect, and only simulation tree modified
	 */
	@Test
	public void testSkeletonRuntimeTreeOnlySimulation()
	{
		Root runtime = new Root("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);
		model.setModified(false);
		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);
		visualization.setModified(false);
		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);

		runtime.addChild(entity_A);
		entity_A.getAspects().add(aspect_A);
		entity_A.setModified(true);
		aspect_A.setParent(entity_A);
		aspect_A.setModified(true);
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
		Assert.assertEquals(
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"SimulationTree\":{\"type\":\"SimulationTree\",\"id\":\"SimulationTree\",\"name\":\"Simulation\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	/**
	 * Skeleton tree test. One entity with one aspect, and only visualization modified
	 */
	@Test
	public void testSkeletonRuntimeTreeOnlyVisualization()
	{
		Root runtime = new Root("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);
		model.setModified(false);
		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);
		simulation.setModified(false);

		runtime.addChild(entity_A);
		entity_A.getAspects().add(aspect_A);
		entity_A.setModified(true);
		aspect_A.setParent(entity_A);
		aspect_A.setModified(true);
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
		Assert.assertEquals(
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"VisualizationTree\":{\"type\":\"VisualizationTree\",\"id\":\"VisualizationTree\",\"name\":\"Visualization\",\"instancePath\":\"Entity_A.Aspect_A.VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	/**
	 * Test Model Tree. Emulates retrieving model tree for aspect
	 */
	@Test
	public void emulateGetModelTree()
	{
		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);

		model.setModified(true);

		DynamicsSpecificationValue dynamics = new DynamicsSpecificationValue("Dynamics");

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

		ParameterValue parameter = new ParameterValue("Parameter");

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

		model.addChild(parameter);
		model.addChild(dynamics);
		model.addChild(functionNode);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		model.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertEquals(
				"{\"ModelTree\":{\"Parameter\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"id\":\"Parameter\",\"instancePath\":\"ModelTree.Parameter\",\"_metaType\":\"ParameterSpecificationNode\"},\"Dynamics\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"_function\":{\"expression\":\"y=x+2\",\"arguments\":{\"0\":\"1\",\"1\":\"2\"}},\"id\":\"Dynamics\",\"instancePath\":\"ModelTree.Dynamics\",\"_metaType\":\"DynamicsSpecificationNode\"},\"FunctionNode\":{\"expression\":\"y=x^2\",\"arguments\":{\"0\":\"1\"},\"id\":\"FunctionNode\",\"instancePath\":\"ModelTree.FunctionNode\",\"_metaType\":\"FunctionNode\"},\"type\":\"ModelTree\",\"id\":\"ModelTree\",\"name\":\"Model\",\"instancePath\":\"ModelTree\",\"_metaType\":\"AspectSubTreeNode\"}}",
				serialized);
	}

	/**
	 * Test Model Tree. Emulates retrieving model tree for aspect
	 */
	@Test
	public void childWithSameIds()
	{
		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);

		model.setModified(true);

		CompositeValue first = new CompositeValue("One");
		first.setId("One_1");

		CompositeValue second = new CompositeValue("One");
		second.setId("One_2");

		DynamicsSpecificationValue dynamics = new DynamicsSpecificationValue("Dynamics");

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

		ParameterValue parameter = new ParameterValue("Parameter");

		ParameterValue parameter2 = new ParameterValue("Parameter");

		PhysicalQuantityValue value1 = new PhysicalQuantityValue();
		value1.setScalingFactor("10");
		value1.setUnit(new Unit("ms"));
		value1.setValue(new DoubleValue(10));

		parameter.setValue(value1);
		parameter2.setValue(value1);

		FunctionValue functionNode = new FunctionValue("FunctionNode");
		functionNode.setExpression("y=x^2");
		List<String> arguments = new ArrayList<String>();
		arguments.add("1");
		functionNode.setArgument(arguments);

		first.addChild(parameter);
		first.addChild(dynamics);
		first.addChild(functionNode);

		second.addChild(parameter2);

		model.addChild(first);
		model.addChild(second);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		model.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertEquals(
				"{\"ModelTree\":{\"One_1\":{\"Parameter\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"id\":\"Parameter\",\"instancePath\":\"ModelTree.One_1.Parameter\",\"_metaType\":\"ParameterSpecificationNode\"},\"Dynamics\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"_function\":{\"expression\":\"y=x+2\",\"arguments\":{\"0\":\"1\",\"1\":\"2\"}},\"id\":\"Dynamics\",\"instancePath\":\"ModelTree.One_1.Dynamics\",\"_metaType\":\"DynamicsSpecificationNode\"},\"FunctionNode\":{\"expression\":\"y=x^2\",\"arguments\":{\"0\":\"1\"},\"id\":\"FunctionNode\",\"instancePath\":\"ModelTree.One_1.FunctionNode\",\"_metaType\":\"FunctionNode\"},\"id\":\"One_1\",\"instancePath\":\"ModelTree.One_1\",\"_metaType\":\"CompositeNode\"},\"One_2\":{\"Parameter\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"id\":\"Parameter\",\"instancePath\":\"ModelTree.One_2.Parameter\",\"_metaType\":\"ParameterSpecificationNode\"},\"id\":\"One_2\",\"instancePath\":\"ModelTree.One_2\",\"_metaType\":\"CompositeNode\"},\"type\":\"ModelTree\",\"id\":\"ModelTree\",\"name\":\"Model\",\"instancePath\":\"ModelTree\",\"_metaType\":\"AspectSubTreeNode\"}}",
				serialized);
	}
	
	/**
	 * Test tree serialization with skeleton animation node.
	 */
	@Test
	public void treeWithSkeletonAnimationNode() {
		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);
		visualization.setModified(true);
		
		CompositeValue first = new CompositeValue("CompositeNode");
		first.setId("Composite_XXX");
		
		SkeletonAnimationValue skeletonNode = new SkeletonAnimationValue("SkeletonAnimation");
		skeletonNode.setId("Skeleton_XXX");
		
		List<Double> matrix1 = new ArrayList<Double>();
		matrix1.addAll(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0));
		
		List<Double> matrix2 = new ArrayList<Double>();
		matrix2.addAll(Arrays.asList(10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0));
		
		List<Double> matrix3 = new ArrayList<Double>();
		matrix3.addAll(Arrays.asList(19.0, 20.0, 21.0, 22.0, 23.0, 24.0, 25.0, 26.0, 27.0));
		
		skeletonNode.setSkeletonTransformationSeries(Arrays.asList(matrix1, matrix2, matrix3));
		
		// build tree
		first.addChild(skeletonNode);
		visualization.addChild(first);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		visualization.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertEquals("{\"VisualizationTree\":{\"Composite_XXX\":{\"Skeleton_XXX\":{\"skeletonTransformations\":[[1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0],[10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0],[19.0, 20.0, 21.0, 22.0, 23.0, 24.0, 25.0, 26.0, 27.0]],\"id\":\"Skeleton_XXX\",\"instancePath\":\"VisualizationTree.Composite_XXX.Skeleton_XXX\",\"_metaType\":\"SkeletonAnimationNode\"},\"id\":\"Composite_XXX\",\"instancePath\":\"VisualizationTree.Composite_XXX\",\"_metaType\":\"CompositeNode\"},\"type\":\"VisualizationTree\",\"id\":\"VisualizationTree\",\"name\":\"Visualization\",\"instancePath\":\"VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"}}", serialized);		
	}
}
