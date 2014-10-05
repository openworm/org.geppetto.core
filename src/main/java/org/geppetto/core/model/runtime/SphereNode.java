package org.geppetto.core.model.runtime;

import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * Node use to define a sphere for visualization and serialization
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class SphereNode extends AVisualObjectNode{

    private Double _radius;
    
	public SphereNode(String id)
	{
		super(id);
	}

	public Double getRadius() {
		return _radius;
	}

	public void setRadius(Double radius) {
		this._radius = radius;
	}

	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitSphereNode(this);
	}

}
