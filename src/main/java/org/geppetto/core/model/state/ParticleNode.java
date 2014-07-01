package org.geppetto.core.model.state;

import org.geppetto.core.model.state.visitors.IStateVisitor;

public class ParticleNode extends AVisualObjectNode{
    
	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitParticleNode(this);
	}

}
