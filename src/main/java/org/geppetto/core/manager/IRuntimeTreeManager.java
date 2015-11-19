package org.geppetto.core.manager;

import java.util.List;
import java.util.Map;

import org.geppetto.core.common.GeppettoAccessException;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.model.runtime.AspectSubTreeNode;

public interface IRuntimeTreeManager
{

	/**
	 * Returns the model tree corresponding to the specified aspect
	 * 
	 * @param aspectID
	 * @return
	 * @throws GeppettoExecutionException 
	 * @throws GeppettoAccessException 
	 */
	public Map<String, AspectSubTreeNode> getModelTree(String aspectInstancePath, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * Returns the simulation tree corresponding to the specified aspect
	 * 
	 * @param aspectInstancePath
	 * @return
	 * @throws GeppettoAccessException 
	 */
	public abstract Map<String, AspectSubTreeNode> getSimulationTree(String aspectInstancePath, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;;

	/**
	 * Changes the parameters of the model 
	 * @param aspectInstancePath
	 * @param parameters
	 * @throws GeppettoAccessException 
	 */
	public abstract AspectSubTreeNode setModelParameters(String aspectInstancePath, Map<String, String> parameters, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;;

	
	/**
	 * Sets variables to be watched
	 * @throws GeppettoExecutionException 
	 * @throws GeppettoAccessException 
	 * @throws GeppettoInitializationException 
	 * */
	void setWatchedVariables(List<String> watchedVariables, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

	
	/**
	 * @throws GeppettoExecutionException 
	 * @throws GeppettoAccessException 
	 * 
	 */
	public abstract void clearWatchLists(IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

}