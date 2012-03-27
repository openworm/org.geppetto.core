package org.openworm.simulationengine.core.simulator;

import org.openworm.simulationengine.core.model.IModel;
import org.openworm.simulationengine.core.simulation.ISimulationCallbackListener;
import org.openworm.simulationengine.core.simulation.ITimeConfiguration;

/**
 * @author matteocantarelli
 *
 */
public interface ISimulator {


	void startSimulatorCycle();
	
	void simulate(IModel model, ITimeConfiguration timeConfiguration);
	
	void endSimulatorCycle();

	void initialize(ISimulationCallbackListener listener);
}
