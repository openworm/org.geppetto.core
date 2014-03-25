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

import org.geppetto.core.model.state.CompositeStateNode;
import org.geppetto.core.model.state.SimpleStateNode;
import org.geppetto.core.model.state.visitors.SerializeTreeVisitor;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.ValuesFactory;
import org.junit.Test;

public class TestTreeSerialization {

	@Test
	public void testTime() {
		CompositeStateNode rootNode = new CompositeStateNode("TIME");
		
		SimpleStateNode stepNode = new SimpleStateNode("step");
		stepNode.addValue(ValuesFactory.getDoubleValue(0.05));
		stepNode.setUnit("ms");
		
		SimpleStateNode dummyNode = new SimpleStateNode("time");
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		
		SimpleStateNode dummyNode = new SimpleStateNode("dummyFloat");
		dummyNode.addValue(ValuesFactory.getDoubleValue(50d));
		dummyNode.addValue(ValuesFactory.getDoubleValue(100d));
		SimpleStateNode anotherDummyNode = new SimpleStateNode("dummyDouble");
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		
		AValue val = ValuesFactory.getDoubleValue(50d);
		AValue val2 = ValuesFactory.getDoubleValue(100d);
		
		SimpleStateNode dummyNode = new SimpleStateNode("dummyFloat");
		dummyNode.addValue(val);
		dummyNode.addValue(val2);
		
		dummyNode.setUnit("V");
		dummyNode.setScalingFactor("1.E3");

		SimpleStateNode anotherDummyNode = new SimpleStateNode("dummyDouble");
		
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode0 = new CompositeStateNode("hhpop[0]");

		SimpleStateNode anotherDummyNode0 = new SimpleStateNode("v");
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(100d));
		rootNode.addChild(dummyNode0);
		dummyNode0.addChild(anotherDummyNode0);
		
		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("spiking");
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
	public void testTreeNestedSerialization() {
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode0 = new CompositeStateNode("hhpop[0]");
		CompositeStateNode dummyNode1 = new CompositeStateNode("hhpop[1]");

		SimpleStateNode anotherDummyNode0 = new SimpleStateNode("v");
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(100d));
		rootNode.addChild(dummyNode0);
		dummyNode0.addChild(anotherDummyNode0);
		
		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("v");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(55d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(65d));
		rootNode.addChild(dummyNode1);
		dummyNode1.addChild(anotherDummyNode1);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":[{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null}},{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null}}]}]}", serialized);
	}
	
	@Test
	public void testTreeNestedSerializationWithGaps() {
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode0 = new CompositeStateNode("hhpop[10]");
		CompositeStateNode dummyNode1 = new CompositeStateNode("hhpop[15]");

		SimpleStateNode anotherDummyNode0 = new SimpleStateNode("v");
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode0.addValue(ValuesFactory.getDoubleValue(100d));
		rootNode.addChild(dummyNode0);
		dummyNode0.addChild(anotherDummyNode0);
		
		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("v");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(55d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(65d));
		rootNode.addChild(dummyNode1);
		dummyNode1.addChild(anotherDummyNode1);
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":[{\"hhpop\":[{},{},{},{},{},{},{},{},{},{},{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null}},{},{},{},{},{\"v\":{\"value\":55.0,\"unit\":null,\"scale\":null}}]}]}", serialized);
	}
	
	
	@Test
	public void testTreeMultpleCompositeSerialization() {
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode1 = new CompositeStateNode("hhpop[0]");
		CompositeStateNode dummyNode2 = new CompositeStateNode("hhpop[1]");

		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("v");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode2 = new SimpleStateNode("v");
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
		Assert.assertEquals("{\"WATCH_TREE\":[{\"hhpop\":[{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null}},{\"v\":{\"value\":20.0,\"unit\":null,\"scale\":null}}]}]}", serialized);
	}

	
	@Test
	public void testTreeMulitpleCompositeSerialization2() {
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode1 = new CompositeStateNode("particle[1]");
		CompositeStateNode dummyNode2 = new CompositeStateNode("position");

		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("x");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode2 = new SimpleStateNode("y");
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode1 = new CompositeStateNode("hhpop[0]");
		CompositeStateNode dummyNode2 = new CompositeStateNode("bioPhys1");
		CompositeStateNode dummyNode3 = new CompositeStateNode("membraneProperties");
		CompositeStateNode dummyNode4 = new CompositeStateNode("naChans");
		CompositeStateNode dummyNode5 = new CompositeStateNode("na");
		CompositeStateNode dummyNode6 = new CompositeStateNode("m");
		CompositeStateNode dummyNode7 = new CompositeStateNode("h");

		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode2 = new SimpleStateNode("q");
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode1 = new CompositeStateNode("hhpop[0]");
		CompositeStateNode dummyNode2 = new CompositeStateNode("bioPhys1");
		CompositeStateNode dummyNode3 = new CompositeStateNode("membraneProperties");
		CompositeStateNode dummyNode4 = new CompositeStateNode("naChans");
		CompositeStateNode dummyNode5 = new CompositeStateNode("na");
		CompositeStateNode dummyNode6 = new CompositeStateNode("m");

		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode2 = new SimpleStateNode("gDensity");
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode1 = new CompositeStateNode("hhpop[0]");
		CompositeStateNode dummyNode2 = new CompositeStateNode("bioPhys1");
		CompositeStateNode dummyNode3 = new CompositeStateNode("membraneProperties");
		CompositeStateNode dummyNode4 = new CompositeStateNode("naChans");
		CompositeStateNode dummyNode5 = new CompositeStateNode("na");
		CompositeStateNode dummyNode6 = new CompositeStateNode("m");
		CompositeStateNode dummyNode7 = new CompositeStateNode("kChans");
		CompositeStateNode dummyNode8 = new CompositeStateNode("k");
		CompositeStateNode dummyNode9 = new CompositeStateNode("n");
		
		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode2 = new SimpleStateNode("q");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode3 = new SimpleStateNode("gDensity");
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode1 = new CompositeStateNode("hhpop[0]");
		CompositeStateNode dummyNode2 = new CompositeStateNode("bioPhys1");
		CompositeStateNode dummyNode3 = new CompositeStateNode("membraneProperties");
		CompositeStateNode dummyNode4 = new CompositeStateNode("naChans");
		CompositeStateNode dummyNode5 = new CompositeStateNode("na");
		CompositeStateNode dummyNode6 = new CompositeStateNode("m");
		CompositeStateNode dummyNode7 = new CompositeStateNode("kChans");
		CompositeStateNode dummyNode8 = new CompositeStateNode("k");
		CompositeStateNode dummyNode9 = new CompositeStateNode("n");
		
		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode2 = new SimpleStateNode("q");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode3 = new SimpleStateNode("gDensity");
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode4 = new SimpleStateNode("gDensity");
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode1 = new CompositeStateNode("hhpop[0]");
		CompositeStateNode dummyNode2 = new CompositeStateNode("bioPhys1");
		CompositeStateNode dummyNode3 = new CompositeStateNode("membraneProperties");
		CompositeStateNode dummyNode4 = new CompositeStateNode("naChans");
		CompositeStateNode dummyNode5 = new CompositeStateNode("na");
		CompositeStateNode dummyNode6 = new CompositeStateNode("m");

		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));

		SimpleStateNode anotherDummyNode2 = new SimpleStateNode("v");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));

		SimpleStateNode anotherDummyNode3 = new SimpleStateNode("spiking");
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(100d));

		SimpleStateNode anotherDummyNode4 = new SimpleStateNode("gDensity");
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode1 = new CompositeStateNode("hhpop[0]");
		CompositeStateNode dummyNode2 = new CompositeStateNode("bioPhys1");
		CompositeStateNode dummyNode3 = new CompositeStateNode("membraneProperties");
		CompositeStateNode dummyNode4 = new CompositeStateNode("naChans");
		CompositeStateNode dummyNode5 = new CompositeStateNode("na");
		CompositeStateNode dummyNode6 = new CompositeStateNode("m");

		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("q");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));

		SimpleStateNode anotherDummyNode2 = new SimpleStateNode("v");
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode2.addValue(ValuesFactory.getDoubleValue(100d));

		SimpleStateNode anotherDummyNode3 = new SimpleStateNode("spiking");
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode3.addValue(ValuesFactory.getDoubleValue(100d));

		SimpleStateNode anotherDummyNode4 = new SimpleStateNode("gDensity");
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
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode1 = new CompositeStateNode("particle[1]");
		CompositeStateNode dummyNode2 = new CompositeStateNode("particle[2]");
		CompositeStateNode dummyNode3 = new CompositeStateNode("position");
		CompositeStateNode dummyNode4 = new CompositeStateNode("position");

		SimpleStateNode anotherDummyNode1 = new SimpleStateNode("x");
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode1.addValue(ValuesFactory.getDoubleValue(100d));
		
		SimpleStateNode anotherDummyNode2 = new SimpleStateNode("y");
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
		Assert.assertEquals("{\"WATCH_TREE\":[{\"particle\":[{},{\"position\":{\"x\":{\"value\":20.0,\"unit\":null,\"scale\":null}}},{\"position\":{\"y\":{\"value\":20.0,\"unit\":null,\"scale\":null}}}]}]}", serialized);
	}
}