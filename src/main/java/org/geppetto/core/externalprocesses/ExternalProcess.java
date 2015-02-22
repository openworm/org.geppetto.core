package org.geppetto.core.externalprocesses;

import java.io.File;
import java.io.IOException;

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
public class ExternalProcess extends Thread{
	
	private static Log _logger = LogFactory.getLog(ExternalProcess.class);
	private String _command;
	private String _directoryToExecuteFrom;
	public volatile boolean run = true;
	private IExternalSimulatorCallbackListener _callback;
	private String _fileToExecute;
	
	public ExternalProcess(String command, String directoryToExecuteFrom, 
			String fileToExecute, IExternalSimulatorCallbackListener callback){
		this._command = command;
		this._directoryToExecuteFrom = directoryToExecuteFrom;
		this._callback = callback;
		this._fileToExecute = fileToExecute;
		
		try {
			compile();
		} catch (GeppettoExecutionException e) {
			_logger.error("Geppetto Exectuion Exception error : " + e.getMessage());
		}
	}
	
	/**
     * Runs a process specified by the command passed in as a parameter
     */
	public boolean compile() throws GeppettoExecutionException {
		try{
			_logger.info("Going to execute command: " + _command + ", from directory: " + _directoryToExecuteFrom);

			Runtime runtime = Runtime.getRuntime();

			Process currentProcess = runtime.exec(_command, null, new File(_directoryToExecuteFrom));
			ExternalProcessWatcher procOutputMain = new ExternalProcessWatcher(currentProcess.getInputStream(),  "Success : ");
			procOutputMain.start();

			ExternalProcessWatcher procOutputError = new ExternalProcessWatcher(currentProcess.getErrorStream(), "Error   : ");
			procOutputError.start();

			_logger.info("Successfully executed command: " + _command);

			currentProcess.waitFor();
			
			_callback.processDone(this._command);
			
			_logger.info("Proccess done for command "+_command + " done");
		}
		catch(IOException | InterruptedException e){
			_logger.error("Unable to execute command: " + _command);
			throw new GeppettoExecutionException(e);
		}
		return true;
    }
	
	public String getCommand(){
		return this._command;
	}
	
	public String getExecutionDirectoryPath(){
		return this._directoryToExecuteFrom;
	}
	
	public String getFileToExecute(){
		return this._fileToExecute;
	}
}
