package org.geppetto.core.model.state.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.ColladaNode;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.CylinderNode;
import org.geppetto.core.model.runtime.DynamicsSpecificationNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.FunctionNode;
import org.geppetto.core.model.runtime.ParameterNode;
import org.geppetto.core.model.runtime.ParameterSpecificationNode;
import org.geppetto.core.model.runtime.ParticleNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.VariableNode;
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
		
		_serialized.append(metaType);

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
	
	private String formatInstancePath(ANode node){
		String instancePath = "";
		if(node.getInstancePath()!=null){
			if(!node.getInstancePath().equals("")){
				instancePath = "\"instancePath\":" + "\"" + node.getInstancePath()+ "\",";
			}
		}
		
		return instancePath;
	}
	
	@Override
	public boolean inCompositeVariableNode(CompositeNode node)
	{
		this.generalACompositeStateNodeIn(node);
		return super.inCompositeVariableNode(node);
	}

	@Override
	public boolean outCompositeVariableNode(CompositeNode node)
	{
		String instancePath = this.formatInstancePath(node);

		_serialized.append(instancePath);
		
		this.generalACompositeStateNodeOut(node);
		return super.outCompositeVariableNode(node);
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
		
		String id = "";
		if(node.getId() != null){
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}
		
		String simulator = "";
		if(node.getSimulator() != null){
			if(node.getSimulator().getName() != null){
				simulator = "\"simulator\":" + "\"" + node.getSimulator().getName() + "\",";
			}
		}

		String modelInterpreter = "";
		if(node.getModelInterpreter() != null){
			if(node.getModelInterpreter().getName() != null){
				modelInterpreter = "\"modelInterpreter\":" + "\"" + node.getModelInterpreter().getName() + "\",";
			}
		}
		
		String instancePath = this.formatInstancePath(node);
				
		_serialized.append(id  + simulator+modelInterpreter+instancePath);
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
		String id = "";
		if(node.getId() != null){
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}
		
		Point position = node.getPosition();
		String positionString = "";
		
		if(position!=null){
			positionString = "\"position\":{" +"\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + ","+"\"z\":" + position.getZ().toString() + "},";
		}
		
		String instancePath = this.formatInstancePath(node);
		
		_serialized.append(id  + positionString + instancePath);

		this.generalACompositeStateNodeOut(node);
		return super.outEntityNode(node);
	}

	@Override
	public boolean inAspectSubTreeNode(AspectSubTreeNode node)
	{
		this.generalACompositeStateNodeIn(node);
		return super.inAspectSubTreeNode(node);
	}
	
	@Override
	public boolean outAspectSubTreeNode(AspectSubTreeNode node)
	{
		String type = "";
		if(node.getType() != null){
			type = "\"type\":" + "\"" + node.getType() + "\",";
		}
		
		String instancePath = this.formatInstancePath(node);

		_serialized.append(type+instancePath);
		
		this.generalACompositeStateNodeOut(node);
		return super.outAspectSubTreeNode(node);
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
	public boolean visitVariableNode(VariableNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		String instancePath = this.formatInstancePath(node);

		PhysicalQuantity quantity = node.consumeFirstValue();

		if(quantity != null){
			AValue value = quantity.getValue();
			String unit = null, scale = null;

			if(quantity.getUnit() != null){
				unit = "\"" + quantity.getUnit() + "\"";
			}
			if(quantity.getScalingFactor() != null){
				scale = "\"" + quantity.getScalingFactor() + "\"";
			}
			_serialized.append("\"" + node.getName() + "\":{\"value\":" + value + ",\"unit\":"
											+ unit + ",\"scale\":" + scale +","+ instancePath + metaType+ "},");
		}
		else{
			_serialized.append("\"" + node.getName() +"\":{"+instancePath+metaType+"},");
		}

		return super.visitVariableNode(node);
	}
	
	@Override
	public boolean visitParameterNode(ParameterNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		String instancePath = this.formatInstancePath(node);

		String properties ="";
		
		if(node.getProperties().size()>0){
			HashMap<String, String> props = node.getProperties();

			properties = ",\"properties\":{";
			
			Set<String> keys = props.keySet();
			int index =0;
			for(String key : keys){
				index++;
				properties = properties.concat("\""+key+ "\":\""+props.get(key)+"\"" );
				if(index < (props.size() -1)){
					properties = properties.concat(",");
				}
			}
			
			properties = properties.concat("},");
		}
		_serialized.append("\"" + node.getName() + "\":{"+instancePath+metaType+properties+"},");

		return super.visitParameterNode(node);
	}
	
	public boolean visitDynamicsSpecificationNode(DynamicsSpecificationNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		String instancePath = this.formatInstancePath(node);
		
		PhysicalQuantity quantity = node.getInitialConditions();
		FunctionNode functionNode = node.getDynamics();

		String specs ="", function="";
		
		if(quantity != null){
			AValue value = quantity.getValue();
			String unit = null, scale = null;

			if(quantity.getUnit() != null){
				unit = quantity.getUnit();
			}
			if(quantity.getScalingFactor() != null){
				scale = "\"" + quantity.getScalingFactor() + "\"";
			}
			specs = "\"value\":" +"\""+ value + "\",\"unit\":" + "\""+ unit + "\",\"scale\":" + scale +",";
		}
		
		if(functionNode!=null){
			
			String properties="";

			if(functionNode.getArgument()!=null){
				List<String> arguments = functionNode.getArgument();

				properties = "," + "\"arguments\":{";
				
				for(int index =0; index<arguments.size(); index++){
					properties = properties.concat("\""+index+ "\":\""+arguments.get(index)+"\"" );
					if(index < (arguments.size() -1)){
						properties = properties.concat(",");
					}
				}
				
				properties = properties.concat("}");
			}
			
			function = "\"_function\":{" + "\"expression\":" + "\"" + functionNode.getExpression() +"\""+ properties + "},";
		}
			
		_serialized.append("\"" + node.getName() +"\":{"+specs + function + instancePath + metaType+"},");
		

		return super.visitDynamicsSpecificationNode(node);
	}
	
	public boolean visitParameterSpecificationNode(ParameterSpecificationNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		String instancePath = this.formatInstancePath(node);

		PhysicalQuantity quantity = node.getValue();

		if(quantity != null){
			AValue value = quantity.getValue();
			String unit = null, scale = null;

			if(quantity.getUnit() != null){
				unit = "\"" + quantity.getUnit() + "\"";
			}
			if(quantity.getScalingFactor() != null){
				scale = "\"" + quantity.getScalingFactor() + "\"";
			}
			_serialized.append("\"" + node.getName() + "\":{\"value\":" + "\""+ value + "\",\"unit\":" + unit + 
								",\"scale\":" + scale +","+ instancePath + metaType+ "},");
		}
		else{
			_serialized.append("\"" + node.getName() +"\":{"+instancePath+metaType+"},");
		}

		return super.visitParameterSpecificationNode(node);
	}
	
	public boolean visitFunctionNode(FunctionNode node)
	{
		String metaType = "";
		if(node.getMetaType() != null){
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}
		
		String instancePath = this.formatInstancePath(node);

		String properties ="";
		
		if(node.getArgument()!=null){
			List<String> arguments = node.getArgument();

			properties = "\"arguments\":{";
			
			for(int index =0; index<arguments.size(); index++){
				properties = properties.concat("\""+index+ "\":\""+arguments.get(index)+"\"" );
				if(index < (arguments.size() -1)){
					properties = properties.concat(",");
				}
			}
			
			properties = properties.concat("},");
		}
		
		_serialized.append("\"" + node.getName() + "\":{"+ 
				"\"expression\":" + "\"" + node.getExpression() + "\","+properties+ instancePath + metaType+"},");

		return super.visitFunctionNode(node);
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
		String id = "";
		if(node.getId() != null){
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}
		
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
		
		Double radius = node.getRadius();
		String radiusString = null;
		if(radius!=null){
			radiusString = "\"" + radius.toString() + "\"";
		}
		
		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," 
								+"\"radius\":" + radiusString + ","+ id + metaType + "},");

		return super.visitSphereNode(node);
	}
	
	@Override
	public boolean visitCylinderNode(CylinderNode node)
	{
		String id = "";
		if(node.getId() != null){
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}
		
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
		
		Point distal = node.getDistal();
		String distalString = null;
		
		if(distal!=null){
			distalString = "\"x\":" + distal.getX().toString() + ","
					+ "\"y\":" + distal.getY().toString() + ","+"\"z\":" + distal.getZ().toString() + "";
		}
		
		Double radiusBottom = node.getRadiusBottom();
		String radiusBottomString = null;
		if(radiusBottom!=null){
			radiusBottomString = "\"" + radiusBottom.toString() + "\"";
		}
		
		Double radiusTop = node.getRadiusTop();
		String radiusTopString = null;
		if(radiusTop!=null){
			radiusTopString = "\"" + radiusTop.toString() + "\"";
		}
		
		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," +"\"distal\":{" + distalString + "},"
								+"\"radiusBottom\":" + radiusBottomString + ","+"\"radiusTop\":" + radiusTopString + ","+ id + metaType + "},");

		return super.visitCylinderNode(node);
	}
	
	@Override
	public boolean visitParticleNode(ParticleNode node)
	{
		String id = "";
		if(node.getId() != null){
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}
		
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
		
		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "},"+ id+ metaType + "},");

		return super.visitParticleNode(node);
	}
	
	@Override
	public boolean visitColladaNode(ColladaNode node)
	{
		String id = "";
		if(node.getId() != null){
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}
		
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
		
		_serialized.append("\"" + name + "," + "\":{\"position\":{" + positionString + "}," + metaType + "},");

		return super.visitColladaNode(node);
	}

	public String getSerializedTree()
	{
		if(_serialized.charAt(_serialized.length() - 1) == ',') _serialized.deleteCharAt(_serialized.lastIndexOf(","));
		return _serialized.toString();
	}

}
