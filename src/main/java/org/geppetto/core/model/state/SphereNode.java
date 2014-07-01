package org.geppetto.core.model.state;

import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * Node use to define a sphere for visualization and serialization
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class SphereNode extends AVisualObjectNode{

    private Double radius;
    
	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitSphereNode(this);
	}

}
