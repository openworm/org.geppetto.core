package org.geppetto.core.manager;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.geppetto.core.common.GeppettoAccessException;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.ResultsFormat;
import org.geppetto.model.ModelFormat;

public interface IDownloadManager
{

	/**
	 * Writes the model enclosed in a given aspect for a specified format supported by a converter service to a string.
	 * 
	 * @param aspectInstancePath
	 * @param format
	 * @return
	 * @throws GeppettoAccessException 
	 */
	public abstract File downloadModel(String aspectInstancePath, ModelFormat format, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * Writes the model enclosed in a given aspect for a specified format supported by a converter service to a string.
	 * 
	 * @param aspectInstancePath
	 * @return
	 * @throws GeppettoAccessException 
	 */
	public abstract List<ModelFormat> getSupportedOuputs(String aspectInstancePath, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

	/**
	 * @param aspectPath
	 * @param resultsFormat
	 * @return
	 * @throws GeppettoExecutionException
	 * @throws GeppettoAccessException 
	 */
	public abstract URL downloadResults(String aspectPath, ResultsFormat resultsFormat, IExperiment experiment, IGeppettoProject project) throws GeppettoExecutionException, GeppettoAccessException;

}