package org.geppetto.core.simulator;

import org.geppetto.core.model.IModel;
import org.geppetto.core.simulation.ISimulatorCallbackListener;
import org.geppetto.core.simulation.ITimeConfiguration;

/**
 * @author matteocantarelli
 *
 */
public interface ISimulator {


	void startSimulatorCycle();
	
	void simulate(IModel model, ITimeConfiguration timeConfiguration);
	
	void endSimulatorCycle();

	void initialize(ISimulatorCallbackListener listener);
}
