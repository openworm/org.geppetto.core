package org.geppetto.core.manager;

import java.util.List;
import java.util.Map;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.model.ExperimentState;

public interface IRuntimeTreeManager
{

	/**
	 * Changes the parameters of the model
	 * 
	 * @param aspectInstancePath
	 * @param parameters
	 */
	ExperimentState setModelParameters(Map<String, String> parameters, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException;;

	/**
	 * Sets variables to be watched
	 * 
	 * @throws GeppettoExecutionException
	 * @throws GeppettoInitializationException
	 * */
	void setWatchedVariables(List<String> watchedVariables, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException;

}