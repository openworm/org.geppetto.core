package org.geppetto.core.manager;

import java.util.Map;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.model.typesystem.Root;

public interface IExperimentManager
{

	/**
	 * @param requestId
	 * @param experiment
	 * @return
	 * @throws GeppettoExecutionException
	 */
	public abstract Root loadExperiment(String requestId, IExperiment experiment) throws GeppettoExecutionException;

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
	 * @param requestId
	 * @param experiment
	 * @return
	 */
	Map<String, AspectSubTreeNode> playExperiment(String requestId, IExperiment experiment) throws GeppettoExecutionException;

	/**
	 * Create a new experiment inside a given project
	 * 
	 * @param requestId
	 * @param project
	 * @return
	 * @throws GeppettoExecutionException
	 */
	public abstract IExperiment newExperiment(String requestId, IGeppettoProject project) throws GeppettoExecutionException;

	/**
	 * Delete a specified experiment
	 * 
	 * @param requestId
	 * @param user
	 * @param experiment
	 * @throws GeppettoExecutionException
	 */
	void deleteExperiment(String requestId, IExperiment experiment) throws GeppettoExecutionException;

	/**
	 * @param requestId
	 * @param experiment
	 * @param project
	 */
	void cancelExperimentRun(String requestId, IExperiment experiment);

}