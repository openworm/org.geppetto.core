package org.geppetto.core.beans;

import java.io.File;

public class Settings
{

	public static final String BUCKET_NAME = "org.geppetto.bucket";

	public static final String SETTINGS_DIR = System.getProperty("user.home") + "/geppetto";

	public static String getPathInTempFolder(String subPath)
	{
		return System.getProperty("user.dir") + File.separator + "geppettoTmp" + File.separator + subPath;
	}

	public static String getAspectDependentPath(long projectId, long experimentId, String aspectPath, String filename)
	{
		return "projects/" + projectId + "/" + experimentId + "/" + aspectPath + "/" + filename;
	}

	public static String getAspectDependentFullPath(long projectId, long experimentId, String aspectPath, String filename)
	{
		File dir = new File(getPathInTempFolder(getAspectDependentPath(projectId, experimentId, aspectPath, "")));
		dir.mkdirs();
		return getPathInTempFolder(getAspectDependentPath(projectId, experimentId, aspectPath, filename));
	}

}
