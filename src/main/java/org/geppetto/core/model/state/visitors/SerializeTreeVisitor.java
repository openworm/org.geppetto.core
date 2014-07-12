package org.geppetto.core.model.state.visitors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.ColladaNode;
import org.geppetto.core.model.runtime.CompositeVariableNode;
import org.geppetto.core.model.runtime.CylinderNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.ParameterNode;
import org.geppetto.core.model.runtime.ParticleNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.StateVariableNode;
import org.geppetto.core.model.runtime.TextMetadataNode;
import org.geppetto.core.model.runtime.URLMetadataNode;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.visualisation.model.Point;

public class SerializeTreeVisitor extends DefaultStateVisitor
{
	private StringBuilder _serialized = new StringBuilder();
	private Map<ANode, Map<String, Integer>> _arraysLastIndexMap = new HashMap<ANode, Map<String, Integer>>();
	public SerializeTreeVisitor(){
		super();
	}
	
	public void generalACompositeStateNodeIn(ACompositeNode node){
		String name = node.getBaseName();
		if(node.isArray() && node.getParent() != null){
			int index = node.getIndex();
			Map<String, Integer> indexMap = _arraysLastIndexMap.get(node.getParent());

			if(!indexMap.containsKey(name)){
				// if the object is not in the map we haven't found this array before
				_serialized.append("{\"" + name + "\":[");
				indexMap.put(name, -1);
			}
			if(indexMap.containsKey(name) && indexMap.get(name) > index){
				throw new RuntimeException("The tree is not ordered, found surpassed index");
			}
			for(int i = indexMap.get(name); i < index - 1; i++){
				// we fill in the gaps with empty objects so that we generate a valid JSON array
				_serialized.append("{},");
			}
			if(!(node.getChildren().get(0) instanceof ACompositeNode)){
				_serialized.append("{");
			}
			indexMap.put(name, index);
		}
		else{
			String namePath = "{\"" + name + "\":";
			ANode parent = node.getParent();

			if(parent != null){
				if(((ACompositeNode) parent).getChildren().contains(node)){
					if(((ACompositeNode) parent).getChildren().indexOf(node) > 0){
						if(_serialized.length() != 0){
							namePath = namePath.replace("{", "");
						}
					}
				}
			}

			_serialized.append(namePath);

			if(node.getChildren().size() > 1){
				if(!(node.getChildren().get(0) instanceof ACompositeNode)){
					_serialized.append("{");
				}
			}

			// puts bracket around leaf simplestatenode
			else if(node.getChildren().size() == 1){
				if(!(node.getChildren().get(0) instanceof ACompositeNode)){
					_serialized.append("{");
				}
			}
			else if(node.getChildren().size() == 0){

				_serialized.append("{");
			}

		}
		_arraysLastIndexMap.put(node, new HashMap<String, Integer>());
	}

	public void generalACompositeStateNodeOut(ACompositeNode node){
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		//if(_serialized.toString().endsWith(",")){
			//_serialized.deleteCharAt(_serialized.lastIndexOf(","));
			_serialized.append(metaType);
//		/}

		if(node.isArray()){
			ANode sibling = node.nextSibling();
			if(sibling == null || !(sibling instanceof ACompositeNode) || !(((ACompositeNode) sibling).getBaseName().equals(node.getBaseName())))
			{

				_serialized.append("}],");
				return;
			}
			else{
				_serialized.append("},");
			}
		}
		else{
			if(node.getChildren().size() > 1){

				// no parent means double bracket
				if(node.getParent() == null){
					_serialized.append("}");
				}
				_serialized.append("},");
				return;
			}
			else{
				ANode parent = node.getParent();
				if(parent == null){
					_serialized.append("}");
				}
			}
			_serialized.append("},");	
		}
	}
	
	@Override
	public boolean inCompositeStateNode(CompositeVariableNode node)
	{
		this.generalACompositeStateNodeIn(node);
		return super.inCompositeStateNode(node);
	}

	@Override
	public boolean outCompositeStateNode(CompositeVariableNode node)
	{
		this.generalACompositeStateNodeOut(node);
		return super.outCompositeStateNode(node);
	}
	
	@Override
	public boolean inAspectNode(AspectNode node)
	{
		this.generalACompositeStateNodeIn(node);
		return super.inAspectNode(node);
	}

	@Override
	public boolean outAspectNode(AspectNode node)
	{
		this.generalACompositeStateNodeOut(node);
		return super.outAspectNode(node);
	}
	
	@Override
	public boolean inEntityNode(EntityNode node)
	{
		this.generalACompositeStateNodeIn(node);
		return super.inEntityNode(node);
	}
	
	@Override
	public boolean outEntityNode(EntityNode node)
	{
		this.generalACompositeStateNodeOut(node);
		return super.outEntityNode(node);
	}

	@Override
	public boolean inAspectTreeNode(AspectSubTreeNode node)
	{
		this.generalACompositeStateNodeIn(node);
		return super.inAspectTreeNode(node);
	}
	
	@Override
	public boolean outAspectTreeNode(AspectSubTreeNode node)
	{
		this.generalACompositeStateNodeOut(node);
		return super.outAspectTreeNode(node);
	}
	
	@Override
	public boolean inRuntimeTreeRoot(RuntimeTreeRoot node)
	{
		this.generalACompositeStateNodeIn(node);
		return super.inRuntimeTreeRoot(node);
	}

	@Override
	public boolean outRuntimeTreeRoot(RuntimeTreeRoot node)
	{
		this.generalACompositeStateNodeOut(node);
		return super.outRuntimeTreeRoot(node);
	}
	@Override
	public boolean visitStateVariableNode(StateVariableNode node)
	{
		String metaType = "";
		AValue value = node.consumeFirstValue();
		String unit = null, scale = null;
		
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		if(node.getUnit() != null){
			unit = "\"" + node.getUnit() + "\"";
		}
		if(node.getScalingFactor() != null){
			scale = "\"" + node.getScalingFactor() + "\"";
		}
		_serialized.append("\"" + node.getName() + "\":{\"value\":" + value + ",\"unit\":" + unit + ",\"scale\":" + scale +","+ metaType+ "},");

		return super.visitStateVariableNode(node);
	}
	
	@Override
	public boolean visitParameterNode(ParameterNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		Set<String> keys = node.getProperties().keySet();
		StringBuilder properties = new StringBuilder();
		
		for(String key : keys){
			properties.append("\"" + key + "\":\""+node.getProperties().get(key)+"\",");
		}
		_serialized.append("\"" + node.getName() + "\":{"+properties+metaType+"},");

		return super.visitParameterNode(node);
	}
	
	@Override
	public boolean visitTimeNode(StateVariableNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		_serialized.append("\"" + node.getName() + "\":{"+metaType+"},");

		return super.visitTimeNode(node);
	}
	
	@Override
	public boolean visitTextMetadataNode(TextMetadataNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		_serialized.append("\"" + node.getName() + "\":{"+metaType+"},");
		
		return super.visitTextMetadataNode(node);
	}
	
	@Override
	public boolean visitURLMetadataNode(URLMetadataNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		_serialized.append("\"" + node.getName() + "\":{"+metaType+"},");
		
		return super.visitURLMetadataNode(node);
	}
	
	@Override
	public boolean visitSphereNode(SphereNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
				
		Point position = node.getPosition();
		String name = node.getName();
		String positionString = null;
		
		if(position!=null){
			positionString = "\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + ","+"\"z\":" + position.getZ().toString() + "";
		}
		
		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," + metaType + "},");

		return super.visitSphereNode(node);
	}
	
	@Override
	public boolean visitCylinderNode(CylinderNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
				
		Point position = node.getPosition();
		String name = node.getName();		
		String positionString = null;
		
		if(position!=null){
			positionString = "\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + ","+"\"z\":" + position.getZ().toString() + "";
		}
		
		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," + metaType + "},");

		return super.visitCylinderNode(node);
	}
	
	@Override
	public boolean visitParticleNode(ParticleNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
				
		Point position = node.getPosition();
		String name = node.getName();		
		String positionString = null;
		
		if(position!=null){
			positionString = "\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + ","+"\"z\":" + position.getZ().toString() + "";
		}
		
		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," + metaType + "},");

		return super.visitParticleNode(node);
	}
	
	@Override
	public boolean visitColladaNode(ColladaNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
				
		Point position = node.getPosition();
		String name = node.getName();		
		String positionString = null;
		
		if(position!=null){
			positionString = "\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + ","+"\"z\":" + position.getZ().toString() + "";
		}
		
		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," + metaType + "},");

		return super.visitColladaNode(node);
	}

	public String getSerializedTree()
	{
		if(_serialized.charAt(_serialized.length() - 1) == ',') _serialized.deleteCharAt(_serialized.lastIndexOf(","));
		return _serialized.toString();
	}

}
