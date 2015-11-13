/**
 * 
 */
package org.geppetto.core.model.typesystem.values;

/**
 * @author matteocantarelli
 *
 */
public class StringValue extends AValue
{

	protected String value;

	public StringValue(String value)
	{
		super();
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.values.AValue#getStringValue()
	 */
	@Override
	public String getStringValue()
	{
		return value;
	}

}
