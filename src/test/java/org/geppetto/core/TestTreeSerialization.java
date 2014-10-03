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
/**
 * Tests for simulation trees, visualization trees and emulates serializing some common used simulations 
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 */
package org.geppetto.core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.DynamicsSpecificationNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.FunctionNode;
import org.geppetto.core.model.runtime.ParameterSpecificationNode;
import org.geppetto.core.model.runtime.ParticleNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.state.visitors.SerializeTreeVisitor;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.DoubleValue;
import org.geppetto.core.model.values.ValuesFactory;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestTreeSerialization {
	@Test
	public void testTreeSerialization() {
		RuntimeTreeRoot runtime = new RuntimeTreeRoot("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.WATCH_TREE);

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
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"SimulationTree\":{\"dummyFloat\":{\"value\":50.0,\"unit\":null,\"scale\":null,\"id\":\"dummyFloat\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyFloat\",\"_metaType\":\"VariableNode\"},\"dummyDouble\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"id\":\"dummyDouble\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyDouble\",\"_metaType\":\"VariableNode\"},\"type\":\"SimulationTree\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	@Test
	public void testTreeWithUnits() {

		RuntimeTreeRoot runtime = new RuntimeTreeRoot("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.WATCH_TREE);

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

		AValue val3 = ValuesFactory.getDoubleValue(50d);
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
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"SimulationTree\":{\"dummyFloat\":{\"value\":50.0,\"unit\":\"V\",\"scale\":\"1.E3\",\"id\":\"dummyFloat\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyFloat\",\"_metaType\":\"VariableNode\"},\"dummyDouble\":{\"value\":50.0,\"unit\":\"mV\",\"scale\":\"1.E3\",\"id\":\"dummyDouble\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree.dummyDouble\",\"_metaType\":\"VariableNode\"},\"type\":\"SimulationTree\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	@Test
	public void emulateJLemsSimulation() {

		RuntimeTreeRoot runtime = new RuntimeTreeRoot("root");

		EntityNode hhcell = new EntityNode("hhcell");

		AspectNode electrical = new AspectNode("electrical");

		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);

		SphereNode sphere = new SphereNode("hhcell");
		visualization.addChild(sphere);

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.WATCH_TREE);

		CompositeNode hhpop = new CompositeNode("hhpop[0]");
		CompositeNode bio = new CompositeNode("bioPhys1");
		CompositeNode membrane = new CompositeNode("membraneProperties");
		CompositeNode naChans = new CompositeNode("naChans");
		CompositeNode na = new CompositeNode("na");
		CompositeNode m = new CompositeNode("m");

		VariableNode v = new VariableNode("v");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));

		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));

		VariableNode spiking = new VariableNode("spiking");

		VariableNode q = new VariableNode("q");

		v.addPhysicalQuantity(quantity);
		v.addPhysicalQuantity(quantity2);

		spiking.addPhysicalQuantity(quantity);
		q.addPhysicalQuantity(quantity);

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
				"{\"root\":{\"hhcell\":{\"electrical\":{\"SimulationTree\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"id\":\"v\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].v\",\"_metaType\":\"VariableNode\"},\"spiking\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"id\":\"spiking\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].spiking\",\"_metaType\":\"VariableNode\"},\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"id\":\"q\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties.naChans.na.m.q\",\"_metaType\":\"VariableNode\"},\"id\":\"m\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties.naChans.na.m\",\"_metaType\":\"CompositeNode\"},\"id\":\"na\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties.naChans.na\",\"_metaType\":\"CompositeNode\"},\"id\":\"naChans\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties.naChans\",\"_metaType\":\"CompositeNode\"},\"id\":\"membraneProperties\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1.membraneProperties\",\"_metaType\":\"CompositeNode\"},\"id\":\"bioPhys1\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0].bioPhys1\",\"_metaType\":\"CompositeNode\"},\"id\":\"hhpop[0]\",\"instancePath\":\"hhcell.electrical.SimulationTree.hhpop[0]\",\"_metaType\":\"CompositeNode\"}],\"type\":\"SimulationTree\",\"instancePath\":\"hhcell.electrical.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"electrical\",\"instancePath\":\"hhcell.electrical\",\"_metaType\":\"AspectNode\"},\"id\":\"hhcell\",\"instancePath\":\"hhcell\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	@Test
	public void emulateSmallLiquidSimulation() {

		RuntimeTreeRoot runtime = new RuntimeTreeRoot("root");

		EntityNode small = new EntityNode("small");

		AspectNode fluid = new AspectNode("fluid");

		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);

		CompositeNode elastic = new CompositeNode("Elastic");
		CompositeNode liquid = new CompositeNode("Liquid");
		CompositeNode boundary = new CompositeNode("Boundary");

		ParticleNode p0 = new ParticleNode("p[0]");
		ParticleNode p1 = new ParticleNode("p[1]");
		ParticleNode p2 = new ParticleNode("p[2]");

		visualization.addChild(elastic);
		visualization.addChild(liquid);
		visualization.addChild(boundary);

		fluid.addChild(visualization);
		visualization.setModified(true);

		liquid.addChild(p0);
		liquid.addChild(p1);
		liquid.addChild(p2);

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.WATCH_TREE);

		// CompositeNode dummyNode0 = new CompositeNode("particle[0]");
		CompositeNode particle = new CompositeNode("particle[1]");
		CompositeNode position = new CompositeNode("position");

		VariableNode anotherDummyNode0 = new VariableNode("v");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));

		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));

		anotherDummyNode0.addPhysicalQuantity(quantity);
		anotherDummyNode0.addPhysicalQuantity(quantity2);

		VariableNode anotherDummyNode1 = new VariableNode("v");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(55d));

		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(65d));

		anotherDummyNode1.addPhysicalQuantity(quantity3);
		anotherDummyNode1.addPhysicalQuantity(quantity4);

		simulation.addChild(particle);
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

		Assert.assertEquals(
				"{\"root\":{\"small\":{\"fluid\":{\"VisualizationTree\":{\"Elastic\":{\"id\":\"Elastic\",\"instancePath\":\"small.fluid.VisualizationTree.Elastic\",\"_metaType\":\"CompositeNode\"},\"Liquid\":{\"p[0]\":{\"position\":{},\"id\":\"p[0]\",\"_metaType\":\"ParticleNode\"},\"p[1]\":{\"position\":{},\"id\":\"p[1]\",\"_metaType\":\"ParticleNode\"},\"p[2]\":{\"position\":{},\"id\":\"p[2]\",\"_metaType\":\"ParticleNode\"},\"id\":\"Liquid\",\"instancePath\":\"small.fluid.VisualizationTree.Liquid\",\"_metaType\":\"CompositeNode\"},\"Boundary\":{\"id\":\"Boundary\",\"instancePath\":\"small.fluid.VisualizationTree.Boundary\",\"_metaType\":\"CompositeNode\"},\"type\":\"VisualizationTree\",\"instancePath\":\"small.fluid.VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"SimulationTree\":{\"particle\":[{},{\"position\":{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null,\"id\":\"v\",\"instancePath\":\"small.fluid.SimulationTree.particle[1].position.v\",\"_metaType\":\"VariableNode\"},\"id\":\"position\",\"instancePath\":\"small.fluid.SimulationTree.particle[1].position\",\"_metaType\":\"CompositeNode\"},\"id\":\"particle[1]\",\"instancePath\":\"small.fluid.SimulationTree.particle[1]\",\"_metaType\":\"CompositeNode\"}],\"type\":\"SimulationTree\",\"instancePath\":\"small.fluid.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"fluid\",\"instancePath\":\"small.fluid\",\"_metaType\":\"AspectNode\"},\"id\":\"small\",\"instancePath\":\"small\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	/**
	 * Skeleton tree test. One entity, one aspect, and all subtrees modified
	 */
	@Test
	public void testSkeletonRuntimeTree() {
		RuntimeTreeRoot runtime = new RuntimeTreeRoot("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(
				AspectTreeType.MODEL_TREE);

		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.WATCH_TREE);

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
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"ModelTree\":{\"type\":\"ModelTree\",\"instancePath\":\"Entity_A.Aspect_A.ModelTree\",\"_metaType\":\"AspectSubTreeNode\"},\"VisualizationTree\":{\"type\":\"VisualizationTree\",\"instancePath\":\"Entity_A.Aspect_A.VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"SimulationTree\":{\"type\":\"SimulationTree\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	/**
	 * Skeleton tree test. Emulate one entity, one aspect and model tree
	 * modified only
	 */
	@Test
	public void testSkeletonRuntimeTreeOnlyModel() {
		RuntimeTreeRoot runtime = new RuntimeTreeRoot("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(
				AspectTreeType.MODEL_TREE);

		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);

		visualization.setModified(false);

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.WATCH_TREE);

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
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"ModelTree\":{\"type\":\"ModelTree\",\"instancePath\":\"Entity_A.Aspect_A.ModelTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	/**
	 * Skeleton tree test. One entity, one aspect, and only simulation tree
	 * modified
	 */
	@Test
	public void testSkeletonRuntimeTreeOnlySimulation() {
		RuntimeTreeRoot runtime = new RuntimeTreeRoot("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(
				AspectTreeType.MODEL_TREE);
		model.setModified(false);
		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);
		visualization.setModified(false);
		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.WATCH_TREE);

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
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"SimulationTree\":{\"type\":\"SimulationTree\",\"instancePath\":\"Entity_A.Aspect_A.SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	/**
	 * Skeleton tree test. One entity with one aspect, and only visualization
	 * modified
	 */
	@Test
	public void testSkeletonRuntimeTreeOnlyVisualization() {
		RuntimeTreeRoot runtime = new RuntimeTreeRoot("root");

		EntityNode entity_A = new EntityNode("Entity_A");

		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(
				AspectTreeType.MODEL_TREE);
		model.setModified(false);
		AspectSubTreeNode visualization = new AspectSubTreeNode(
				AspectTreeType.VISUALIZATION_TREE);

		AspectSubTreeNode simulation = new AspectSubTreeNode(
				AspectTreeType.WATCH_TREE);
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
				"{\"root\":{\"Entity_A\":{\"Aspect_A\":{\"VisualizationTree\":{\"type\":\"VisualizationTree\",\"instancePath\":\"Entity_A.Aspect_A.VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"Aspect_A\",\"instancePath\":\"Entity_A.Aspect_A\",\"_metaType\":\"AspectNode\"},\"id\":\"Entity_A\",\"instancePath\":\"Entity_A\",\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}",
				serialized);
	}

	/**
	 * Test Model Tree. Emulates retrieving model tree for aspect
	 */
	@Test
	public void emulateGetModelTree() {
		AspectSubTreeNode model = new AspectSubTreeNode(
				AspectTreeType.MODEL_TREE);

		model.setModified(true);

		DynamicsSpecificationNode dynamics = new DynamicsSpecificationNode(
				"Dynamics");

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

		ParameterSpecificationNode parameter = new ParameterSpecificationNode(
				"Parameter");

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

		model.addChild(parameter);
		model.addChild(dynamics);
		model.addChild(functionNode);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		model.apply(visitor);
		String serialized = "{" + visitor.getSerializedTree() + "}";
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertEquals(
				"{\"ModelTree\":{\"Parameter\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"id\":\"Parameter\",\"instancePath\":\"Parameter\",\"_metaType\":\"ParameterSpecificationNode\"},\"Dynamics\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"_function\":{\"expression\":\"y=x+2\",\"arguments\":{\"0\":\"1\",\"1\":\"2\"}},\"id\":\"Dynamics\",\"instancePath\":\"Dynamics\",\"_metaType\":\"DynamicsSpecificationNode\"},\"FunctionNode\":{\"expression\":\"y=x^2\",\"arguments\":{\"0\":\"1\"},\"id\":\"FunctionNode\",\"instancePath\":\"FunctionNode\",\"_metaType\":\"FunctionNode\"},\"type\":\"ModelTree\",\"_metaType\":\"AspectSubTreeNode\"}}",
				serialized);
	}
	
	/**
	 * Test Model Tree. Emulates retrieving model tree for aspect
	 */
	@Test
	public void childWithSameIds() {
		AspectSubTreeNode model = new AspectSubTreeNode(
				AspectTreeType.MODEL_TREE);

		model.setModified(true);
		
		CompositeNode first = new CompositeNode("One");
		first.setId("One_1");
		
		CompositeNode second = new CompositeNode("One");
		second.setId("One_2");

		DynamicsSpecificationNode dynamics = new DynamicsSpecificationNode(
				"Dynamics");

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

		ParameterSpecificationNode parameter = new ParameterSpecificationNode(
				"Parameter");
		
		ParameterSpecificationNode parameter2 = new ParameterSpecificationNode(
				"Parameter");

		PhysicalQuantity value1 = new PhysicalQuantity();
		value1.setScalingFactor("10");
		value1.setUnit("ms");
		value1.setValue(new DoubleValue(10));

		parameter.setValue(value1);
		parameter2.setValue(value1);

		FunctionNode functionNode = new FunctionNode("FunctionNode");
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
		String serialized = "{" + visitor.getSerializedTree() + "}";
		System.out.println(serialized);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);

		System.out.println(prettyJsonString);

		Assert.assertEquals(
				"{\"ModelTree\":{\"One_1\":{\"Parameter\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"id\":\"Parameter\",\"instancePath\":\"One_1.Parameter\",\"_metaType\":\"ParameterSpecificationNode\"},\"Dynamics\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"_function\":{\"expression\":\"y=x+2\",\"arguments\":{\"0\":\"1\",\"1\":\"2\"}},\"id\":\"Dynamics\",\"instancePath\":\"One_1.Dynamics\",\"_metaType\":\"DynamicsSpecificationNode\"},\"FunctionNode\":{\"expression\":\"y=x^2\",\"arguments\":{\"0\":\"1\"},\"id\":\"FunctionNode\",\"instancePath\":\"One_1.FunctionNode\",\"_metaType\":\"FunctionNode\"},\"id\":\"One_1\",\"instancePath\":\"One_1\",\"_metaType\":\"CompositeNode\"},\"One_2\":{\"Parameter\":{\"value\":\"10.0\",\"unit\":\"ms\",\"scale\":\"10\",\"id\":\"Parameter\",\"instancePath\":\"One_2.Parameter\",\"_metaType\":\"ParameterSpecificationNode\"},\"id\":\"One_2\",\"instancePath\":\"One_2\",\"_metaType\":\"CompositeNode\"},\"type\":\"ModelTree\",\"_metaType\":\"AspectSubTreeNode\"}}",
				serialized);
	}
}