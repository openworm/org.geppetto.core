package org.geppetto.core.manager;

import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.services.ModelFormat;
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
	public abstract void uploadModelToDropBox(String aspectID, IExperiment experiment, IGeppettoProject project, ModelFormat format);

	/**
	 * @param aspectID
	 * @param format
	 */
	public abstract void uploadResultsToDropBox(String aspectID, IExperiment experiment, IGeppettoProject project, ResultsFormat format);

}