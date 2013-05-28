/**
 * 
 */
package org.geppetto.core.simulation;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.model.state.StateTreeRoot;


/**
 * @author matteocantarelli
 *
 */
public interface ISimulatorCallbackListener {

	void stateTreeUpdated(StateTreeRoot stateTree) throws GeppettoExecutionException;
	
	
}
