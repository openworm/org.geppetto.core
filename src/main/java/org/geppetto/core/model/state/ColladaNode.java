package org.geppetto.core.model.state;

import org.geppetto.core.model.state.visitors.IStateVisitor;

public class ColladaNode extends AVisualObjectNode{

	private String model;
	
	public String getModel() {
		return model;
	}

	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitColladaNode(this);
	}

	public void setModel(String model) {
		this.model = model;
	}

}
