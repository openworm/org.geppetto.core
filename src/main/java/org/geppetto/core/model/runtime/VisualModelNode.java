package org.geppetto.core.model.runtime;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * Node use to define a model for visualization and serialization
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class VisualModelNode extends ACompositeNode{

    private List<AVisualObjectNode> _models = new ArrayList<AVisualObjectNode>();
    private String _id;
    
	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	@Override
	public synchronized boolean apply(IStateVisitor visitor)
	{
		if (visitor.inVisualModelNode(this))  // enter this node?
		{
			for(ANode stateNode:this.getChildren())
			{
				stateNode.apply(visitor);
				if(visitor.stopVisiting())
				{
					break;
				}
			}
		}
		return visitor.outVisualModelNode( this );
	}

	public List<AVisualObjectNode> getObjects() {
		return this._models;
	}
}
