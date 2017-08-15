

package org.geppetto.core.simulation;

import org.geppetto.core.data.model.IExperiment;

/**
 * @author matteocantarelli
 * 
 */
public interface IGeppettoManagerCallbackListener
{	
	/**
	 */
	void experimentError(String titleMessage, String logMessage, Exception exception, IExperiment experiment);

}
