package org.geppetto.core.externalprocesses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Threaded class to print out log statements with Neuron Process logs.
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class ExternalProcessWatcher extends Thread
{
	private InputStreamReader _processStream = null;
	private String _type = null;
	private static Log _logger = LogFactory.getLog(ExternalProcessWatcher.class);
	private StringBuilder logMessage = new StringBuilder();
	
	public ExternalProcessWatcher(InputStream processStream, String type)
	{
		this._processStream = new InputStreamReader(processStream);
		this._type = type;
	}

	@Override
	public void run()
	{
		try
		{
			BufferedReader br = new BufferedReader(_processStream);
			String line = null;
			while((line = br.readLine()) != null)
			{
				_logger.info(_type + "> " + line);
				this.addToLogMessage(line);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void addToLogMessage(String newLine){
		this.logMessage.append(newLine);
	}
	
	public String logMessage(){
		return this.logMessage.toString();
	}
}
