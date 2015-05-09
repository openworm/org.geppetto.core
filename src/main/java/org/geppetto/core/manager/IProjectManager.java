package org.geppetto.core.manager;

import java.net.MalformedURLException;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.simulation.IGeppettoManagerCallbackListener;

/**
 * @author matteocantarelli
 *
 */
public interface IProjectManager
{

	/**
	 * Loads a Geppetto project
	 * 
	 * @param requestId
	 * @param project
	 * @param listener
	 * @throws MalformedURLException
	 * @throws GeppettoInitializationException
	 * @throws GeppettoExecutionException
	 */
	void loadProject(String requestId, IGeppettoProject project, IGeppettoManagerCallbackListener listener) throws MalformedURLException, GeppettoInitializationException,
			GeppettoExecutionException;

	/**
	 * Closes a Geppetto project
	 * 
	 * @param requestId
	 * @param project
	 * @throws GeppettoExecutionException
	 */
	void closeProject(String requestId, IGeppettoProject project) throws GeppettoExecutionException;
	
	/**
	 * @param requestId
	 * @param project
	 * @throws GeppettoExecutionException
	 */
	void deleteProject(String requestId, IGeppettoProject project) throws GeppettoExecutionException;
	
	/**
	 * Saves the current project in the database. Once a project is persisted it will always
	 * be updated until it gets deleted
	 * 
	 * @param requestId
	 * @param user
	 * @param project
	 */
	void persistProject(String requestId, IGeppettoProject project);

}