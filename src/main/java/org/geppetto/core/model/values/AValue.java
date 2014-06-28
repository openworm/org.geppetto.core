/**
 * 
 */
package org.geppetto.core.model.values;

import org.geppetto.core.model.state.ANode;

/**
 * @author matteocantarelli
 *
 */
public abstract class AValue
{	
	private ANode parentNode;

	public abstract String getStringValue();

	@Override
	public String toString()
	{
		return getStringValue();
	}
	
	public ANode getParentNode(){
		return this.parentNode;
	}
	
	public void setParentNode(ANode parentNode){
		this.parentNode = parentNode;
	}
}
