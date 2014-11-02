package org.geppetto.core.model.runtime;

import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.quantities.Quantity;
import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * Element of Visual Group used to store individual node properties 
 * not shared with rest of the nodes in the same group.
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 */
public class VisualGroupElementNode extends ANode{

	private String _defaultColor;
	private PhysicalQuantity _parameter;
	
	public VisualGroupElementNode(String id) {
		super(id);
	}

	public String getDefaultColor(){
		return this._defaultColor;
	}
	
	public void setDefaultColor(String defaultColor){
		this._defaultColor = defaultColor;
	}
	
	public PhysicalQuantity getParameter(){
		return this._parameter;
	}
	
	public void setParameter(PhysicalQuantity parameter){
		this._parameter = parameter;
	}
	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitVisualGroupElementNode(this);
	}
}
