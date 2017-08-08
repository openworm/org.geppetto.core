
package org.geppetto.core.simulator;

import java.util.Map;

public class RemoteSimulatorConfig extends ExternalSimulatorConfig
{

	private String username;
	private String password;
	private Map<String ,String> simulatorParameters;
	
	
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public Map<String, String> getSimulatorParameters()
	{
		return simulatorParameters;
	}
	public void setSimulatorParameters(Map<String, String> simulatorParameters)
	{
		this.simulatorParameters = simulatorParameters;
	}
	

}
