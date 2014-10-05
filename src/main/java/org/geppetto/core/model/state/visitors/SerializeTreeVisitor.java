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
import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
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
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.TextMetadataNode;
import org.geppetto.core.model.runtime.URLMetadataNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.visualisation.model.Point;

import com.google.gson.JsonObject;

public class SerializeTreeVisitor extends DefaultStateVisitor {
	private StringBuilder _serialized = new StringBuilder();
	private Map<ANode, Map<String, Integer>> _arraysLastIndexMap = new HashMap<ANode, Map<String, Integer>>();

	public SerializeTreeVisitor() {
		super();
	}

	/**
	 * Method that extracts most common properties of a node used by different
	 * serialization node methods
	 * 
	 * @param node
	 * @return
	 */
	private String commonProperties(ANode node) {

		String id = "";
		if (node.getId() != null) {
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}

		String instancePath = "";
		if (node.getInstancePath() != null) {
			if (!node.getInstancePath().equals("")) {
				instancePath = "\"instancePath\":" + "\""
						+ node.getInstancePath() + "\",";
			}
		}

		String metaType = "";
		if (node.getMetaType() != null) {
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		return id + instancePath + metaType;
	}

	@Override
	public boolean inCompositeNode(CompositeNode node) {
		String id = node.getId();
		if (node.isArray()) {
			int index = node.getIndex();
			Map<String, Integer> indexMap = _arraysLastIndexMap.get(node
					.getParent());

			if (indexMap == null) {
				_arraysLastIndexMap.put(node.getParent(),
						new HashMap<String, Integer>());
				indexMap = _arraysLastIndexMap.get(node.getParent());
			}

			if (!indexMap.containsKey(id)) {
				// if the object is not in the map we haven't found this array
				// before
				_serialized.append("\"" + node.getBaseName() + "\":[");
				indexMap.put(id, -1);
			} else {
				_serialized.append("{");
			}
			if (indexMap.containsKey(id) && indexMap.get(id) > index) {
				throw new RuntimeException(
						"The tree is not ordered, found surpassed index");
			}
			for (int i = indexMap.get(id); i < index - 1; i++) {
				// we fill in the gaps with empty objects so that we generate a
				// valid JSON array
				_serialized.append("{},");
			}
			indexMap.put(id, index);
		} else {
			// add bracket if array and it's at index 0
			if (node.getParent().isArray()) {
				CompositeNode parent = (CompositeNode) node.getParent();
				if (parent.getChildren().indexOf(node) == 0) {
					_serialized.append("{");
				}
			}

			String namePath = "\"" + node.getBaseName() + "\":{";

			_serialized.append(namePath);

		}
		_arraysLastIndexMap.put(node, new HashMap<String, Integer>());
		return super.inCompositeNode(node);
	}

	@Override
	public boolean outCompositeNode(CompositeNode node) {
		String commonProperties = this.commonProperties(node);
		_serialized.append(commonProperties);

		if (node.isArray()) {
			ANode sibling = node.nextSibling();
			if (sibling == null
					|| !(sibling instanceof ACompositeNode)
					|| !(((ACompositeNode) sibling).getBaseName().equals(node
							.getBaseName()))) {

				_serialized.append("}],");
				return _stopVisiting;
			} else {
				_serialized.append("},");
			}
		} else {
			_serialized.append("},");
		}

		return super.outCompositeNode(node);
	}

	@Override
	public boolean inAspectNode(AspectNode node) {
		if (node.isModified()) {
			String namePath = "";

			namePath = "\"" + node.getBaseName() + "\":{";

			_serialized.append(namePath);
			return super.inAspectNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean outAspectNode(AspectNode node) {
		if (node.isModified()) {
			String commonProperties = this.commonProperties(node);
			_serialized.append(commonProperties + "},");

			return super.outAspectNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean inEntityNode(EntityNode node) {
		if (node.isModified()) {
			String namePath = "\"" + node.getBaseName() + "\":";

			_serialized.append(namePath + "{");
			return super.inEntityNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean outEntityNode(EntityNode node) {
		if (node.isModified()) {
			String commonProperties = this.commonProperties(node);

			Point position = node.getPosition();
			String positionString = "";

			if (position != null) {
				positionString = "\"position\":{" + "\"x\":"
						+ position.getX().toString() + "," + "\"y\":"
						+ position.getY().toString() + "," + "\"z\":"
						+ position.getZ().toString() + "},";
			}

			_serialized.append(positionString + commonProperties + "},");
			return super.outEntityNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean inAspectSubTreeNode(AspectSubTreeNode node) {
		if (node.isModified()) {
			String namePath = "\"" + node.getBaseName() + "\":{";

			_serialized.append(namePath);

			return super.inAspectSubTreeNode(node);
		}

		return this._stopVisiting;
	}

	@Override
	public boolean outAspectSubTreeNode(AspectSubTreeNode node) {
		if (node.isModified()) {
			String type = "";
			if (node.getType() != null) {
				type = "\"type\":" + "\"" + node.getType() + "\",";
			}

			String instancePath = "";
			if (node.getInstancePath() != null) {
				if (!node.getInstancePath().equals("")) {
					instancePath = "\"instancePath\":" + "\""
							+ node.getInstancePath() + "\",";
				}
			}

			String metaType = "";
			if (node.getMetaType() != null) {
				metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
			}

			_serialized.append(type + instancePath + metaType + "},");

			return super.outAspectSubTreeNode(node);
		}
		return this._stopVisiting;
	}

	@Override
	public boolean inRuntimeTreeRoot(RuntimeTreeRoot node) {
		String namePath = "{\"" + node.getBaseName() + "\":{";

		_serialized.append(namePath);
		return super.inRuntimeTreeRoot(node);
	}

	@Override
	public boolean outRuntimeTreeRoot(RuntimeTreeRoot node) {
		String metaType = "";
		if (node.getMetaType() != null) {
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		_serialized.append(metaType + "}},");
		return super.outRuntimeTreeRoot(node);
	}

	@Override
	public boolean visitConnectionNode(ConnectionNode node) {

		String commonproperties = this.commonProperties(node);
		
		String entityId = "";
		if (node.getEntityInstancePath() != null) {
			entityId = "\"entityInstancePath\":" + "\"" + node.getEntityInstancePath()+ "\",";
		}
		
		String type = "\"type\":" + "\"" +node.getConnectionType().toString()+ "\",";
		
		_serialized.append("\"" + node.getId() + "\":{"
				+entityId + type+ commonproperties+ "},");
		return super.visitConnectionNode(node);
	}
	
	@Override
	public boolean visitVariableNode(VariableNode node) {

		if (node.getParent().isArray()) {
			CompositeNode parent = (CompositeNode) node.getParent();
			if (parent.getChildren().indexOf(node) == 0) {
				_serialized.append("{");
			}
		}

		String commonProperties = this.commonProperties(node);

		PhysicalQuantity quantity = node.consumeFirstValue();

		if (quantity != null) {
			AValue value = quantity.getValue();
			String unit = null, scale = null;

			if (quantity.getUnit() != null) {
				unit = "\"" + quantity.getUnit() + "\"";
			}
			if (quantity.getScalingFactor() != null) {
				scale = "\"" + quantity.getScalingFactor() + "\"";
			}
			_serialized.append("\"" + node.getId() + "\":{\"value\":" + value
					+ ",\"unit\":" + unit + ",\"scale\":" + scale + ","
					+ commonProperties + "},");
		} else {
			_serialized.append("\"" + node.getId() + "\":{"
					+ commonProperties + "},");
		}
		return super.visitVariableNode(node);
	}

	@Override
	public boolean visitParameterNode(ParameterNode node) {
		String commonProperties = this.commonProperties(node);

		String properties = "";

		if (node.getProperties().size() > 0) {
			HashMap<String, String> props = node.getProperties();

			properties = ",\"properties\":{";

			Set<String> keys = props.keySet();
			int index = 0;
			for (String key : keys) {
				index++;
				properties = properties.concat("\"" + key + "\":\""
						+ props.get(key) + "\"");
				if (index < (props.size() - 1)) {
					properties = properties.concat(",");
				}
			}

			properties = properties.concat("},");
		}
		_serialized.append("\"" + node.getId() + "\":{" + commonProperties
				+ properties + "},");

		return super.visitParameterNode(node);
	}

	public boolean visitDynamicsSpecificationNode(DynamicsSpecificationNode node) {
		String commonProperties = this.commonProperties(node);

		PhysicalQuantity quantity = node.getInitialConditions();
		FunctionNode functionNode = node.getDynamics();

		String specs = "", function = "";

		if (quantity != null) {
			AValue value = quantity.getValue();
			String unit = null, scale = null;

			if (quantity.getUnit() != null) {
				unit = quantity.getUnit();
			}
			if (quantity.getScalingFactor() != null) {
				scale = "\"" + quantity.getScalingFactor() + "\"";
			}
			specs = "\"value\":" + "\"" + value + "\",\"unit\":" + "\"" + unit
					+ "\",\"scale\":" + scale + ",";
		}

		if (functionNode != null) {

			String properties = "";

			if (functionNode.getArgument() != null) {
				List<String> arguments = functionNode.getArgument();

				properties = "," + "\"arguments\":{";

				for (int index = 0; index < arguments.size(); index++) {
					properties = properties.concat("\"" + index + "\":\""
							+ arguments.get(index) + "\"");
					if (index < (arguments.size() - 1)) {
						properties = properties.concat(",");
					}
				}

				properties = properties.concat("}");
			}

			function = "\"_function\":{" + "\"expression\":" + "\""
					+ functionNode.getExpression() + "\"" + properties + "},";
		}

		_serialized.append("\"" + node.getId() + "\":{" + specs + function
				+ commonProperties + "},");

		return super.visitDynamicsSpecificationNode(node);
	}

	public boolean visitParameterSpecificationNode(
			ParameterSpecificationNode node) {
		String commonProperties = this.commonProperties(node);

		PhysicalQuantity quantity = node.getValue();

		if (quantity != null) {
			AValue value = quantity.getValue();
			String unit = null, scale = null;

			if (quantity.getUnit() != null) {
				unit = "\"" + quantity.getUnit() + "\"";
			}
			if (quantity.getScalingFactor() != null) {
				scale = "\"" + quantity.getScalingFactor() + "\"";
			}
			_serialized.append("\"" + node.getId() + "\":{\"value\":" + "\""
					+ value + "\",\"unit\":" + unit + ",\"scale\":" + scale
					+ "," + commonProperties + "},");
		} else {
			_serialized.append("\"" + node.getId() + "\":{"
					+ commonProperties + "},");
		}

		return super.visitParameterSpecificationNode(node);
	}

	public boolean visitFunctionNode(FunctionNode node) {
		String commonProperties = this.commonProperties(node);

		String properties = "";

		if (node.getArgument() != null) {
			List<String> arguments = node.getArgument();

			properties = "\"arguments\":{";

			for (int index = 0; index < arguments.size(); index++) {
				properties = properties.concat("\"" + index + "\":\""
						+ arguments.get(index) + "\"");
				if (index < (arguments.size() - 1)) {
					properties = properties.concat(",");
				}
			}

			properties = properties.concat("},");
		}

		_serialized.append("\"" + node.getId() + "\":{" + "\"expression\":"
				+ "\"" + node.getExpression() + "\"," + properties
				+ commonProperties + "},");

		return super.visitFunctionNode(node);
	}

	@Override
	public boolean visitTextMetadataNode(TextMetadataNode node) {
		String commonProperties = this.commonProperties(node);
		
		String valueString = "";
		if (node.getValue() != null){
			AValue value = node.getValue();
			valueString = "\"value\":" + "\"" + value + "\",";
		}

		_serialized.append("\"" + node.getId() + "\":{" + valueString.replaceAll("[\n\r]", "") + commonProperties+ "},");

		return super.visitTextMetadataNode(node);
	}

	@Override
	public boolean visitURLMetadataNode(URLMetadataNode node) {
		String metaType = "";
		if (node.getMetaType() != null) {
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		_serialized.append("\"" + node.getId() + "\":{" + metaType + "},");

		return super.visitURLMetadataNode(node);
	}

	@Override
	public boolean visitSphereNode(SphereNode node) {
		String id = "";
		if (node.getId() != null) {
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}
		
		String metaType = "";
		if (node.getMetaType() != null) {
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if (position != null) {
			positionString = "\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + "," + "\"z\":"
					+ position.getZ().toString() + "";
		}

		Double radius = node.getRadius();
		String radiusString = null;
		if (radius != null) {
			radiusString = "\"" + radius.toString() + "\"";
		}

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString
				+ "}," + "\"radius\":" + radiusString + ","+ id + metaType
				+ "},");

		return super.visitSphereNode(node);
	}

	@Override
	public boolean visitCylinderNode(CylinderNode node) {
		String id = "";
		if (node.getId() != null) {
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}
		
		String metaType = "";
		if (node.getMetaType() != null) {
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if (position != null) {
			positionString = "\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + "," + "\"z\":"
					+ position.getZ().toString() + "";
		}

		Point distal = node.getDistal();
		String distalString = "";

		if (distal != null) {
			distalString = "\"x\":" + distal.getX().toString() + "," + "\"y\":"
					+ distal.getY().toString() + "," + "\"z\":"
					+ distal.getZ().toString() + "";
		}

		Double radiusBottom = node.getRadiusBottom();
		String radiusBottomString = "";
		if (radiusBottom != null) {
			radiusBottomString = "\"" + radiusBottom.toString() + "\"";
		}

		Double radiusTop = node.getRadiusTop();
		String radiusTopString = "";
		if (radiusTop != null) {
			radiusTopString = "\"" + radiusTop.toString() + "\"";
		}

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString
				+ "}," + "\"distal\":{" + distalString + "},"
				+ "\"radiusBottom\":" + radiusBottomString + ","
				+ "\"radiusTop\":" + radiusTopString + "," + id + metaType
				+ "},");

		return super.visitCylinderNode(node);
	}

	@Override
	public boolean visitParticleNode(ParticleNode node) {
		String id = "";
		if (node.getId() != null) {
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}
		
		String metaType = "";
		if (node.getMetaType() != null) {
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if (position != null) {
			positionString = "\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + "," + "\"z\":"
					+ position.getZ().toString() + "";
		}

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString
				+ "}," + id+ metaType + "},");

		return super.visitParticleNode(node);
	}

	@Override
	public boolean visitColladaNode(ColladaNode node) {
		String metaType = "";
		if (node.getMetaType() != null) {
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if (position != null) {
			positionString = "\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + "," + "\"z\":"
					+ position.getZ().toString() + "";
		}

		String model = "";
		if (node.getModel() != null) {
			JsonObject obj = new JsonObject();
			obj.addProperty("data", node.getModel());
			model = "\"model\":" + obj.toString() + ",";
		}

		String id = "";
		if (node.getId() != null) {
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString
				+ "}," + model + id + metaType + "},");

		return super.visitColladaNode(node);
	}

	@Override
	public boolean visitObjNode(OBJNode node) {
		String metaType = "";
		if (node.getMetaType() != null) {
			metaType = "\"_metaType\":" + "\"" + node.getMetaType() + "\"";
		}

		String id = "";
		if (node.getId() != null) {
			id = "\"id\":" + "\"" + node.getId() + "\",";
		}

		Point position = node.getPosition();
		String name = node.getId();
		String positionString = "";

		if (position != null) {
			positionString = "\"x\":" + position.getX().toString() + ","
					+ "\"y\":" + position.getY().toString() + "," + "\"z\":"
					+ position.getZ().toString() + "";
		}

		String model = "";
		if (node.getModel() != null) {
			JsonObject obj = new JsonObject();
			obj.addProperty("data", node.getModel());
			model = "\"model\":" + obj.toString() + ",";
		}

		_serialized.append("\"" + name + "\":{\"position\":{" + positionString
				+ "}," + model + id + metaType + "},");

		return super.visitObjNode(node);
	}

	public String getSerializedTree() {
		if (_serialized.charAt(_serialized.length() - 1) == ',')
			_serialized.deleteCharAt(_serialized.lastIndexOf(","));
		return _serialized.toString();
	}
}
