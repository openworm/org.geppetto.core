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
		CompositeStateNode rootNode = new CompositeStateNode("variable_watch");
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
		Assert.assertEquals("{\"variable_watch\":[{ \"name\":\"dummyFloat\", \"value\":\"50.0\" },{ \"name\":\"dummyDouble\", \"value\":\"20.0\" }]}", serialized);
	}
	
	@Test
	public void testTreeNestedSerialization() {
		CompositeStateNode rootNode = new CompositeStateNode("WATCH_TREE");
		CompositeStateNode dummyNode = new CompositeStateNode("hhpop[0]");

		SimpleStateNode anotherDummyNode = new SimpleStateNode("v");
		anotherDummyNode.addValue(ValuesFactory.getDoubleValue(20d));
		anotherDummyNode.addValue(ValuesFactory.getDoubleValue(100d));
		rootNode.addChild(dummyNode);
		dummyNode.addChild(anotherDummyNode);
		
		
		SerializeTreeVisitor visitor = new SerializeTreeVisitor();
		rootNode.apply(visitor);
		String serialized = visitor.getSerializedTree();
		System.out.println(serialized);
		Assert.assertEquals("{\"WATCH_TREE\":{\"hhpop[0]\":{ \"name\":\"v\", \"value\":\"20.0\" }}}", serialized);
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
		Assert.assertEquals("{\"WATCH_TREE\":[{\"hhpop[0]\":{ \"name\":\"v\", \"value\":\"20.0\" }},{\"hhpop[1]\":{ \"name\":\"v\", \"value\":\"20.0\" }}]}", serialized);
	}

}
