/**
 * 
 */
package org.geppetto.core.model.typesystem.values;

/**
 * @author matteocantarelli
 *
 */
public class IntValue extends AValue
{

	private int _value;

	public IntValue(int value)
	{
		_value=value;
	}

	@Override
	public String getStringValue()
	{
		return Integer.toString(_value);
	}
	
	public int getAsInt()
	{
		return _value;
	}
	
}
