
package org.geppetto.core.beans;

/**
 * @author Jesus Martinez (jesus@metacell.us)
 *
 */
public class SimulatorConfig
{

	private String simulatorName;
	private String simulatorID;

	public void setSimulatorName(String simulatorName)
	{
		this.simulatorName = simulatorName;
	}

	public String getSimulatorName()
	{
		return this.simulatorName;
	}

	public String getSimulatorID()
	{
		return this.simulatorID;
	}

	public void setSimulatorID(String simulatorID)
	{
		this.simulatorID = simulatorID;
	}
}
