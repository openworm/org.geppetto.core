/**
 * 
 */
package org.geppetto.core.model.typesystem.values;

/**
 * @author matteocantarelli
 *
 */
public class ValuesFactory
{

	public static DoubleValue getDoubleValue(double value)
	{
		return new DoubleValue(value);
	}
	
	public static FloatValue getFloatValue(float value)
	{
		return new FloatValue(value);
	}
	
	public static StringValue getStringValue(String value)
	{
		return new StringValue(value);
	}

	public static AValue getIntValue(int value)
	{
		return new IntValue(value);
	}
}
