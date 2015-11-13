package org.geppetto.core.manager;

import java.util.List;
import java.util.Map;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;

public interface IRuntimeTreeManager
{

	/**
	 * Returns the model tree corresponding to the specified aspect
	 * 
	 * @param aspectID
	 * @return
	 * @throws GeppettoExecutionException
	 */
	public Map<String, AspectSubTreeNode> getModelTree(String aspectInstancePath, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException;

	/**
	 * Returns the simulation tree corresponding to the specified aspect
	 * 
	 * @param aspectInstancePath
	 * @return
	 */
	public abstract Map<String, AspectSubTreeNode> getSimulationTree(String aspectInstancePath, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException;;

	/**
	 * Changes the parameters of the model
	 * 
	 * @param aspectInstancePath
	 * @param parameters
	 */
	public abstract AspectSubTreeNode setModelParameters(String aspectInstancePath, Map<String, String> parameters, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException;;

	/**
	 * Sets variables to be watched
	 * 
	 * @throws GeppettoExecutionException
	 * @throws GeppettoInitializationException
	 * */
	void setWatchedVariables(List<String> watchedVariables, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException;

	/**
	 * @throws GeppettoExecutionException
	 * 
	 */
	public abstract void clearWatchLists(IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException;

}