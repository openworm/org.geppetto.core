

package org.geppetto.core.simulation;

import java.io.File;
import java.util.Map;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.data.model.ResultsFormat;

/**
 * @author matteocantarelli
 * 
 */
public interface ISimulatorCallbackListener
{

	/**
	 * @param aspectNode
	 *            The aspect that was simulated
	 * @param results
	 *            The file containing the results of the simulation in the Geppetto HDF5 recording format
	 */
	void endOfSteps(IAspectConfiguration aspectConfiguration, Map<File, ResultsFormat> results) throws GeppettoExecutionException;

	void externalProcessFailed(String message, Exception e);
}
