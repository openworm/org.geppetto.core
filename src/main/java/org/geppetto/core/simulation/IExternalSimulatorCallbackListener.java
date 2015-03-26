package org.geppetto.core.simulation;

import org.geppetto.core.common.GeppettoExecutionException;

public interface IExternalSimulatorCallbackListener {

	void processDone(String[] processCommand) throws GeppettoExecutionException;
}
