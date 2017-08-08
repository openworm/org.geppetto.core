

package org.geppetto.core.data.model.local;

import java.util.Map;

import org.geppetto.core.data.model.ISimulatorConfiguration;

public class LocalSimulatorConfiguration implements ISimulatorConfiguration
{
	private long id;

	private String simulatorId;

	private String conversionServiceId;

	private float timestep;

	private float length;

	private Map<String, String> parameters;

	public LocalSimulatorConfiguration(long id, String simulatorId, String conversionServiceId, float timestep, float length, Map<String, String> parameters)
	{
		this.id = id;
		this.simulatorId = simulatorId;
		this.conversionServiceId = conversionServiceId;
		this.timestep = timestep;
		this.length = length;
		this.parameters = parameters;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public String getSimulatorId()
	{
		return simulatorId;
	}

	@Override
	public String getConversionServiceId()
	{
		return conversionServiceId;
	}

	@Override
	public float getTimestep()
	{
		return timestep;
	}

	@Override
	public float getLength()
	{
		return length;
	}

	@Override
	public Map<String, String> getParameters()
	{
		return parameters;
	}

	@Override
	public void setLength(float length)
	{
		this.length = length;
	}

	@Override
	public void setTimestep(float timestep)
	{
		this.timestep = timestep;
	}

	@Override
	public void setSimulatorId(String simulatorId)
	{
		this.simulatorId = simulatorId;
	}

	@Override
	public void setConversionServiceId(String conversionServiceId)
	{
		this.conversionServiceId = conversionServiceId;
	}

	@Override
	public void setParameters(Map<String, String> parameters)
	{
		this.parameters = parameters;
	}

	@Override
	public void setId(long id)
	{
		this.id = id;
	}

}
