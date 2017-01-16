package org.geppetto.core.manager;

import java.net.MalformedURLException;
import java.util.List;

import org.geppetto.core.common.GeppettoAccessException;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;

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
	 * @throws GeppettoAccessException 
	 */
	void loadProject(String requestId, IGeppettoProject project) throws MalformedURLException, GeppettoInitializationException, GeppettoExecutionException, GeppettoAccessException;

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
	 * @throws GeppettoAccessException 
	 */
	void deleteProject(String requestId, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * Saves the current project in the database. Once a project is persisted it will always be updated until it gets deleted
	 * 
	 * @param requestId
	 * @param user
	 * @param project
	 * @throws GeppettoExecutionException
	 * @throws GeppettoAccessException 
	 */
	void persistProject(String requestId, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * @param requestId
	 * @param project
	 * @return
	 */
	List<? extends IExperiment> checkExperimentsStatus(String requestId, IGeppettoProject project);
	
	void makeProjectPublic(String requestId, IGeppettoProject project,boolean isPublic) throws GeppettoExecutionException,GeppettoAccessException;

}