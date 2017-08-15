

package org.geppetto.core.data.model.local;

import org.geppetto.core.data.model.ISimulationResult;
import org.geppetto.core.data.model.ResultsFormat;

public class LocalSimulationResult implements ISimulationResult
{
	private long id;

	private String simulatedInstance;

	private LocalPersistedData result;

	private ResultsFormat format;

	public LocalSimulationResult(long id, String simulatedInstance, LocalPersistedData result, ResultsFormat format)
	{
		this.id = id;
		this.simulatedInstance = simulatedInstance;
		this.result = result;
		this.format = format;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public LocalPersistedData getResult()
	{
		return result;
	}

	@Override
	public String getSimulatedInstance()
	{
		return simulatedInstance;
	}

	@Override
	public void setId(long id)
	{
		this.id = id;
	}

	@Override
	public ResultsFormat getFormat()
	{
		return this.format;
	}

}