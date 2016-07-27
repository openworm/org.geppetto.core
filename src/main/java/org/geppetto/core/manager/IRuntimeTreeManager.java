package org.geppetto.core.manager;


import java.util.List;
import java.util.Map;

import org.geppetto.core.common.GeppettoAccessException;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.model.ExperimentState;
import org.geppetto.model.GeppettoModel;

public interface IRuntimeTreeManager
{

	/**
	 * Changes the parameters of the model
	 * 
	 * @param aspectInstancePath
	 * @param parameters
	 * @throws GeppettoAccessException 
	 */
	ExperimentState setModelParameters(Map<String, String> parameters, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * Sets variables to be watched
	 * @return 
	 * 
	 * @throws GeppettoExecutionException
	 * @throws GeppettoAccessException 
	 * @throws GeppettoInitializationException
	 * */
	ExperimentState setWatchedVariables(List<String> watchedVariables, IExperiment experiment, IGeppettoProject project, boolean watch) throws GeppettoExecutionException, GeppettoAccessException;
	
	/**
	 * @param typePaths
	 * @param experiment
	 * @param geppettoProject
	 * @return
	 * @throws GeppettoExecutionException 
	 */
	GeppettoModel resolveImportType(List<String> typePaths, IGeppettoProject geppettoProject) throws GeppettoExecutionException;

}