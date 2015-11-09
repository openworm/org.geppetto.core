/**
 * 
 */
package org.geppetto.core.model.typesystem.values;

/**
 * @author matteocantarelli
 *
 */
public class FloatValue extends AValue
{

	private float _value;

	public FloatValue(float value)
	{
		_value=value;
	}

	@Override
	public String getStringValue()
	{
		return Float.toString(_value);
	}
	
	public double getAsDouble()
	{
		return Float.valueOf(_value).doubleValue();
	}

	public float getAsFloat()
	{
		return _value;
	}
	
}
