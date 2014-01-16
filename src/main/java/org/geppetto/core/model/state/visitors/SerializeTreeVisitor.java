package org.geppetto.core.model.state.visitors;

import java.util.HashMap;
import java.util.Map;

import org.geppetto.core.model.state.AStateNode;
import org.geppetto.core.model.state.CompositeStateNode;
import org.geppetto.core.model.state.SimpleStateNode;

public class SerializeTreeVisitor extends DefaultStateVisitor
{

	private StringBuilder _serialized = new StringBuilder();

	private Map<AStateNode, Map<String, Integer>> _arraysLastIndexMap = new HashMap<AStateNode, Map<String, Integer>>();

	public SerializeTreeVisitor()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean inCompositeStateNode(CompositeStateNode node)
	{

		String name = node.getBaseName();
		if(node.isArray() && node.getParent() != null)
		{
			int index = node.getIndex();
			Map<String, Integer> indexMap = _arraysLastIndexMap.get(node.getParent());

			if(!indexMap.containsKey(name))
			{
				// if the object is not in the map we haven't found this array before
				_serialized.append("{\"" + name + "\":[");
				indexMap.put(name, -1);
			}
			if(indexMap.containsKey(name) && indexMap.get(name) > index)
			{
				throw new RuntimeException("The tree is not ordered, found surpassed index");
			}
			for(int i = indexMap.get(name); i < index - 1; i++)
			{
				// we fill in the gaps with empty objects so that we generate a valid JSON array
				_serialized.append("{},");
			}
			_serialized.append("{");
			indexMap.put(name, index);
		}
		else
		{
			_serialized.append("{\"" + name + "\":");
			if(node.getChildren().size() > 1)
			{
				if(((AStateNode)node.getChildren().get(0)).isArray()){
					_serialized.append("[");
				}
				else{
					_serialized.append("[{");
				}
			}

		}

		_arraysLastIndexMap.put(node, new HashMap<String, Integer>());
		return super.inCompositeStateNode(node);
	}

	@Override
	public boolean outCompositeStateNode(CompositeStateNode node)
	{

		_serialized.deleteCharAt(_serialized.lastIndexOf(","));

		if(node.isArray())
		{
			AStateNode sibling = node.nextSibling();
			if(sibling == null || !(sibling instanceof CompositeStateNode) || !(((CompositeStateNode) sibling).getBaseName().equals(node.getBaseName())))
			{
				_serialized.append("}]},");
				return super.outCompositeStateNode(node);
			}
			else
			{
				_serialized.append("},");
			}
		}
		else
		{
			if(node.getChildren().size() > 1)
			{
				if(((AStateNode)node.getChildren().get(0)).isArray()){
					_serialized.append("]");
				}
				else{
					_serialized.append("}]");
				}				
			}

			_serialized.append("},");
		}

		return super.outCompositeStateNode(node);
	}

	@Override
	public boolean visitSimpleStateNode(SimpleStateNode node)
	{
		_serialized.append("\""  + node.getName() + "\":" + node.consumeFirstValue() + ",");

		return super.visitSimpleStateNode(node);
	}

	public String getSerializedTree()
	{
		if(_serialized.charAt(_serialized.length() - 1) == ',') _serialized.deleteCharAt(_serialized.lastIndexOf(","));
		return _serialized.toString();
	}

}
