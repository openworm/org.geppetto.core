

package org.geppetto.core.data.model.local;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.data.model.IParameter;

public class LocalAspectConfiguration implements IAspectConfiguration
{
	private long id;

	private String instance = null;

	private List<String> watchedVariables = new ArrayList<String>();

	private List<LocalParameter> modelParameters = new ArrayList<LocalParameter>();

	private LocalSimulatorConfiguration simulatorConfiguration = new LocalSimulatorConfiguration(id, null, null, id, id, null);

	public LocalAspectConfiguration(long id, String instance, List<String> watchedVariables, List<LocalParameter> modelParameter, LocalSimulatorConfiguration simulatorConfiguration)
	{
		this.id = id;
		this.instance = instance;
		this.watchedVariables = watchedVariables;
		this.modelParameters = modelParameter;
		this.simulatorConfiguration = simulatorConfiguration;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public String getInstance()
	{
		return instance;
	}

	@Override
	public List<String> getWatchedVariables()
	{
		return watchedVariables;
	}

	@Override
	public List<LocalParameter> getModelParameter()
	{
		return modelParameters;
	}

	@Override
	public LocalSimulatorConfiguration getSimulatorConfiguration()
	{
		return simulatorConfiguration;
	}

	@Override
	public void addModelParameter(IParameter modelParameter)
	{
		if(modelParameters == null)
		{
			modelParameters = new ArrayList<LocalParameter>();
		}
		if(modelParameter instanceof LocalParameter)
		{
			modelParameters.add((LocalParameter) modelParameter);
		}
	}

	@Override
	public void setId(long id)
	{
		id = id;
	}

}