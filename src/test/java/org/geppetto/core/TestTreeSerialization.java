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

import junit.framework.Assert;

import org.geppetto.core.model.state.AspectNode;
import org.geppetto.core.model.state.AspectTreeNode;
import org.geppetto.core.model.state.CompositeVariableNode;
import org.geppetto.core.model.state.CylinderNode;
import org.geppetto.core.model.state.EntityNode;
import org.geppetto.core.model.state.ParameterNode;
import org.geppetto.core.model.state.RuntimeTreeRoot;
import org.geppetto.core.model.state.SphereNode;
import org.geppetto.core.model.state.StateVariableNode;
import org.geppetto.core.model.state.TextMetadataNode;
import org.geppetto.core.model.state.visitors.SerializeTreeVisitor;
import org.geppetto.core.model.values.AValue;
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
		CompositeVariableNode rootNode = new CompositeVariableNode("TIME");
		
		StateVariableNode stepNode = new StateVariableNode("step");
		stepNode.addValue(ValuesFactory.getDoubleValue(0.05));
		stepNode.setUnit("ms");
		
		StateVariableNode dummyNode = new StateVariableNode("time");
		dummyNode.addValue(ValuesFactory.getDoubleValue(0.04));
		dummyNode.addValue(ValuesFactory.getDoubleValue(0.05));
		dummyNode.setUnit("ms");
		
		rootNode.addChild(stepNode);
		rootNode.addChild(dummyNode);		
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		
		System.out.println(serialized);
		Assert.assertEquals("{\"TIME\":{\"step\":{\"value\":0.05,\"unit\":\"ms\",\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"time\":{\"value\":0.04,\"unit\":\"ms\",\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeSerialization() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		
		StateVariableNode dummyNode = new StateVariableNode("dummyFloat");
		dummyNode.addValue(ValuesFactory.getDoubleValue(50d));
		dummyNode.addValue(ValuesFactory.getDoubleValue(100d));
		StateVariableNode anotherDummyNode = new StateVariableNode("dummyDouble");
		anotherDummyNode.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode.addValue(ValuesFactory.getDoubleValue(100d));
		rootNode.addChild(dummyNode);
		rootNode.addChild(anotherDummyNode);
		
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"dummyFloat\":{\"value\":50.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"dummyDouble\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}

	@Test
	public void testTreeWithUnits() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		
		AValue val = ValuesFactory.getDoubleValue(50d);
		AValue val2 = ValuesFactory.getDoubleValue(100d);
		
		StateVariableNode dummyNode = new StateVariableNode("dummyFloat");
		dummyNode.addValue(val);
		dummyNode.addValue(val2);
		
		dummyNode.setUnit("V");
		dummyNode.setScalingFactor("1.E3");

		StateVariableNode anotherDummyNode = new StateVariableNode("dummyDouble");
		
		AValue val3= ValuesFactory.getDoubleValue(50d);
		AValue val4 = ValuesFactory.getDoubleValue(100d);
		
		anotherDummyNode.setUnit("mV");
		anotherDummyNode.setScalingFactor("1.E3");
		
		anotherDummyNode.addValue(val3);
		anotherDummyNode.addValue(val4);
		rootNode.addChild(dummyNode);
		rootNode.addChild(anotherDummyNode);
		
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"dummyFloat\":{\"value\":50.0,\"unit\":\"V\",\"scale\":\"1.E3\",\"_metaType\":\"StateVariableNode\"},\"dummyDouble\":{\"value\":50.0,\"unit\":\"mV\",\"scale\":\"1.E3\",\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeNestedSerialization2() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode0 = new CompositeVariableNode("hhpop[0]");

		StateVariableNode anotherDummyNode0 = new StateVariableNode("v");
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(100d));
		rootNode.addChild(dummyNode0);
		dummyNode0.addChild(anotherDummyNode0);
		
		StateVariableNode anotherDummyNode1 = new StateVariableNode("spiking");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(55d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(65d));
		dummyNode0.addChild(anotherDummyNode1);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"spiking\":{\"value\":55.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeNestedSerialization3() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode c302 = new CompositeVariableNode("c302");
		
		CompositeVariableNode electrical = new CompositeVariableNode("electrical");
		
		CompositeVariableNode adal = new CompositeVariableNode("ADAL");
		
		CompositeVariableNode adal0 = new CompositeVariableNode("0");
		
		CompositeVariableNode adalgeneric_iaf_cell = new CompositeVariableNode("generic_iaf_cell");
		
		CompositeVariableNode adar = new CompositeVariableNode("ADAR");
		
		CompositeVariableNode adar0 = new CompositeVariableNode("0");
		
		CompositeVariableNode adargeneric_iaf_cell = new CompositeVariableNode("generic_iaf_cell");

		StateVariableNode v = new StateVariableNode("v");
		v.addValue(ValuesFactory.getDoubleValue(-0.05430606873466336d));
		rootNode.addChild(c302);
		c302.addChild(electrical);
		electrical.addChild(adal);
		adal.addChild(adal0);
		adal0.addChild(adalgeneric_iaf_cell);
		adalgeneric_iaf_cell.addChild(v);
		
		StateVariableNode adarV = new StateVariableNode("v");
		adarV.addValue(ValuesFactory.getDoubleValue(-0.055433782139120126d));
		
		electrical.addChild(adar);
		adar.addChild(adar0);
		adar0.addChild(adargeneric_iaf_cell);
		adargeneric_iaf_cell.addChild(adarV);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"c302\":{\"electrical\":{\"ADAL\":{\"0\":{\"generic_iaf_cell\":{\"v\":{\"value\":-0.05430606873466336,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"ADAR\":{\"0\":{\"generic_iaf_cell\":{\"v\":{\"value\":-0.055433782139120126,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeNestedSerialization() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode0 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[1]");

		StateVariableNode anotherDummyNode0 = new StateVariableNode("v");
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(100d));
		rootNode.addChild(dummyNode0);
		dummyNode0.addChild(anotherDummyNode0);
		
		StateVariableNode anotherDummyNode1 = new StateVariableNode("v");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(55d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(65d));
		rootNode.addChild(dummyNode1);
		dummyNode1.addChild(anotherDummyNode1);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeNestedSerializationWithGaps() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode0 = new CompositeVariableNode("hhpop[10]");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[15]");

		StateVariableNode anotherDummyNode0 = new StateVariableNode("v");
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(100d));
		rootNode.addChild(dummyNode0);
		dummyNode0.addChild(anotherDummyNode0);
		
		StateVariableNode anotherDummyNode1 = new StateVariableNode("v");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(55d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(65d));
		rootNode.addChild(dummyNode1);
		dummyNode1.addChild(anotherDummyNode1);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{},{},{},{},{},{},{},{},{},{},{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},{},{},{},{},{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	
	@Test
	public void testTreeMultpleCompositeSerialization() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("hhpop[1]");

		StateVariableNode anotherDummyNode1 = new StateVariableNode("v");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode2 = new StateVariableNode("v");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));
		rootNode.addChild(dummyNode1);
		rootNode.addChild(dummyNode2);
		dummyNode1.addChild(anotherDummyNode1);
		dummyNode2.addChild(anotherDummyNode2);
		
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}

	
	@Test
	public void testTreeMulitpleCompositeSerialization2() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("particle[1]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("position");

		StateVariableNode anotherDummyNode1 = new StateVariableNode("x");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode2 = new StateVariableNode("y");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));
				
		rootNode.addChild(dummyNode1);		
		dummyNode2.addChild(anotherDummyNode1);
		dummyNode2.addChild(anotherDummyNode2);
		
		dummyNode1.addChild(dummyNode2);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"particle\":[{},{\"position\":{\"x\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"y\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeMulitpleCompositeSerialization3() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("bioPhys1");
		CompositeVariableNode dummyNode3 = new CompositeVariableNode("membraneProperties");
		CompositeVariableNode dummyNode4 = new CompositeVariableNode("naChans");
		CompositeVariableNode dummyNode5 = new CompositeVariableNode("na");
		CompositeVariableNode dummyNode6 = new CompositeVariableNode("m");
		CompositeVariableNode dummyNode7 = new CompositeVariableNode("h");

		StateVariableNode anotherDummyNode1 = new StateVariableNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode2 = new StateVariableNode("q");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));
				
		rootNode.addChild(dummyNode1);		
		dummyNode6.addChild(anotherDummyNode1);
		dummyNode7.addChild(anotherDummyNode2);
		
		dummyNode5.addChild(dummyNode7);
		dummyNode5.addChild(dummyNode6);
		
		dummyNode4.addChild(dummyNode5);
		dummyNode3.addChild(dummyNode4);
		dummyNode2.addChild(dummyNode3);
		dummyNode1.addChild(dummyNode2);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"na\":{\"h\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeMulitpleCompositeSerialization4() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("bioPhys1");
		CompositeVariableNode dummyNode3 = new CompositeVariableNode("membraneProperties");
		CompositeVariableNode dummyNode4 = new CompositeVariableNode("naChans");
		CompositeVariableNode dummyNode5 = new CompositeVariableNode("na");
		CompositeVariableNode dummyNode6 = new CompositeVariableNode("m");

		StateVariableNode anotherDummyNode1 = new StateVariableNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode2 = new StateVariableNode("gDensity");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));
				
		rootNode.addChild(dummyNode1);		
		dummyNode6.addChild(anotherDummyNode1);
		dummyNode5.addChild(anotherDummyNode2);

		dummyNode5.addChild(dummyNode6);
		
		dummyNode4.addChild(dummyNode5);
		dummyNode3.addChild(dummyNode4);
		dummyNode2.addChild(dummyNode3);
		dummyNode1.addChild(dummyNode2);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"na\":{\"gDensity\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeMulitpleCompositeSerialization5() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("bioPhys1");
		CompositeVariableNode dummyNode3 = new CompositeVariableNode("membraneProperties");
		CompositeVariableNode dummyNode4 = new CompositeVariableNode("naChans");
		CompositeVariableNode dummyNode5 = new CompositeVariableNode("na");
		CompositeVariableNode dummyNode6 = new CompositeVariableNode("m");
		CompositeVariableNode dummyNode7 = new CompositeVariableNode("kChans");
		CompositeVariableNode dummyNode8 = new CompositeVariableNode("k");
		CompositeVariableNode dummyNode9 = new CompositeVariableNode("n");
		
		StateVariableNode anotherDummyNode1 = new StateVariableNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode2 = new StateVariableNode("q");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode3 = new StateVariableNode("gDensity");
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(100d));
				
		rootNode.addChild(dummyNode1);		
		dummyNode6.addChild(anotherDummyNode1);
		dummyNode5.addChild(anotherDummyNode3);
		dummyNode9.addChild(anotherDummyNode2);
		
		dummyNode8.addChild(dummyNode9);
		dummyNode7.addChild(dummyNode8);
		
		dummyNode3.addChild(dummyNode7);
		dummyNode5.addChild(dummyNode6);
		
		dummyNode4.addChild(dummyNode5);
		dummyNode3.addChild(dummyNode4);
		dummyNode2.addChild(dummyNode3);
		dummyNode1.addChild(dummyNode2);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"kChans\":{\"k\":{\"n\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"naChans\":{\"na\":{\"gDensity\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeMulitpleCompositeSerialization6() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("bioPhys1");
		CompositeVariableNode dummyNode3 = new CompositeVariableNode("membraneProperties");
		CompositeVariableNode dummyNode4 = new CompositeVariableNode("naChans");
		CompositeVariableNode dummyNode5 = new CompositeVariableNode("na");
		CompositeVariableNode dummyNode6 = new CompositeVariableNode("m");
		CompositeVariableNode dummyNode7 = new CompositeVariableNode("kChans");
		CompositeVariableNode dummyNode8 = new CompositeVariableNode("k");
		CompositeVariableNode dummyNode9 = new CompositeVariableNode("n");
		
		StateVariableNode anotherDummyNode1 = new StateVariableNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode2 = new StateVariableNode("q");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode3 = new StateVariableNode("gDensity");
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode4 = new StateVariableNode("gDensity");
		anotherDummyNode4.addValue(ValuesFactory.getDoubleValue(40d));
		anotherDummyNode4.addValue(ValuesFactory.getDoubleValue(100d));
		
		rootNode.addChild(dummyNode1);		
		dummyNode6.addChild(anotherDummyNode1);
		dummyNode7.addChild(anotherDummyNode4);
		dummyNode4.addChild(anotherDummyNode3);
		dummyNode9.addChild(anotherDummyNode2);
		
		dummyNode8.addChild(dummyNode9);
		dummyNode7.addChild(dummyNode8);
		
		dummyNode3.addChild(dummyNode7);
		dummyNode5.addChild(dummyNode6);
		
		dummyNode4.addChild(dummyNode5);
		dummyNode3.addChild(dummyNode4);
		dummyNode2.addChild(dummyNode3);
		dummyNode1.addChild(dummyNode2);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"kChans\":{\"gDensity\":{\"value\":40.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"k\":{\"n\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"naChans\":{\"gDensity\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}

	@Test
	public void testTreeMulitpleCompositeSerialization7() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("bioPhys1");
		CompositeVariableNode dummyNode3 = new CompositeVariableNode("membraneProperties");
		CompositeVariableNode dummyNode4 = new CompositeVariableNode("naChans");
		CompositeVariableNode dummyNode5 = new CompositeVariableNode("na");
		CompositeVariableNode dummyNode6 = new CompositeVariableNode("m");

		StateVariableNode anotherDummyNode1 = new StateVariableNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));

		StateVariableNode anotherDummyNode2 = new StateVariableNode("v");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));

		StateVariableNode anotherDummyNode3 = new StateVariableNode("spiking");
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(100d));

		StateVariableNode anotherDummyNode4 = new StateVariableNode("gDensity");
		anotherDummyNode4.addValue(ValuesFactory.getDoubleValue(40d));
		anotherDummyNode4.addValue(ValuesFactory.getDoubleValue(100d));

		rootNode.addChild(dummyNode1);		
		dummyNode1.addChild(anotherDummyNode2);
		dummyNode1.addChild(anotherDummyNode3);
		dummyNode6.addChild(anotherDummyNode1);

		dummyNode4.addChild(anotherDummyNode4);

		dummyNode5.addChild(dummyNode6);
		dummyNode4.addChild(dummyNode5);
		dummyNode3.addChild(dummyNode4);
		dummyNode2.addChild(dummyNode3);
		dummyNode1.addChild(dummyNode2);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"spiking\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"gDensity\":{\"value\":40.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeMulitpleCompositeSerialization8() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("bioPhys1");
		CompositeVariableNode dummyNode3 = new CompositeVariableNode("membraneProperties");
		CompositeVariableNode dummyNode4 = new CompositeVariableNode("naChans");
		CompositeVariableNode dummyNode5 = new CompositeVariableNode("na");
		CompositeVariableNode dummyNode6 = new CompositeVariableNode("m");

		StateVariableNode anotherDummyNode1 = new StateVariableNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));

		StateVariableNode anotherDummyNode2 = new StateVariableNode("v");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));

		StateVariableNode anotherDummyNode3 = new StateVariableNode("spiking");
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(100d));

		StateVariableNode anotherDummyNode4 = new StateVariableNode("gDensity");
		anotherDummyNode4.addValue(ValuesFactory.getDoubleValue(40d));
		anotherDummyNode4.addValue(ValuesFactory.getDoubleValue(100d));

		rootNode.addChild(dummyNode1);		
		dummyNode6.addChild(anotherDummyNode1);

		dummyNode4.addChild(anotherDummyNode4);

		dummyNode5.addChild(dummyNode6);
		dummyNode4.addChild(dummyNode5);
		dummyNode3.addChild(dummyNode4);
		dummyNode2.addChild(dummyNode3);
		dummyNode1.addChild(dummyNode2);
		
		dummyNode1.addChild(anotherDummyNode2);
		dummyNode1.addChild(anotherDummyNode3);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"gDensity\":{\"value\":40.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"spiking\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeMulitpleCompositeSerialization9() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("particle[1]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("particle[2]");
		CompositeVariableNode dummyNode3 = new CompositeVariableNode("position");
		CompositeVariableNode dummyNode4 = new CompositeVariableNode("position");

		StateVariableNode anotherDummyNode1 = new StateVariableNode("x");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		StateVariableNode anotherDummyNode2 = new StateVariableNode("y");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));
				
		rootNode.addChild(dummyNode1);
		rootNode.addChild(dummyNode2);
		
		dummyNode3.addChild(anotherDummyNode1);
		dummyNode4.addChild(anotherDummyNode2);
		
		dummyNode1.addChild(dummyNode3);
		dummyNode2.addChild(dummyNode4);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"particle\":[{},{\"position\":{\"x\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},{\"position\":{\"y\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	/**
	 * Skeleton tree test
	 */
	@Test
	public void testSkeletonTree() {
		EntityNode entity_A = new EntityNode("Entity_A");
		
		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectTreeNode model = new AspectTreeNode("model");

		AspectTreeNode visualization = new AspectTreeNode("visualization");

		AspectTreeNode simulation = new AspectTreeNode("simulation");

		entity_A.addChild(aspect_A);
		aspect_A.addChild(model);
		aspect_A.addChild(visualization);
		aspect_A.addChild(simulation);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		entity_A.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(serialized);
		String prettyJsonString = gson.toJson(je);
		
		System.out.println(prettyJsonString);
		
		Assert.assertEquals("{\"Entity_A\":{\"Aspect_A\":{\"model\":{\"_metaType\":\"AspectTreeNode\"},\"visualization\":{\"_metaType\":\"AspectTreeNode\"},\"simulation\":{\"_metaType\":\"AspectTreeNode\"},\"_metaType\":\"AspectNode\"},\"_metaType\":\"EntityNode\"}}", serialized);
	}
	
	/**
	 * ReCreate Sample Tree below
	 * 
	 *  RuntimeTree -> RuntimeTreeRoot -> ACompositeNode
		  Entity1 -> EntityNode -> ACompositeNode
	    	AspectA -> AspectNode -> ACompositeNode
	        	modelTree -> AspectTreeNode -> ACompositeNode
	            	BiophysicalProperties -> CompositeVariableNode -> ACompositeNode
	                	a -> ParameterNode -> ASimpleStateNode
	                	b -> ParameterNode -> ASimpleStateNode
	                	c -> StateVariableNode -> ASimpleStateNode
	                	text -> TextMetadataNode -> AMetaDataNode
	        	visualisationTree -> AspectTreeNode -> ACompositeNode
	            		Sphere1 -> Sphere -> AVisualObject
	            		Cylinder1 -> Cylinder -> AVisualObject
	            		Cylinder2
	            	…
	            		CylinderN
	        	simulationTree -> AspectTree -> ACompositeNode
	            	hhpop -> CompositeVariableNode -> ACompositeNode
	                	0  -> CompositeVariableNode -> ACompositeNode
	                    	v -> StateVariableNode -> SimpleStateNode
	                    	a -> ParameterNode -> ASimpleStateNode
	 */
	@Test
	public void testRefactorSampleTree() {
		
		RuntimeTreeRoot runtimeTree = new RuntimeTreeRoot("RuntimeTree");
		
		EntityNode entity1 = new EntityNode("Entity1");
		
		AspectNode aspectA = new AspectNode("AspectA");

		AspectTreeNode model = new AspectTreeNode("model");

		CompositeVariableNode biophysicalProperties = new CompositeVariableNode("BiophysicalProperties");
		
		ParameterNode a = new ParameterNode("a");
		ParameterNode b = new ParameterNode("b");
		ParameterNode c = new ParameterNode("c");
		
		TextMetadataNode text = new TextMetadataNode("text");
		
		AspectTreeNode visualization = new AspectTreeNode("visualization");

		SphereNode sphere = new SphereNode();
		sphere.setName("sphere");
		Point p = new Point();
		p.setX(new Double(3.3));
		p.setY(new Double(4));
		p.setZ(new Double(-1.444));
		sphere.setPosition(p);
		
		CylinderNode cylinder = new CylinderNode();
		cylinder.setName("cylinder");
		Point p2 = new Point();
		p2.setX(new Double(6.3));
		p2.setY(new Double(8));
		p2.setZ(new Double(-3.999));
		cylinder.setPosition(p2);
		
		AspectTreeNode simulation = new AspectTreeNode("simulation");
		
		CompositeVariableNode hhpop = new CompositeVariableNode("hhpop[0]");
		
		StateVariableNode v = new StateVariableNode("v");
		v.addValue(ValuesFactory.getDoubleValue(20d));
		v.addValue(ValuesFactory.getDoubleValue(100d));
		
		ParameterNode a1 = new ParameterNode("a");
		
		runtimeTree.addChild(entity1);
		entity1.addChild(aspectA);
		aspectA.addChild(model);
		model.addChild(biophysicalProperties);
		biophysicalProperties.addChild(a);
		biophysicalProperties.addChild(b);
		biophysicalProperties.addChild(c);
		biophysicalProperties.addChild(text); 
		
		aspectA.addChild(visualization);
		visualization.addChild(sphere);
		visualization.addChild(cylinder);
		
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

		Assert.assertEquals("{\"RuntimeTree\":{\"Entity1\":{\"AspectA\":{\"model\":{\"BiophysicalProperties\":{\"a\":{\"_metaType\":\"ParameterNode\"},\"b\":{\"_metaType\":\"ParameterNode\"},\"c\":{\"_metaType\":\"ParameterNode\"},\"text\":{\"_metaType\":\"TextMetadataNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"AspectTreeNode\"},\"visualization\":{\"sphere\":{\"position\":{\"x\":3.3,\"y\":4.0,\"z\":-1.444},\"_metaType\":\"SphereNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"_metaType\":\"CylinderNode\"},\"_metaType\":\"AspectTreeNode\"},\"simulation\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"StateVariableNode\"},\"a\":{\"_metaType\":\"ParameterNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"AspectTreeNode\"},\"_metaType\":\"AspectNode\"},\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}", serialized);
	}
}