/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011 - 2015 OpenWorm.
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
/**
 * Serialization class for nodes  
 *
 * @author  Jesus R. Martinez (jesus@metacell.us)
 */
package org.geppetto.core.model.state.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.quantities.Quantity;
import org.geppetto.core.model.quantities.Unit;
import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.ATimeSeriesNode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.ColladaNode;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.ConnectionNode;
import org.geppetto.core.model.runtime.CylinderNode;
import org.geppetto.core.model.runtime.DynamicsSpecificationNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.FunctionNode;
import org.geppetto.core.model.runtime.OBJNode;
import org.geppetto.core.model.runtime.ParameterNode;
import org.geppetto.core.model.runtime.ParameterSpecificationNode;
import org.geppetto.core.model.runtime.ParticleNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.SkeletonAnimationNode;
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.TextMetadataNode;
import org.geppetto.core.model.runtime.URLMetadataNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.runtime.VisualGroupElementNode;
import org.geppetto.core.model.runtime.VisualGroupNode;
import org.geppetto.core.model.runtime.VisualObjectReferenceNode;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.visualisation.model.Point;

import com.google.gson.JsonObject;

public class SerializeTreeVisitor extends RuntimeTreeVisitor
{
	private StringBuilder _serialized = new StringBuilder();
	private Map<ANode, Map<String, Integer>> _arraysLastIndexMap = new HashMap<ANode, Map<String, Integer>>();

	public SerializeTreeVisitor()
	{
		super();
	}

	/**
	 * Method that extracts most common properties of a node used by different serialization node methods
	 * 
	 * @param node
	 * @return
	 */
	private String commonProperties(ANode node)
	{

		String id = "";
		if(node.getId() != null)
		{
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}

		String name = "";
		if(node.getName() != null)
		{
			name = "\"name\":" + "\"" + node.getName() + "\",";
		}

		String domainType = "";
		if(node.getDomainType() != null)
		{
			domainType = "\"domainType\":" + "\"" + node.getDomainType() + "\",";
		}

		String instancePath = "";
		if(node.getInstancePath() != null)
		{
			if(!node.getInstancePath().equals(""))
			{
				instancePath = "\"instancePath\":" + "\"" + node.getInstancePath() + "\",";
			}
		}

		String metaType = "";
		if(node.getMetaType() != null)
		{
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		return id + name + domainType + instancePath + metaType;
	}

	@Override
	public boolean inCompositeNode(CompositeNode node)
	{
		if(node.isModified())
		{
			String id = node.getBaseName();
			if(node.isArray())
			{
				int index = node.getIndex();
				Map<String, Integer> indexMap = _arraysLastIndexMap.get(node.getParent());

				if(indexMap == null)
				{
					_arraysLastIndexMap.put(node.getParent(), new HashMap<String, Integer>());
					indexMap = _arraysLastIndexMap.get(node.getParent());
				}

				if(!indexMap.containsKey(id))
				{
					// if the object is not in the map we haven't found this array
					// before
					_serialized.append("\"" + node.getBaseName() + "\":[");
					indexMap.put(id, -1);
				}
				else
				{
					if(index == 0)
					{
						_serialized.append("{");
					}
				}
				if(indexMap.containsKey(id) && indexMap.get(id) > index)
				{
					throw new RuntimeException("The tree is not ordered, found surpassed index");
				}
				if(node.getChildren().size() == 0 || node.getChildren().get(0).isArray())
				{
					_serialized.append("{");
				}
				for(int i = indexMap.get(id); i < index - 1; i++)
				{
					// we fill in the gaps with empty objects so that we generate a
					// valid JSON array
					_serialized.append("{},");
				}
				indexMap.put(id, index);
			}
			else
			{
				// add bracket if array and it's at index 0
				if(node.getParent().isArray())
				{
					CompositeNode parent = (CompositeNode) node.getParent();
					if(parent.getChildren().indexOf(node) == 0)
					{
						_serialized.append("{");
					}
				}

				String namePath = "\"" + node.getBaseName() + "\":{";

				_serialized.append(namePath);

			}
			_arraysLastIndexMap.put(node, new HashMap<String, Integer>());
		}
		return super.inCompositeNode(node);
	}

	@Override
	public boolean outCompositeNode(CompositeNode node)
	{
		if(node.isModified())
		{
			String commonProperties = this.commonProperties(node);
			_serialized.append(commonProperties);

			if(node.isArray())
			{
				ANode sibling = node.nextSibling();
				if(sibling == null || !(sibling instanceof ACompositeNode) || !(((ACompositeNode) sibling).getBaseName().equals(node.getBaseName())))
				{

					_serialized.append("}],");
					return _stopVisiting;
				}
				else
				{
					_serialized.append("},");
				}
			}
			else
			{
				_serialized.append("},");
			}
		}
		return super.outCompositeNode(node);
	}

	@Override
	public boolean inAspectNode(AspectNode node)
	{
		if(node.isModified())
		{
			String namePath = "";

			namePath = "\"" + node.getBaseName() + "\":{";

			_serialized.append(namePath);
			return super.inAspectNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean outAspectNode(AspectNode node)
	{
		if(node.isModified())
		{
			String commonProperties = this.commonProperties(node);
			_serialized.append(commonProperties + "},");

			return super.outAspectNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean inEntityNode(EntityNode node)
	{
		if(node.isModified())
		{
			String namePath = "\"" + node.getBaseName() + "\":";

			_serialized.append(namePath + "{");
			return super.inEntityNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean outEntityNode(EntityNode node)
	{
		if(node.isModified())
		{
			String commonProperties = this.commonProperties(node);

			Point position = node.getPosition();
			String positionString = "";

			if(position != null)
			{
				positionString = "\"position\":{" + "\"x\":" + position.getX().toString() + "," + "\"y\":" + position.getY().toString() + "," + "\"z\":" + position.getZ().toString() + "},";
			}

			_serialized.append(positionString + commonProperties + "},");
			return super.outEntityNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean inAspectSubTreeNode(AspectSubTreeNode node)
	{
		if(node.isModified())
		{
			String namePath = "\"" + node.getBaseName() + "\":{";

			_serialized.append(namePath);

			return super.inAspectSubTreeNode(node);
		}

		return this._stopVisiting;
	}

	@Override
	public boolean outAspectSubTreeNode(AspectSubTreeNode node)
	{
		if(node.isModified())
		{
			String type = "";
			if(node.getType() != null)
			{
				type = "\"type\":" + "\"" + node.getType() + "\",";
			}

			String commonProperties = this.commonProperties(node);

			_serialized.append(type + commonProperties + "},");

			return super.outAspectSubTreeNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean inRuntimeTreeRoot(RuntimeTreeRoot node)
	{
		String namePath = "{\"" + node.getBaseName() + "\":{";

		_serialized.append(namePath);
		return super.inRuntimeTreeRoot(node);
	}

	@Override
	public boolean outRuntimeTreeRoot(RuntimeTreeRoot node)
	{
		String metaType = "";
		if(node.getMetaType() != null)
		{
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		_serialized.append(metaType + "}},");
		return super.outRuntimeTreeRoot(node);
	}

	@Override
	public boolean inConnectionNode(ConnectionNode node)
	{
		if(node.isModified())
		{
			String namePath = "\"" + node.getId() + "\":";

			_serialized.append(namePath + "{");
			return super.inConnectionNode(node);
		}

		return this._stopVisiting;
	}

	@Override
	public boolean outConnectionNode(ConnectionNode node)
	{
		if(node.isModified())
		{
			String commonproperties = this.commonProperties(node);

			String entityId = "";
			if(node.getEntityInstancePath() != null)
			{
				entityId = "\"entityInstancePath\":" + "\"" + node.getEntityInstancePath() + "\",";
			}
			
			String aspectInstancePath = "\"aspectInstancePath\":" + "\"" + node.getAspectInstancePath() + "\",";
			String type = "\"type\":" + "\"" + node.getConnectionType().toString() + "\",";

			_serialized.append(entityId + type + aspectInstancePath + commonproperties + "},");
			return super.outConnectionNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean inVisualGroupNode(VisualGroupNode node)
	{
		String namePath = "\"" + node.getId() + "\":";

		_serialized.append(namePath + "{");
		return super.inVisualGroupNode(node);

	}

	@Override
	public boolean outVisualGroupNode(VisualGroupNode node)
	{

		String commonproperties = this.commonProperties(node);

		String highSpectrumColor = "";
		if(node.getHighSpectrumColor() != null)
		{
			highSpectrumColor = "\"highSpectrumColor\":" + "\"" + node.getHighSpectrumColor() + "\",";
		}

		String lowSpectrumColor = "";
		if(node.getLowSpectrumColor() != null)
		{
			lowSpectrumColor = "\"lowSpectrumColor\":" + "\"" + node.getLowSpectrumColor() + "\",";
		}

		String type = "\"type\":" + "\"" + node.getType() + "\",";

		_serialized.append(highSpectrumColor + lowSpectrumColor + type + commonproperties + "},");
		return super.outVisualGroupNode(node);
	}

	@Override
	public boolean visitVisualObjectReferenceNode(VisualObjectReferenceNode node)
	{

		String aspectInstancePath = "\"aspectInstancePath\":" + "\"" + node.getAspectInstancePath() + "\",";

		String visualObjectID = "\"visualObjectID\":" + "\"" + node.getVisualObjectId() + "\",";

		String commonProperties = this.commonProperties(node);

		_serialized.append("\"" + node.getId() + "\":{" + aspectInstancePath + visualObjectID + commonProperties + "},");

		return super.visitVisualObjectReferenceNode(node);
	}

	@Override
	public boolean visitVisualGroupElementNode(VisualGroupElementNode node)
	{

		String parameter = "";
		if(node.getParameter() != null)
		{
			String param = "value\":" + node.getParameter().getValue().toString() + ",\"scalingFactor\":" + "\"" + node.getParameter().getScalingFactor() + "\"" + ",\"unit\":" + "\""
					+ node.getParameter().getUnit();
			parameter = "\"parameter\":{" + "\"" + param + "\"},";
		}

		String color = "\"color\":" + "\"" + node.getDefaultColor() + "\",";

		String commonProperties = this.commonProperties(node);

		_serialized.append("\"" + node.getId() + "\":{" + parameter + color + commonProperties + "},");

		return super.visitVisualGroupElementNode(node);
	}

	@Override
	public boolean visitVariableNode(VariableNode node)
	{
		if(node.isModified())
		{
			timeSeries(node);
		}
		return super.visitVariableNode(node);
	}

	@Override
	public boolean visitParameterNode(ParameterNode node)
	{
		if(node.isModified())
		{
			timeSeries(node);
		}
		return super.visitParameterNode(node);
	}

	private void timeSeries(ATimeSeriesNode node)
	{
		if(node.getParent().isArray())
		{
			CompositeNode parent = (CompositeNode) node.getParent();
			if(parent.getChildren().indexOf(node) == 0)
			{
				_serialized.append("{");
			}
		}

		String commonProperties = this.commonProperties(node);

		String watched = "\"watched\":" + "\"" + node.isWatched() + "\",";

		String unit = "\"unit\":" + ((node.getUnit() != null) ? "\"" + node.getUnit() + "\"" : null) + ",";

		if(node.getTimeSeries().size() > 0)
		{
			_serialized.append("\"" + node.getId() + "\":{\"timeSeries\":{");
			for(int i = 0; i < node.getTimeSeries().size(); i++)
			{
				Quantity quantity = node.getTimeSeries().get(i);
				if(quantity != null)
				{
					AValue value = quantity.getValue();
					String scale = null;

					if(quantity.getScalingFactor() != null)
					{
						scale = "\"" + quantity.getScalingFactor() + "\"";
					}
					String qName = "quantity" + String.valueOf(i);
					_serialized.append("\"" + qName + "\":{\"value\":" + value + ",\"scale\":" + scale + "},");
				}
			}
			if(_serialized.charAt(_serialized.length() - 1) == ',') _serialized.deleteCharAt(_serialized.lastIndexOf(","));
			_serialized.append("}," + watched + unit + commonProperties + "},");
			node.getTimeSeries().clear();
		}
		else
		{
			_serialized.append("\"" + node.getId() + "\":{" + watched + unit + commonProperties + "},");
		}
	}

	public boolean visitDynamicsSpecificationNode(DynamicsSpecificationNode node)
	{
		if(node.isModified())
		{
			String commonProperties = this.commonProperties(node);

			PhysicalQuantity quantity = node.getInitialConditions();
			FunctionNode functionNode = node.getDynamics();

			String specs = "", function = "";

			if(quantity != null)
			{
				AValue value = quantity.getValue();
				Unit unit = null;
				String scale = null;

				if(quantity.getUnit() != null)
				{
					unit = quantity.getUnit();
				}
				if(quantity.getScalingFactor() != null)
				{
					scale = "\"" + quantity.getScalingFactor() + "\"";
				}
				specs = "\"value\":" + "\"" + value + "\",\"unit\":" + "\"" + unit + "\",\"scale\":" + scale + ",";
			}

			if(functionNode != null)
			{

				String properties = "";

				if(functionNode.getArgument() != null)
				{
					List<String> arguments = functionNode.getArgument();

					properties = "," + "\"arguments\":{";

					for(int index = 0; index < arguments.size(); index++)
					{
						properties = properties.concat("\"" + index + "\":\"" + arguments.get(index) + "\"");
						if(index < (arguments.size() - 1))
						{
							properties = properties.concat(",");
						}
					}

					properties = properties.concat("}");
				}

				function = "\"_function\":{" + "\"expression\":" + "\"" + functionNode.getExpression() + "\"" + properties + "},";
			}

			_serialized.append("\"" + node.getId() + "\":{" + specs + function + commonProperties + "},");
		}
		return super.visitDynamicsSpecificationNode(node);
	}

	public boolean visitParameterSpecificationNode(ParameterSpecificationNode node)
	{
		if(node.isModified())
		{
			String commonProperties = this.commonProperties(node);

			PhysicalQuantity quantity = node.getValue();

			if(quantity != null)
			{
				AValue value = quantity.getValue();
				String unit = null, scale = null;

				if(quantity.getUnit() != null)
				{
					unit = "\"" + quantity.getUnit() + "\"";
				}
				if(quantity.getScalingFactor() != null)
				{
					scale = "\"" + quantity.getScalingFactor() + "\"";
				}
				_serialized.append("\"" + node.getId() + "\":{\"value\":" + "\"" + value + "\",\"unit\":" + unit + ",\"scale\":" + scale + "," + commonProperties + "},");
			}
			else
			{
				_serialized.append("\"" + node.getId() + "\":{" + commonProperties + "},");
			}

		}
		return super.visitParameterSpecificationNode(node);
	}

	public boolean visitFunctionNode(FunctionNode node)
	{
		if(node.isModified())
		{
			String commonProperties = this.commonProperties(node);

			String properties = "";

			if(node.getArgument() != null)
			{
				List<String> arguments = node.getArgument();

				properties = "\"arguments\":{";

				for(int index = 0; index < arguments.size(); index++)
				{
					properties = properties.concat("\"" + index + "\":\"" + arguments.get(index) + "\"");
					if(index < (arguments.size() - 1))
					{
						properties = properties.concat(",");
					}
				}

				properties = properties.concat("},");
			}

			String plotMetadata = "";

			if(node.getPlotMetadata().size() > 0)
			{
				HashMap<String, String> props = node.getPlotMetadata();

				plotMetadata = "\"plotMetadata\":{";

				Set<String> keys = props.keySet();
				int index = 0;
				for(String key : keys)
				{
					plotMetadata = plotMetadata.concat("\"" + key + "\":\"" + props.get(key) + "\"");
					if(index < (props.size() - 1))
					{
						plotMetadata = plotMetadata.concat(",");
					}
					index++;
				}

				plotMetadata = plotMetadata.concat("},");
			}

			_serialized.append("\"" + node.getId() + "\":{" + "\"expression\":" + "\"" + node.getExpression() + "\"," + properties + plotMetadata + commonProperties + "},");
		}
		return super.visitFunctionNode(node);
	}

	@Override
	public boolean visitTextMetadataNode(TextMetadataNode node)
	{
		if(node.isModified())
		{
			String commonProperties = this.commonProperties(node);
			String valueString = "";
			if(node.getValue() != null)
			{
				AValue value = node.getValue();
				valueString = "\"value\":" + "\"" + value + "\",";
			}

			_serialized.append("\"" + node.getId() + "\":{" + valueString.replaceAll("[\n\r]", "") + commonProperties + "},");
		}
		return super.visitTextMetadataNode(node);
	}

	@Override
	public boolean visitURLMetadataNode(URLMetadataNode node)
	{
		String commonProperties = this.commonProperties(node);

		String url = "";
		if(node.getURL() != null)
		{
			url = "\"url\":" + "\"" + node.getURL() + "\",";
		}

		_serialized.append("\"" + node.getId() + "\":{" + url + commonProperties + "},");

		return super.visitURLMetadataNode(node);
	}

	@Override
	public boolean visitSphereNode(SphereNode node)
	{
		String commonProperties = this.commonProperties(node);

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if(position != null)
		{
			positionString = "\"x\":" + position.getX().toString() + "," + "\"y\":" + position.getY().toString() + "," + "\"z\":" + position.getZ().toString() + "";
		}

		Double radius = node.getRadius();
		String radiusString = null;
		if(radius != null)
		{
			radiusString = "\"" + radius.toString() + "\"";
		}

		List<String> map = node.getGroupElementsMap();

		String groups = "\"groups\":{";

		for(int index = 0; index < map.size(); index++)
		{
			groups = groups.concat("\"" + index + "\":\"" + map.get(index) + "\"");
			if(index < (map.size() - 1))
			{
				groups = groups.concat(",");
			}
		}

		groups = groups.concat("},");

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," + groups + "\"radius\":" + radiusString + "," + commonProperties + "},");

		return super.visitSphereNode(node);
	}

	@Override
	public boolean visitCylinderNode(CylinderNode node)
	{
		String commonProperties = this.commonProperties(node);

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if(position != null)
		{
			positionString = "\"x\":" + position.getX().toString() + "," + "\"y\":" + position.getY().toString() + "," + "\"z\":" + position.getZ().toString() + "";
		}

		Point distal = node.getDistal();
		String distalString = "";

		if(distal != null)
		{
			distalString = "\"x\":" + distal.getX().toString() + "," + "\"y\":" + distal.getY().toString() + "," + "\"z\":" + distal.getZ().toString() + "";
		}

		Double radiusBottom = node.getRadiusBottom();
		String radiusBottomString = "";
		if(radiusBottom != null)
		{
			radiusBottomString = "\"" + radiusBottom.toString() + "\"";
		}

		Double radiusTop = node.getRadiusTop();
		String radiusTopString = "";
		if(radiusTop != null)
		{
			radiusTopString = "\"" + radiusTop.toString() + "\"";
		}

		List<String> map = node.getGroupElementsMap();

		String groups = "\"groups\":{";

		for(int index = 0; index < map.size(); index++)
		{
			groups = groups.concat("\"" + index + "\":\"" + map.get(index) + "\"");
			if(index < (map.size() - 1))
			{
				groups = groups.concat(",");
			}
		}

		groups = groups.concat("},");

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," + "\"distal\":{" + distalString + "}," + groups + "\"radiusBottom\":" + radiusBottomString + ","
				+ "\"radiusTop\":" + radiusTopString + "," + commonProperties + "},");

		return super.visitCylinderNode(node);
	}

	@Override
	public boolean visitParticleNode(ParticleNode node)
	{
		String commonProperties = this.commonProperties(node);

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if(position != null)
		{
			positionString = "\"x\":" + position.getX().toString() + "," + "\"y\":" + position.getY().toString() + "," + "\"z\":" + position.getZ().toString() + "";
		}

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," + commonProperties + "},");

		return super.visitParticleNode(node);
	}

	@Override
	public boolean visitColladaNode(ColladaNode node)
	{
		String commonProperties = this.commonProperties(node);

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if(position != null)
		{
			positionString = "\"x\":" + position.getX().toString() + "," + "\"y\":" + position.getY().toString() + "," + "\"z\":" + position.getZ().toString() + "";
		}

		String model = "";
		if(node.getModel() != null)
		{
			JsonObject obj = new JsonObject();
			obj.addProperty("data", node.getModel());
			model = "\"model\":" + obj.toString() + ",";
		}

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," + model + commonProperties + "},");

		return super.visitColladaNode(node);
	}

	@Override
	public boolean visitObjNode(OBJNode node)
	{
		String commonProperties = this.commonProperties(node);

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if(position != null)
		{
			positionString = "\"x\":" + position.getX().toString() + "," + "\"y\":" + position.getY().toString() + "," + "\"z\":" + position.getZ().toString() + "";
		}

		String model = "";
		if(node.getModel() != null)
		{
			JsonObject obj = new JsonObject();
			obj.addProperty("data", node.getModel());
			model = "\"model\":" + obj.toString() + ",";
		}

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString + "}," + model + commonProperties + "},");

		return super.visitObjNode(node);
	}

	@Override
	public boolean visitSkeletonAnimationNode(SkeletonAnimationNode node)
	{
		String commonProperties = this.commonProperties(node);
		String name = node.getId();

		List<List<Double>> transformationsSeries = node.getSkeletonTransformationSeries();
		String transformationsString = "";

		if(transformationsSeries != null)
		{

			// open bracket for array of matrices
			transformationsString += "[";

			// Loop list of matrices
			for(int i = 0; i < transformationsSeries.size(); i++)
			{
				List<Double> transformation = transformationsSeries.get(i);

				// if any items
				if(transformation.size() > 0)
				{

					transformationsString += transformation.toString();

					// add comma unless it's the last element
					if(i != transformationsSeries.size() - 1)
					{
						transformationsString += ",";
					}
				}
			}

			// close bracket for array of matrices
			transformationsString += "]";
		}
		else
		{
			transformationsString = "[]";
		}

		_serialized.append("\"" + name + "\":{\"skeletonTransformations\":" + transformationsString + "," + commonProperties + "},");

		return super.visitSkeletonAnimationNode(node);
	}

	public String getSerializedTree()
	{

		if(_serialized.length() != 0)
		{
			if(_serialized.charAt(_serialized.length() - 1) == ',')
			{
				_serialized.deleteCharAt(_serialized.lastIndexOf(","));
			}
			if(_serialized.charAt(0) != '{')
			{
				return "{" + _serialized.toString() + "}";
			}
			return _serialized.toString();
		}
		return "";
	}
}
