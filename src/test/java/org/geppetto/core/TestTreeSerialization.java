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

import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.runtime.CompositeVariableNode;
import org.geppetto.core.model.runtime.CylinderNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.ParameterNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.runtime.TextMetadataNode;
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
		Assert.assertEquals("{\"TIME\":{\"step\":{\"value\":0.05,\"unit\":\"ms\",\"scale\":null,\"_metaType\":\"VariableNode\"},\"time\":{\"value\":0.04,\"unit\":\"ms\",\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeSerialization() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"dummyFloat\":{\"value\":50.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"dummyDouble\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}

	@Test
	public void testTreeWithUnits() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"dummyFloat\":{\"value\":50.0,\"unit\":\"V\",\"scale\":\"1.E3\",\"_metaType\":\"VariableNode\"},\"dummyDouble\":{\"value\":50.0,\"unit\":\"mV\",\"scale\":\"1.E3\",\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeNestedSerialization2() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode0 = new CompositeVariableNode("hhpop[0]");

		VariableNode anotherDummyNode0 = new VariableNode("v");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode0.addPhysicalQuantity(quantity);
		anotherDummyNode0.addPhysicalQuantity(quantity2);
		
		rootNode.addChild(dummyNode0);
		dummyNode0.addChild(anotherDummyNode0);
		
		VariableNode anotherDummyNode1 = new VariableNode("spiking");
		
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(55d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(65d));

		anotherDummyNode1.addPhysicalQuantity(quantity3);
		anotherDummyNode1.addPhysicalQuantity(quantity4);
		
		dummyNode0.addChild(anotherDummyNode1);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"spiking\":{\"value\":55.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
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

		VariableNode v = new VariableNode("v");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(-0.05430606873466336d));
		v.addPhysicalQuantity(quantity);
		
		rootNode.addChild(c302);
		c302.addChild(electrical);
		electrical.addChild(adal);
		adal.addChild(adal0);
		adal0.addChild(adalgeneric_iaf_cell);
		adalgeneric_iaf_cell.addChild(v);
		
		VariableNode adarV = new VariableNode("v");
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(-0.055433782139120126d));
		
		adarV.addPhysicalQuantity(quantity2);
		
		electrical.addChild(adar);
		adar.addChild(adar0);
		adar0.addChild(adargeneric_iaf_cell);
		adargeneric_iaf_cell.addChild(adarV);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"c302\":{\"electrical\":{\"ADAL\":{\"0\":{\"generic_iaf_cell\":{\"v\":{\"value\":-0.05430606873466336,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"ADAR\":{\"0\":{\"generic_iaf_cell\":{\"v\":{\"value\":-0.055433782139120126,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeNestedSerialization() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode0 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[1]");

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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeNestedSerializationWithGaps() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode0 = new CompositeVariableNode("hhpop[10]");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[15]");

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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{},{},{},{},{},{},{},{},{},{},{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},{},{},{},{},{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	
	@Test
	public void testTreeMultpleCompositeSerialization() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("hhpop[0]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("hhpop[1]");

		VariableNode anotherDummyNode1 = new VariableNode("v");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity);
		anotherDummyNode1.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode2 = new VariableNode("v");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode2.addPhysicalQuantity(quantity3);
		anotherDummyNode2.addPhysicalQuantity(quantity4);
		
		rootNode.addChild(dummyNode1);
		rootNode.addChild(dummyNode2);
		dummyNode1.addChild(anotherDummyNode1);
		dummyNode2.addChild(anotherDummyNode2);
		
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}

	
	@Test
	public void testTreeMulitpleCompositeSerialization2() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("particle[1]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("position");

		VariableNode anotherDummyNode1 = new VariableNode("x");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity);
		anotherDummyNode1.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode2 = new VariableNode("y");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode2.addPhysicalQuantity(quantity3);
		anotherDummyNode2.addPhysicalQuantity(quantity4);
				
		rootNode.addChild(dummyNode1);		
		dummyNode2.addChild(anotherDummyNode1);
		dummyNode2.addChild(anotherDummyNode2);
		
		dummyNode1.addChild(dummyNode2);

		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"particle\":[{},{\"position\":{\"x\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"y\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
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

		VariableNode anotherDummyNode1 = new VariableNode("q");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity);
		anotherDummyNode1.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode2 = new VariableNode("q");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode2.addPhysicalQuantity(quantity3);
		anotherDummyNode2.addPhysicalQuantity(quantity4);
				
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"na\":{\"h\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
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

		VariableNode anotherDummyNode1 = new VariableNode("q");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity);
		anotherDummyNode1.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode2 = new VariableNode("gDensity");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode2.addPhysicalQuantity(quantity3);
		anotherDummyNode2.addPhysicalQuantity(quantity4);
				
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"na\":{\"gDensity\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
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
		
		VariableNode anotherDummyNode1 = new VariableNode("q");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity);
		anotherDummyNode1.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode2 = new VariableNode("q");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode2.addPhysicalQuantity(quantity3);
		anotherDummyNode2.addPhysicalQuantity(quantity4);
		
		VariableNode anotherDummyNode3 = new VariableNode("gDensity");
		PhysicalQuantity quantity5 = new PhysicalQuantity();
		quantity5.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity6 = new PhysicalQuantity();
		quantity6.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode3.addPhysicalQuantity(quantity5);
		anotherDummyNode3.addPhysicalQuantity(quantity6);
				
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"kChans\":{\"k\":{\"n\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"naChans\":{\"na\":{\"gDensity\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
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
		
		VariableNode anotherDummyNode1 = new VariableNode("q");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity);
		anotherDummyNode1.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode2 = new VariableNode("q");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode2.addPhysicalQuantity(quantity3);
		anotherDummyNode2.addPhysicalQuantity(quantity4);
		
		VariableNode anotherDummyNode3 = new VariableNode("gDensity");
		PhysicalQuantity quantity5 = new PhysicalQuantity();
		quantity5.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity6 = new PhysicalQuantity();
		quantity6.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode3.addPhysicalQuantity(quantity5);
		anotherDummyNode3.addPhysicalQuantity(quantity6);
		
		VariableNode anotherDummyNode4 = new VariableNode("gDensity");
		PhysicalQuantity quantity7 = new PhysicalQuantity();
		quantity7.setValue(ValuesFactory.getDoubleValue(40d));
		
		PhysicalQuantity quantity8 = new PhysicalQuantity();
		quantity8.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode4.addPhysicalQuantity(quantity7);
		anotherDummyNode4.addPhysicalQuantity(quantity8);
		
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"kChans\":{\"gDensity\":{\"value\":40.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"k\":{\"n\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"naChans\":{\"gDensity\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
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

		VariableNode anotherDummyNode1 = new VariableNode("q");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity);
		anotherDummyNode1.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode2 = new VariableNode("v");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode2.addPhysicalQuantity(quantity3);
		anotherDummyNode2.addPhysicalQuantity(quantity4);
		
		VariableNode anotherDummyNode3 = new VariableNode("spiking");
		PhysicalQuantity quantity5 = new PhysicalQuantity();
		quantity5.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity6 = new PhysicalQuantity();
		quantity6.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode3.addPhysicalQuantity(quantity5);
		anotherDummyNode3.addPhysicalQuantity(quantity6);
		
		VariableNode anotherDummyNode4 = new VariableNode("gDensity");
		PhysicalQuantity quantity7 = new PhysicalQuantity();
		quantity7.setValue(ValuesFactory.getDoubleValue(40d));
		
		PhysicalQuantity quantity8 = new PhysicalQuantity();
		quantity8.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode4.addPhysicalQuantity(quantity7);
		anotherDummyNode4.addPhysicalQuantity(quantity8);

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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"spiking\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"gDensity\":{\"value\":40.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
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

		VariableNode anotherDummyNode1 = new VariableNode("q");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity);
		anotherDummyNode1.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode2 = new VariableNode("v");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode2.addPhysicalQuantity(quantity3);
		anotherDummyNode2.addPhysicalQuantity(quantity4);
		
		VariableNode anotherDummyNode3 = new VariableNode("spiking");
		PhysicalQuantity quantity5 = new PhysicalQuantity();
		quantity5.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity6 = new PhysicalQuantity();
		quantity6.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode3.addPhysicalQuantity(quantity5);
		anotherDummyNode3.addPhysicalQuantity(quantity6);
		
		VariableNode anotherDummyNode4 = new VariableNode("gDensity");
		PhysicalQuantity quantity7 = new PhysicalQuantity();
		quantity7.setValue(ValuesFactory.getDoubleValue(40d));
		
		PhysicalQuantity quantity8 = new PhysicalQuantity();
		quantity8.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode4.addPhysicalQuantity(quantity7);
		anotherDummyNode4.addPhysicalQuantity(quantity8);

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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"gDensity\":{\"value\":40.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"spiking\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	@Test
	public void testTreeMulitpleCompositeSerialization9() {
		CompositeVariableNode rootNode = new CompositeVariableNode("WATCH_TREE");
		CompositeVariableNode dummyNode1 = new CompositeVariableNode("particle[1]");
		CompositeVariableNode dummyNode2 = new CompositeVariableNode("particle[2]");
		CompositeVariableNode dummyNode3 = new CompositeVariableNode("position");
		CompositeVariableNode dummyNode4 = new CompositeVariableNode("position");

		VariableNode anotherDummyNode1 = new VariableNode("x");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode1.addPhysicalQuantity(quantity);
		anotherDummyNode1.addPhysicalQuantity(quantity2);
		
		VariableNode anotherDummyNode2 = new VariableNode("y");
		PhysicalQuantity quantity3 = new PhysicalQuantity();
		quantity3.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity4 = new PhysicalQuantity();
		quantity4.setValue(ValuesFactory.getDoubleValue(100d));
		
		anotherDummyNode2.addPhysicalQuantity(quantity3);
		anotherDummyNode2.addPhysicalQuantity(quantity4);
				
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"particle\":[{},{\"position\":{\"x\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"},{\"position\":{\"y\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"_metaType\":\"CompositeVariableNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"_metaType\":\"CompositeVariableNode\"}}", serialized);
	}
	
	/**
	 * Skeleton tree test
	 * @
	 */
	@Test
	public void testSkeletonTree() {
		EntityNode entity_A = new EntityNode("Entity_A");
		
		AspectNode aspect_A = new AspectNode("Aspect_A");

		AspectSubTreeNode model = new AspectSubTreeNode(AspectTreeType.MODEL_TREE);

		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);

		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.WATCH_TREE);

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
		
		Assert.assertEquals("{\"Entity_A\":{\"Aspect_A\":{\"ModelTree\":{\"type\":\"ModelTree\",\"_metaType\":\"AspectSubTreeNode\"},\"VisualizationTree\":{\"type\":\"VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"SimulationTree\":{\"type\":\"SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"_metaType\":\"AspectNode\"},\"_metaType\":\"EntityNode\"}}", serialized);
	}
	
	/**
	 * ReCreate Sample Tree below
	 * 
	 *  RuntimeTree -> RuntimeTreeRoot -> ACompositeNode
		  Entity1 -> EntityNode -> ACompositeNode
	    	AspectA -> AspectNode -> ACompositeNode
	        	modelTree -> AspectSubTreeNode -> ACompositeNode
	            	BiophysicalProperties -> CompositeVariableNode -> ACompositeNode
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
	            	hhpop -> CompositeVariableNode -> ACompositeNode
	                	0  -> CompositeVariableNode -> ACompositeNode
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

		CompositeVariableNode biophysicalProperties = new CompositeVariableNode("BiophysicalProperties");
		
		ParameterNode a = new ParameterNode("a");
		ParameterNode b = new ParameterNode("b");
		ParameterNode c = new ParameterNode("c");
		
		TextMetadataNode text = new TextMetadataNode("text");
		
		AspectSubTreeNode visualization = new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);

		SphereNode sphere = new SphereNode("sphere");
		Point p = new Point();
		p.setX(new Double(3.3));
		p.setY(new Double(4));
		p.setZ(new Double(-1.444));
		sphere.setPosition(p);
		
		CylinderNode cylinder = new CylinderNode("cylinder");
		Point p2 = new Point();
		p2.setX(new Double(6.3));
		p2.setY(new Double(8));
		p2.setZ(new Double(-3.999));
		cylinder.setPosition(p2);
		
		AspectSubTreeNode simulation = new AspectSubTreeNode(AspectTreeType.WATCH_TREE);
		
		
		CompositeVariableNode hhpop = new CompositeVariableNode("hhpop[0]");
		
		VariableNode v = new VariableNode("v");
		PhysicalQuantity quantity = new PhysicalQuantity();
		quantity.setValue(ValuesFactory.getDoubleValue(20d));
		
		PhysicalQuantity quantity2 = new PhysicalQuantity();
		quantity2.setValue(ValuesFactory.getDoubleValue(100d));
		
		v.addPhysicalQuantity(quantity);
		v.addPhysicalQuantity(quantity2);
		
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

		Assert.assertEquals("{\"RuntimeTree\":{\"Entity1\":{\"AspectA\":{\"ModelTree\":{\"BiophysicalProperties\":{\"a\":{\"_metaType\":\"ParameterNode\"},\"b\":{\"_metaType\":\"ParameterNode\"},\"c\":{\"_metaType\":\"ParameterNode\"},\"text\":{\"_metaType\":\"TextMetadataNode\"},\"_metaType\":\"CompositeVariableNode\"},\"type\":\"ModelTree\",\"_metaType\":\"AspectSubTreeNode\"},\"VisualizationTree\":{\"sphere\":{\"position\":{\"x\":3.3,\"y\":4.0,\"z\":-1.444},\"_metaType\":\"SphereNode\"},\"cylinder\":{\"position\":{\"x\":6.3,\"y\":8.0,\"z\":-3.999},\"_metaType\":\"CylinderNode\"},\"type\":\"VisualizationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"SimulationTree\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null,\"_metaType\":\"VariableNode\"},\"a\":{\"_metaType\":\"ParameterNode\"},\"_metaType\":\"CompositeVariableNode\"}],\"type\":\"SimulationTree\",\"_metaType\":\"AspectSubTreeNode\"},\"id\":\"12\",\"simulator\":\"test\",\"modelInterpreter\":\"Test Model interpreter\",\"_metaType\":\"AspectNode\"},\"_metaType\":\"EntityNode\"},\"_metaType\":\"RuntimeTreeRoot\"}}", serialized);
	}
}