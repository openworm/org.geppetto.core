/**
 * 
 */
package org.geppetto.core.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.DataManagerHelper;
import org.geppetto.core.data.DefaultGeppettoDataManager;

/**
 * @author matteocantarelli
 * 
 */
public class URLReader
{

	private static Log _logger = LogFactory.getLog(URLReader.class);

	/**
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	public static URL getURL(String urlString) throws IOException
	{
		URL url = null;
		if(urlString.startsWith("https://") || urlString.startsWith("http://") || urlString.startsWith("file://"))
		{
			url = new URL(urlString);
		}
		else if(DataManagerHelper.getDataManager().isDefault())
		{
			url = DefaultGeppettoDataManager.class.getResource(urlString);
		}
		else
		{
			throw new IOException("Can't find the Geppetto model at " + urlString);
		}
		return url;
	}

	public static String readStringFromURL(URL url) throws IOException
	{
		long startRead = System.currentTimeMillis();

		StringBuilder content = new StringBuilder();

		// many of these calls can throw exceptions, so i've just
		// wrapped them all in one try/catch statement.
		try
		{

			// create a urlconnection object
			URLConnection urlConnection = url.openConnection();

			// wrap the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String line;

			// read from the urlconnection via the bufferedreader
			while((line = bufferedReader.readLine()) != null)
			{
				content.append(line + "\n");
			}
			bufferedReader.close();

			_logger.info("Reading of " + url.toString() + " took " + (System.currentTimeMillis() - startRead) + "ms");
		}
		catch(Exception e)
		{
			// e.printStackTrace();
			_logger.warn("File at " + url + " not found");
		}
		return content.toString();

	}
}
