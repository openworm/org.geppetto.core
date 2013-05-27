/**
 * 
 */
package org.geppetto.core.simulation;

import java.net.URL;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;

/**
 * @author matteocantarelli
 *
 */
public interface ISimulation {

	/**
	 * @param simConfigURL
	 * @param simulationListener 
	 */
	void init(URL simConfigURL, ISimulationCallbackListener simulationListener) throws GeppettoInitializationException;
	
	/**
	 * 
	 */
	void start() throws GeppettoExecutionException;
	
	/**
	 * 
	 */
	void pause() throws GeppettoExecutionException;
	
	/**
	 * 
	 */
	void stop() throws GeppettoExecutionException;
	
	/**
	 * 
	 */
	void reset() throws GeppettoExecutionException;
}
