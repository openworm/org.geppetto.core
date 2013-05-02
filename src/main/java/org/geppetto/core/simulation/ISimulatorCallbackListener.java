/**
 * 
 */
package org.geppetto.core.simulation;

import org.geppetto.core.model.StateSet;

/**
 * @author matteocantarelli
 *
 */
public interface ISimulatorCallbackListener {

	void stateSetReady(StateSet results);
	
	
}
