package org.geppetto.core.model.runtime;

import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * Node use to define a collada object for visualization and serialization
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class ColladaNode extends AVisualObjectNode{

	private String _model;
	
	public String getModel() {
		return _model;
	}

	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitColladaNode(this);
	}

	public void setModel(String model) {
		this._model = model;
	}

}
