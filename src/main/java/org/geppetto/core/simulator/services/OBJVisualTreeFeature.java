package org.geppetto.core.simulator.services;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.features.IVisualTreeFeature;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.ColladaNode;
import org.geppetto.core.model.runtime.OBJNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.services.GeppettoFeature;

public class OBJVisualTreeFeature implements IVisualTreeFeature{

	GeppettoFeature type = GeppettoFeature.VISUAL_TREE_FEATURE;
	
	@Override
	public GeppettoFeature getType() {
		return type;
	}

	@Override
	public boolean populateVisualTree(AspectNode aspectNode)
			throws ModelInterpreterException {
		OBJNode obj = new OBJNode("OBJ");
		obj.setModel((String) ((ModelWrapper) aspectNode.getModel()).getModel("OBJ"));

		aspectNode.getSubTree(AspectTreeType.VISUALIZATION_TREE).addChild(obj);
		((AspectSubTreeNode) aspectNode.getSubTree(AspectTreeType.VISUALIZATION_TREE)).setModified(true);

		return false;
	}
}
