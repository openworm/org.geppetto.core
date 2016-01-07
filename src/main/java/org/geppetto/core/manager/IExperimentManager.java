package org.geppetto.core.manager;

import java.util.List;

import org.geppetto.core.common.GeppettoAccessException;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.model.ExperimentState;

public interface IExperimentManager
{

	/**
	 * @param requestId
	 * @param experiment
	 * @return
	 * @throws GeppettoExecutionException
	 * @throws GeppettoAccessException 
	 */
	public abstract ExperimentState loadExperiment(String requestId, IExperiment experiment) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * Run a specified experiment
	 * 
	 * @param requestId
	 * @param user
	 * @param experiment
	 * @throws GeppettoInitializationException
	 * @throws GeppettoExecutionException
	 * @throws GeppettoAccessException 
	 */
	public abstract void runExperiment(String requestId, IExperiment experiment) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * @param requestId
	 * @param experiment
	 * @return a map containing the value for each recorded variable identified by a pointer
	 * @throws GeppettoExecutionException
	 * @throws GeppettoAccessException 
	 */
	public abstract ExperimentState playExperiment(String requestId, IExperiment experiment, List<String> filter) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * Create a new experiment inside a given project
	 * 
	 * @param requestId
	 * @param project
	 * @return
	 * @throws GeppettoExecutionException
	 * @throws GeppettoAccessException 
	 */
	public abstract IExperiment newExperiment(String requestId, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * Delete a specified experiment
	 * 
	 * @param requestId
	 * @param user
	 * @param experiment
	 * @throws GeppettoExecutionException
	 * @throws GeppettoAccessException 
	 */
	void deleteExperiment(String requestId, IExperiment experiment) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * @param requestId
	 * @param experiment
	 * @param project
	 * @throws GeppettoExecutionException 
	 */
	void cancelExperimentRun(String requestId, IExperiment experiment) throws GeppettoExecutionException;

}