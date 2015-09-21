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
package org.geppetto.core.beans;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.manager.Scope;

/**
 * Bean to keep track of the output folders for Geppetto
 * 
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * @author mcantarelli
 * 
 */
public class PathConfiguration
{
	private static Log logger = LogFactory.getLog(PathConfiguration.class);

	public static final String settingsFolder = System.getProperty("user.home") + "/geppetto";
	public static String s3BucketName;
	public static String convertedResultsPath;
	public static String rawResultsPath;
	public static String geppettoTmp;
	public static String downloadModelFolderName;

	static
	{
		Properties prop = new Properties();
		try
		{
			prop.load(PathConfiguration.class.getResourceAsStream("/Geppetto.properties"));
			convertedResultsPath = prop.getProperty("convertedResultsPath");
			rawResultsPath = prop.getProperty("rawResultsPath");
			geppettoTmp = prop.getProperty("geppettoTmp");
			s3BucketName = prop.getProperty("s3BucketName");
			downloadModelFolderName = prop.getProperty("downloadModel");
		}
		catch(IOException e)
		{
			logger.error("Unable to read GEPPETTO.properties file");
		}
	}

	/**
	 * @param scope
	 * @param projectId
	 * @return
	 */
	public static String getProjectPath(Scope scope, long projectId)
	{
		return File.separator + scope + File.separator + "projects" + File.separator + projectId + File.separator;
	}

	/**
	 * e.g. returns /geppettoTmp/RUN/projects/1/
	 * 
	 * @param scope
	 *            The scope of the temporary path
	 * @param projectId
	 * @return
	 */
	public static String getProjectTmpPath(Scope scope, long projectId)
	{
		return getPathInTempFolder(getProjectPath(scope, projectId));
	}

	/**
	 * @param subPath
	 * @return
	 */
	public static String getPathInTempFolder(String subPath)
	{
		return System.getProperty("user.dir") + File.separator + geppettoTmp + File.separator + subPath;
	}

	/**
	 * @param scope
	 * @param projectId
	 * @return
	 * @throws IOException
	 */
	public static void deleteProjectTmpFolder(Scope scope, long projectId) throws IOException
	{
		FileUtils.deleteDirectory(new File(PathConfiguration.getProjectTmpPath(scope, projectId)));
	}

	/**
	 * @param scope
	 * @param projectId
	 * @param experimentId
	 * @param aspectPath
	 * @param filename
	 * @return
	 */
	public static String getExperimentTmpPath(Scope scope, long projectId, long experimentId, String aspectPath, String filename)
	{
		return getPathInTempFolder(getExperimentPath(scope, projectId, experimentId, aspectPath, filename));
	}

	/**
	 * @param scope
	 * @param projectId
	 * @param experimentId
	 * @param aspectPath
	 * @param filename
	 * @return
	 */
	public static String getExperimentPath(Scope scope, long projectId, long experimentId, String aspectPath, String filename)
	{
		if(aspectPath == null)
		{
			return PathConfiguration.getProjectPath(scope, projectId) + File.separator + experimentId + File.separator + filename;
		}
		else
		{
			return PathConfiguration.getProjectPath(scope, projectId) + experimentId + File.separator + aspectPath + File.separator + filename;
		}
	}

	/**
	 * @param scope
	 * @param projectId
	 * @param experimentId
	 * @param aspectPath
	 * @param filename
	 * @return
	 */
	public static String createExperimentTmpPath(Scope scope, long projectId, long experimentId, String aspectPath, String filename)
	{
		File dir = new File(getExperimentTmpPath(scope, projectId, experimentId, aspectPath, ""));
		dir.mkdirs();
		return getExperimentTmpPath(scope, projectId, experimentId, aspectPath, filename);
	}

	/**
	 * @param trailer
	 * @param experiment
	 * @param extension
	 * @param time
	 * @return
	 */
	public static String getName(String trailer, IExperiment experiment, String extension, boolean time)
	{
		return getName(trailer + "_P" + experiment.getParentProject().getId() + "E" + experiment.getId(), time) + "." + extension;
	}

	/**
	 * @param fileName
	 * @param time
	 * @return
	 */
	public static String getName(String fileName, boolean time)
	{
		String timeString = "";
		if(time)
		{
			timeString = "_" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		}
		return fileName + timeString;
	}

	/**
	 * @param scope
	 * @param projectId
	 * @param fileName
	 * @return
	 */
	public static String createProjectTmpFolder(Scope scope, long projectId, String fileName)
	{
		File dir = new File(PathConfiguration.getProjectTmpPath(scope, projectId));
		dir.mkdirs();
		return PathConfiguration.getProjectTmpPath(scope, projectId) + fileName;
	}

	/**
	 * @param scope
	 * @param projectId
	 * @param folderName
	 * @return
	 */
	public static File createFolderInProjectTmpFolder(Scope scope, long projectId, String folderName)
	{
		File folder = new File(createProjectTmpFolder(scope, projectId, folderName) + File.separator);
		// createProjectTmpFolder creates only up to the project level, the following mkdir will create also folderName
		folder.mkdir();
		return folder;
	}
	
	public static URL getModelSchemaURL(){
		return PathConfiguration.class.getResource("/schema/geppettoModel/geppettoModelSchema.xsd");
	}
}
