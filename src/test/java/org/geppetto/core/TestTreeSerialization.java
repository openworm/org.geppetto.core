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
import org.geppetto.core.model.values.ValuesFactory;
import org.junit.Test;

public class TestTreeSerialization {

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
		Assert.assertEquals("{\"WATCH_TREE\":[{\"dummyFloat\":50.0,\"dummyDouble\":20.0}]}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop\":[{\"v\":20.0,\"spiking\":55.0}]}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":[{\"hhpop\":[{\"v\":20.0},{\"v\":55.0}]}]}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":[{\"hhpop\":[{},{},{},{},{},{},{},{},{},{},{\"v\":20.0},{},{},{},{},{\"v\":55.0}]}]}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":[{\"hhpop\":[{\"v\":20.0},{\"v\":20.0}]}]}", serialized);
	}

}
