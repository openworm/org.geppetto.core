/**
 * 
 */
package org.geppetto.core.model.values;

import org.geppetto.core.model.state.AStateNode;

/**
 * @author matteocantarelli
 *
 */
public abstract class AValue
{	
	private AStateNode parentNode;

	public abstract String getStringValue();

	@Override
	public String toString()
	{
		return getStringValue();
	}
	
	public AStateNode getParentNode(){
		return this.parentNode;
	}
	
	public void setParentNode(AStateNode parentNode){
		this.parentNode = parentNode;
	}
}
