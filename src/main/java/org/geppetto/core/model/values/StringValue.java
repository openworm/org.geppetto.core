/**
 * 
 */
package org.geppetto.core.model.values;

/**
 * @author matteocantarelli
 *
 */
public class StringValue extends AValue
{

	private String _value;

	public StringValue(String value)
	{
		super();
		this._value = value;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.model.values.AValue#getStringValue()
	 */
	@Override
	public String getStringValue()
	{
		return _value;
	}

}
