package org.geppetto.core.simulator;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.model.IModel;
import org.geppetto.core.simulation.IRunConfiguration;
import org.geppetto.core.simulation.ISimulatorCallbackListener;

/**
 * @author matteocantarelli
 *
 */
public interface ISimulator {


	void simulate(IRunConfiguration runConfiguration) throws GeppettoExecutionException;
	

	void initialize(IModel model, ISimulatorCallbackListener listener) throws GeppettoInitializationException;


	boolean isInitialized();
	
}
