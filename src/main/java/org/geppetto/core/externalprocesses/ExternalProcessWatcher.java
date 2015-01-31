package org.geppetto.core.externalprocesses;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Threaded class to print out log statements with Neuron Process 
 * logs.
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class ExternalProcessWatcher extends Thread
{
    private InputStreamReader _processStream = null;
    private String _type = null;    
	private static Log _logger = LogFactory.getLog(ExternalProcessWatcher.class);

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
            String line=null;
            while ( (line = br.readLine()) != null)
            {
                _logger.info(_type +"> "+line);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
