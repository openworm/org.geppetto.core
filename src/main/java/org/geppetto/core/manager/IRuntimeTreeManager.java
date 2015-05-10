package org.geppetto.core.manager;

import java.util.List;
import java.util.Map;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.model.runtime.AspectSubTreeNode;

public interface IRuntimeTreeManager
{

	/**
	 * Returns the model tree corresponding to the specified aspect
	 * 
	 * @param aspectID
	 * @return
	 */
	public Map<String, AspectSubTreeNode> getModelTree(String aspectID);

	/**
	 * Returns the simulation tree corresponding to the specified aspect
	 * 
	 * @param aspectID
	 * @return
	 */
	public abstract Map<String, AspectSubTreeNode> getSimulationTree(String aspectID);

	/**
	 * Changes the parameters of the model 
	 * @param aspectInstancePath
	 * @param parameters
	 */
	public abstract Map<String, String> setModelParameters(String aspectInstancePath, Map<String, String> parameters);

	/**
	 * Changes the parameters of the model 
	 * @param aspectInstancePath
	 * @param parameters
	 */
	public abstract Map<String, String> setSimulatorConfiguration(String aspectInstancePath, Map<String, String> parameters);

	
	/**
	 * Sets variables to be watched
	 * @throws GeppettoExecutionException 
	 * @throws GeppettoInitializationException 
	 * */
	void setWatchedVariables(List<String> watchedVariables) throws GeppettoExecutionException, GeppettoInitializationException;

	
	/**
	 * 
	 */
	public abstract void clearWatchLists();

}