/**
 * 
 */
package org.geppetto.core.model.typesystem.values;


/**
 * @author matteocantarelli
 *
 */
public abstract class AValue implements IValue
{	

	public abstract String getStringValue();

	@Override
	public String toString()
	{
		return getStringValue();
	}
	
}
