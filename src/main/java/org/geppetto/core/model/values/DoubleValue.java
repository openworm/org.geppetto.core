/**
 * 
 */
package org.geppetto.core.model.values;

/**
 * @author matteocantarelli
 *
 */
public class DoubleValue extends AValue
{

	private double _value;

	public DoubleValue(double value)
	{
		_value=value;
	}

	@Override
	public String getStringValue()
	{
		return Double.toString(_value);
	}
	
	public double getValue()
	{
		return _value;
	}
	
}
