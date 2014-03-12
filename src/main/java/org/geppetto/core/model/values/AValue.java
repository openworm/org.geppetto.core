/**
 * 
 */
package org.geppetto.core.model.values;

/**
 * @author matteocantarelli
 *
 */
public abstract class AValue<Unit>
{
	private String _unit;
	private String _scalingFactor;
	
	public abstract String getStringValue();

	@Override
	public String toString()
	{
		return getStringValue();
	}
	
	public void setUnit(String unit){
		this._unit = unit;
	}
	
	public String getUnit(){
		return _unit;
	}
	
	public String getScalingFactor(){
		return _scalingFactor;
	}
	
	public void setScalingFactor(String scalingFactor){
		this._scalingFactor = scalingFactor;
	}
}
