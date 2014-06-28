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

import org.geppetto.core.model.state.CompositeVariableNode;
import org.geppetto.core.model.state.StateVariableNode;
import org.geppetto.core.model.state.visitors.SerializeTreeVisitor;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.ValuesFactory;
import org.junit.Test;

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
		Assert.assertEquals("{\"TIME\":{\"step\":{\"value\":0.05,\"unit\":\"ms\",\"scale\":null},\"time\":{\"value\":0.04,\"unit\":\"ms\",\"scale\":null}}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"dummyFloat\":{\"value\":50.0,\"unit\":null,\"scale\":null},\"dummyDouble\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"dummyFloat\":{\"value\":50.0,\"unit\":\"V\",\"scale\":\"1.E3\"},\"dummyDouble\":{\"value\":50.0,\"unit\":\"mV\",\"scale\":\"1.E3\"}}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null},\"spiking\":{\"value\":55.0,\"unit\":null,\"scale\":null}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"c302\":{\"electrical\":{\"ADAL\":{\"0\":{\"generic_iaf_cell\":{\"v\":{\"value\":-0.05430606873466336,\"unit\":null,\"scale\":null}}}},\"ADAR\":{\"0\":{\"generic_iaf_cell\":{\"v\":{\"value\":-0.055433782139120126,\"unit\":null,\"scale\":null}}}}}}}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null}},{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{},{},{},{},{},{},{},{},{},{},{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null}},{},{},{},{},{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null}},{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"particle\":[{},{\"position\":{\"x\":{\"value\":20.0,\"unit\":null,\"scale\":null},\"y\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"na\":{\"h\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null}},\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}}}}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"na\":{\"gDensity\":{\"value\":20.0,\"unit\":null,\"scale\":null},\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}}}}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"kChans\":{\"k\":{\"n\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}},\"naChans\":{\"na\":{\"gDensity\":{\"value\":20.0,\"unit\":null,\"scale\":null},\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}}}}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"kChans\":{\"gDensity\":{\"value\":40.0,\"unit\":null,\"scale\":null},\"k\":{\"n\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}},\"naChans\":{\"gDensity\":{\"value\":20.0,\"unit\":null,\"scale\":null},\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}}}}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null},\"spiking\":{\"value\":20.0,\"unit\":null,\"scale\":null},\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"gDensity\":{\"value\":40.0,\"unit\":null,\"scale\":null},\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}}}}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"bioPhys1\":{\"membraneProperties\":{\"naChans\":{\"gDensity\":{\"value\":40.0,\"unit\":null,\"scale\":null},\"na\":{\"m\":{\"q\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}}}},\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null},\"spiking\":{\"value\":20.0,\"unit\":null,\"scale\":null}}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"particle\":[{},{\"position\":{\"x\":{\"value\":20.0,\"unit\":null,\"scale\":null}}},{\"position\":{\"y\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}]}}", serialized);
	}
}