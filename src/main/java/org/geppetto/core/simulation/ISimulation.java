/**
 * 
 */
package org.geppetto.core.simulation;

import java.net.URL;

/**
 * @author matteocantarelli
 *
 */
public interface ISimulation {

	/**
	 * @param simConfigURL
	 * @param simulationListener 
	 */
	void init(URL simConfigURL, ISimulationCallbackListener simulationListener);
	
	/**
	 * 
	 */
	void start();
	
	/**
	 * 
	 */
	void stop();
	
	/**
	 * 
	 */
	void reset();
}
