/**
 * 
 */
package org.geppetto.core.model.typesystem.values;

import org.geppetto.core.model.typesystem.ANode;

/**
 * @author matteocantarelli
 *
 */
public abstract class AValue extends ANode implements IValue 
{

	public abstract String getStringValue();

	@Override
	public String toString()
	{
		return getStringValue();
	}

}
