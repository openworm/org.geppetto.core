package org.geppetto.core.manager;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;

public interface IExperimentManager
{

	/**
	 * @param requestId
	 * @param experiment
	 * @throws GeppettoExecutionException
	 */
	public abstract void loadExperiment(String requestId, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException;

	/**
	 * Run a specified experiment
	 * 
	 * @param requestId
	 * @param user
	 * @param experiment
	 * @throws GeppettoInitializationException
	 */
	public abstract void runExperiment(String requestId, IExperiment experiment, IGeppettoProject project) throws GeppettoInitializationException;

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
	void deleteExperiment(String requestId, IExperiment experiment, IGeppettoProject project);
	

}