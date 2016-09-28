package org.geppetto.core.externalprocesses;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.simulation.IExternalSimulatorCallbackListener;

/**
 * Threaded class used to execute an external process
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class ExternalProcess extends Thread
{

	private static Log _logger = LogFactory.getLog(ExternalProcess.class);
	private String[] _commands;
	private String _directoryToExecuteFrom;
	public volatile boolean run = true;
	private IExternalSimulatorCallbackListener _callback;
	private String _fileToExecute;
	private String outputFolder;
	private ExternalProcessWatcher procOutputMain,procOutputError;

	public ExternalProcess(String[] commands, String directoryToExecuteFrom, String fileToExecute, IExternalSimulatorCallbackListener callback, String outputFolder)
	{
		this._commands = commands;
		this._directoryToExecuteFrom = directoryToExecuteFrom;
		this._callback = callback;
		this._fileToExecute = fileToExecute;
		this.outputFolder = outputFolder;
	}

	public String getOutputFolder()
	{
		return outputFolder;
	}

	@Override
	public void run()
	{
		if(run)
		{
			run = false;
			try
			{
				compile();
			}
			catch(GeppettoExecutionException e)
			{
				String errorMessage = "Geppetto Execution Exception error : " + e.getMessage();
				_logger.error(errorMessage);
				String logMessage = "";
				if(procOutputError !=null){
					logMessage = procOutputError.logMessage();
				}
				_callback.processFailed(logMessage, e);
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Runs a process specified by the command passed in as a parameter
	 */
	public boolean compile() throws GeppettoExecutionException
	{
		try
		{
			_logger.info("Going to execute command: " + StringUtils.join(_commands, ",") + ", from directory: " + _directoryToExecuteFrom);
			Runtime runtime = Runtime.getRuntime();

			// Process currentProcess = runtime.exec(StringUtils.join(_command, ";"), null, new File(_directoryToExecuteFrom));
			for(String command : _commands)
			{
				Process currentProcess = runtime.exec(command, null, new File(_directoryToExecuteFrom));
				procOutputMain = new ExternalProcessWatcher(currentProcess.getInputStream(), "Success : ");
				procOutputMain.start();

				procOutputError = new ExternalProcessWatcher(currentProcess.getErrorStream(), "Error   : ");
				procOutputError.start();

				_logger.info("Successfully executed command: " + command);

				currentProcess.waitFor();
			}

			_logger.info("Proccess done for command " + _commands + " done");

			_callback.processDone(this._commands);
		}
		catch(Exception e)
		{
			_logger.error("Unable to execute command: " + _commands);
			String logMessage = "";
			if(procOutputError !=null){
				logMessage = procOutputError.logMessage();
			}
			_callback.processFailed(logMessage, e);
			throw new GeppettoExecutionException(e);
		}
		return true;
	}

	public String[] getCommand()
	{
		return this._commands;
	}

	public String getExecutionDirectoryPath()
	{
		return this._directoryToExecuteFrom;
	}

	public String getFileToExecute()
	{
		return this._fileToExecute;
	}
	
	public String getLogErrorMessage(){
		return this.procOutputError.logMessage();
	}
}
