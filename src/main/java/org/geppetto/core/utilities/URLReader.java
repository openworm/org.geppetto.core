/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011 - 2015 OpenWorm.
 * http://openworm.org
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *     	OpenWorm - http://openworm.org/people.html
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/

package org.geppetto.core.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.beans.PathConfiguration;
import org.geppetto.core.data.DataManagerHelper;
import org.geppetto.core.data.DefaultGeppettoDataManager;
import org.geppetto.core.manager.Scope;

/**
 * @author matteocantarelli
 * 
 */
public class URLReader
{

	private static Log _logger = LogFactory.getLog(URLReader.class);
	private static final String SERVER_ROOT_TOKEN = "SERVER_ROOT";

	/**
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	public static URL getURL(String urlString) throws IOException
	{
		URL url = null;
		if(urlString.startsWith("https://") || urlString.startsWith("http://") || urlString.startsWith("file:/"))
		{
			url = new URL(urlString);
		}
		else if(urlString.startsWith(SERVER_ROOT_TOKEN))
		{
			urlString = urlString.replace(SERVER_ROOT_TOKEN, "");
			url = getServerRootURL(urlString);
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

	/**
	 * This method copies the file pointed by the remote URL locally to the server Since this method essentially creates a temporary file in the server that will need to be cleared we have additional
	 * information to define the scope and the id of the project
	 * 
	 * @param url
	 * @param scope
	 * @param projectId
	 * @return
	 * @throws IOException
	 */
	public static URL createLocalCopy(Scope scope, long projectId, URL url) throws IOException
	{
		File outputFile = new File(PathConfiguration.createProjectTmpFolder(scope, projectId, PathConfiguration.getName(getFileName(url), true)));
		URLConnection uc = url.openConnection();
		uc.connect();
		InputStream in = uc.getInputStream();
		FileOutputStream out = new FileOutputStream(outputFile);
		final int BUF_SIZE = 1 << 8;
		byte[] buffer = new byte[BUF_SIZE];
		int bytesRead = -1;
		while((bytesRead = in.read(buffer)) > -1)
		{
			out.write(buffer, 0, bytesRead);
		}
		in.close();
		out.close();
		return outputFile.toURI().toURL();
	}

	public static String getFileName(URL url)
	{
		int indexes = url.getPath().lastIndexOf(File.separator);
		if(indexes == -1)
		{
			indexes = url.getPath().lastIndexOf("/");
		}
		String fileName = url.getPath().substring(indexes + 1);
		return fileName;
	}

	public static URL getServerRootURL(String localPath) throws MalformedURLException
	{
		File catalinaBase = new File(System.getProperty("catalina.home")).getAbsoluteFile();
		return new File(catalinaBase, localPath).toURI().toURL();
	}
}
