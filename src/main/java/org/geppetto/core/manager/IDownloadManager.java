package org.geppetto.core.manager;

import java.io.File;

import org.geppetto.core.services.IModelFormat;
import org.geppetto.core.simulation.ResultsFormat;

public interface IDownloadManager
{

	/**
	 * Writes the model enclosed in a given aspect for a specified format supported
	 * by a converter service to a string.
	 * 
	 * @param aspectID
	 * @param format
	 * @return
	 */
	public abstract File downloadModel(String aspectID, IModelFormat format);

	/**
	 * @param resultsFormat
	 * @return
	 */
	public abstract File downloadResults(ResultsFormat resultsFormat);

}