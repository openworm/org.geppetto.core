package org.geppetto.core.simulator;

import java.util.HashMap;
import java.util.Map;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.externalprocesses.ExternalProcess;
import org.geppetto.core.simulation.IExternalSimulatorCallbackListener;

/**
 * Abstract simulator class for external processes
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 * 
 */
public abstract class AExternalProcessSimulator extends ASimulator implements IExternalSimulatorCallbackListener
{

	protected boolean started = false;

	protected String[] commands = null;

	protected String directoryToExecuteFrom = null;

	protected String originalFileName;

	Map<String[], ExternalProcess> externalProcesses = new HashMap<String[], ExternalProcess>();

	protected String outputFolder;

	public void runExternalProcess(String[] command, String directoryToExecuteFrom, String fileToExecute)
	{
		ExternalProcess process = new ExternalProcess(command, directoryToExecuteFrom, fileToExecute, this, outputFolder);
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
	public void simulate() throws GeppettoExecutionException
	{
		// send command, directory where execution is happening, and path to original file script to execute
		if(!started)
		{
			this.runExternalProcess(commands, directoryToExecuteFrom, originalFileName);
			started = true;
		}
		else
		{
			throw new GeppettoExecutionException("Simulate has been called again");
		}
	}


}
