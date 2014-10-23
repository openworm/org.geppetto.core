package org.geppetto.core.model.runtime;

import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class VisualObjectReferenceNode extends ANode{

	private String _aspectInstancePath;
	private String _visualObjectId;
	
	public void setAspectInstancePath(String path){
		this._aspectInstancePath = path;
	}
	
	public String getAspectInstancePath(){
		return this._aspectInstancePath;
	}
	
	public void setVisualObjectId(String id){
		this._visualObjectId = id;
	}
	
	public String getVisualObjectId(){
		return this._visualObjectId;
	}
	public VisualObjectReferenceNode(String id) {
		super(id);
	}

	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitVisualObjectReferenceNode(this);
	}
}
