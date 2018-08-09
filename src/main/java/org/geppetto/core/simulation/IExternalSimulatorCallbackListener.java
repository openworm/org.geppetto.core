package org.geppetto.core.simulation;

import org.geppetto.core.common.GeppettoExecutionException;

public interface IExternalSimulatorCallbackListener
{
	void processDone(String processToken, String[] processCommand) throws GeppettoExecutionException;
	
	void processFailed(String message, Exception e);
}
