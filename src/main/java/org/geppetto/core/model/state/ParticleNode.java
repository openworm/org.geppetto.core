package org.geppetto.core.model.state;

import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * Node use to define a particle for visualization and serialization
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class ParticleNode extends AVisualObjectNode{
    
	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitParticleNode(this);
	}

}
