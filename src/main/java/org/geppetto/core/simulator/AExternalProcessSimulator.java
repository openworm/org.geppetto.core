package org.geppetto.core.simulator;

import java.util.HashMap;
import java.util.Map;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.externalprocesses.ExternalProcess;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.simulation.IExternalSimulatorCallbackListener;

/**
 * Abstract simulator class for external processes
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 * 
 */
public abstract class AExternalProcessSimulator extends ASimulator implements IExternalSimulatorCallbackListener
{

	//the instance path of the aspect that is being simulated
	protected AspectNode aspectNode;
	

	Map<String[], ExternalProcess> externalProcesses = new HashMap<String[], ExternalProcess>();

	public void runExternalProcess(String[] command, String directoryToExecuteFrom, String fileToExecute)
	{
		ExternalProcess process = new ExternalProcess(command, directoryToExecuteFrom, fileToExecute, this);
		process.setName("External Process");
		process.start();

		externalProcesses.put(command, process);
	}
	

	public Map<String[], ExternalProcess> getExternalProccesses()
	{
		return this.externalProcesses;
	}

	public abstract String getSimulatorPath();

	@Override
	public void processDone(String[] processCommand) throws GeppettoExecutionException
	{
		ExternalProcess process = this.externalProcesses.get(processCommand);
	}
	
	@Override
	public void simulate(IAspectConfiguration aspectConfiguration, AspectNode aspect) throws GeppettoExecutionException
	{
		this.aspectNode = aspect;
	}
	
	public AspectNode getAspectNode(){
		return this.aspectNode;
	}
	
}
