package org.geppetto.core.manager;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.ResultsFormat;
import org.geppetto.core.services.ModelFormat;

public interface IDropBoxManager
{

	/**
	 * Link the user dropbox account with the geppetto account
	 * @throws GeppettoExecutionException 
	 * @throws DbxException 
	 */
	public abstract void linkDropBoxAccount(String key) throws Exception;
	
	/**
	 * Unlink the user dropbox account from the geppetto account
	 * @throws GeppettoExecutionException 
	 */
	public abstract void unlinkDropBoxAccount(String key) throws Exception;

	/**
	 * Upload the model associated with the given aspect of the active experiment to the dropbox folder
	 * @param aspectID
	 * @param format
	 * @throws GeppettoExecutionException 
	 * @throws Exception 
	 */
	public abstract void uploadModelToDropBox(String aspectID, IExperiment experiment, IGeppettoProject project, ModelFormat format) throws Exception;

	/**
	 * @param aspectID
	 * @param format
	 * @throws GeppettoExecutionException 
	 */
	public abstract void uploadResultsToDropBox(String aspectID, IExperiment experiment, IGeppettoProject project, ResultsFormat format) throws GeppettoExecutionException;
}