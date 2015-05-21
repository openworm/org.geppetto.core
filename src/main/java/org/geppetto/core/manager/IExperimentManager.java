package org.geppetto.core.manager;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;

public interface IExperimentManager
{

	/**
	 * @param requestId
	 * @param experiment
	 * @return 
	 * @throws GeppettoExecutionException
	 */
	public abstract RuntimeTreeRoot loadExperiment(String requestId, IExperiment experiment) throws GeppettoExecutionException;

	/**
	 * Run a specified experiment
	 * 
	 * @param requestId
	 * @param user
	 * @param experiment
	 * @throws GeppettoInitializationException
	 * @throws GeppettoExecutionException 
	 */
	public abstract void runExperiment(String requestId, IExperiment experiment) throws GeppettoExecutionException;

	/**
	 * Create a new experiment inside a given project
	 * 
	 * @param requestId
	 * @param project
	 * @return
	 */
	public abstract IExperiment newExperiment(String requestId, IGeppettoProject project);
	
	
	/**
	 * Delete a specified experiment
	 * 
	 * @param requestId
	 * @param user
	 * @param experiment
	 */
	void deleteExperiment(String requestId, IExperiment experiment);
	

	/**
	 * @param requestId
	 * @param experiment
	 * @param project
	 */
	void cancelExperimentRun(String requestId, IExperiment experiment);
}