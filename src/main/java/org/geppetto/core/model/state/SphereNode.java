package org.geppetto.core.model.state;

import org.geppetto.core.model.state.visitors.IStateVisitor;

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
