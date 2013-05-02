/**
 * 
 */
package org.geppetto.core.model.values;

/**
 * @author matteocantarelli
 *
 */
public abstract class AValue
{
	public abstract String getStringValue();

	@Override
	public String toString()
	{
		return getStringValue();
	}
	
	
}
