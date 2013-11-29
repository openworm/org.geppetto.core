package org.geppetto.core.model.state.visitors;

import org.geppetto.core.model.state.CompositeStateNode;
import org.geppetto.core.model.state.SimpleStateNode;

public class SerializeTreeVisitor extends DefaultStateVisitor
{

	private StringBuilder _serialized = new StringBuilder();

	@Override
	public boolean inCompositeStateNode(CompositeStateNode node)
	{

		_serialized.append("{\"" + node.getName() + "\":");

		if(node.getChildren().size() > 1)
		{
			_serialized.append("[");
		}

		return super.inCompositeStateNode(node);
	}

	@Override
	public boolean outCompositeStateNode(CompositeStateNode node)
	{

		_serialized.deleteCharAt(_serialized.lastIndexOf(","));
		if(node.getChildren().size() > 1)
		{
			_serialized.append("]");
		}

		_serialized.append("},");

		return super.outCompositeStateNode(node);
	}

	@Override
	public boolean visitSimpleStateNode(SimpleStateNode node)
	{
		_serialized.append("{ \"name\":\"" + node.getName() + "\", \"value\":\"" + node.consumeFirstValue() + "\" },");

		return super.visitSimpleStateNode(node);
	}

	public String getSerializedTree()
	{
		if(_serialized.charAt(_serialized.length() - 1) == ',') _serialized.deleteCharAt(_serialized.lastIndexOf(","));
		return _serialized.toString();
	}

}
