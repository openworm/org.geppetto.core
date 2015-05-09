package org.geppetto.core.manager;

import org.geppetto.core.ModelFormat;
import org.geppetto.core.simulation.ResultsFormat;

public interface IDropBoxManager
{

	/**
	 * Link the user dropbox account with the geppetto account
	 */
	public abstract void linkDropBoxAccount();

	/**
	 * Upload the model associated with the given aspect of the active experiment to the dropbox folder
	 * @param aspectID
	 * @param format
	 */
	public abstract void uploadModelToDropBox(String aspectID, ModelFormat format);

	/**
	 * @param aspectID
	 * @param format
	 */
	public abstract void uploadResultsToDropBox(String aspectID, ResultsFormat format);

}