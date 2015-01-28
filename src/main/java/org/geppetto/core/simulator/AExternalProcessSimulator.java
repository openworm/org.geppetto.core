package org.geppetto.core.simulator;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.simulation.IRunConfiguration;

/**
 * Abstract simulator class for external processes 
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public abstract class AExternalProcessSimulator extends ASimulator{

	@Override
	public void simulate(IRunConfiguration runConfiguration, AspectNode aspect)
			throws GeppettoExecutionException {
		notifyStateTreeUpdated();
	}

	@Override
	public boolean populateVisualTree(AspectNode aspectNode)
			throws ModelInterpreterException, GeppettoExecutionException {
		return false;
	}

}
