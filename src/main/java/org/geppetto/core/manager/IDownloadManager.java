package org.geppetto.core.manager;

import java.io.File;

import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.services.IModelFormat;
import org.geppetto.core.simulation.ResultsFormat;

public interface IDownloadManager
{

	/**
	 * Writes the model enclosed in a given aspect for a specified format supported
	 * by a converter service to a string.
	 * 
	 * @param aspectInstancePath
	 * @param format
	 * @return
	 */
	public abstract File downloadModel(String aspectInstancePath, IModelFormat format, IExperiment experiment, IGeppettoProject project);

	/**
	 * @param resultsFormat
	 * @return
	 */
	public abstract File downloadResults(ResultsFormat resultsFormat, IExperiment experiment, IGeppettoProject project);

}