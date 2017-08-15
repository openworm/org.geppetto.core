

package org.geppetto.core.s3;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.beans.PathConfiguration;
import org.geppetto.core.data.IGeppettoS3Manager;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Manager implements IGeppettoS3Manager
{

	private AmazonS3 _s3Connection;

	private static Log _logger = LogFactory.getLog(S3Manager.class);

	private static S3Manager instance;

	public static S3Manager getInstance()
	{
		if(instance == null)
		{
			instance = new S3Manager();
		}
		return instance;
	}

	private S3Manager()
	{
	}

	private AmazonS3 getS3Connection()
	{
		if(_s3Connection == null)
		{
			File credentialsFile = new File(PathConfiguration.settingsFolder + "/aws.credentials");
			try
			{
				_s3Connection = new AmazonS3Client(new PropertiesCredentials(credentialsFile));
			}
			catch(Exception e)
			{
				_logger.warn("Could not initialize S3 connection", e);
			}
		}
		return _s3Connection;
	}

	/**
	 * Save a file to S3.
	 * 
	 * @param file
	 * @param path
	 */
	public synchronized void saveFileToS3(File file, String path)
	{
		AmazonS3 s3 = getS3Connection();
		s3.putObject(PathConfiguration.s3BucketName, path, file);
	}
	
	public synchronized long getFileStorage(String path)
	{
		AmazonS3 s3 = getS3Connection();
		List<S3ObjectSummary> s3ObjectSummaries;
		s3ObjectSummaries = s3.listObjects(PathConfiguration.s3BucketName,path).getObjectSummaries();
		long totalSize = 0;

		for (int i = 0; i < s3ObjectSummaries.size(); i++) {
		    S3ObjectSummary s3ObjectSummary = s3ObjectSummaries.get(i);
		    totalSize+=s3ObjectSummary.getSize();
		}

		return totalSize;
	}

	/**
	 * Save a text to S3.
	 * 
	 * @param text
	 * @param path
	 */
	public void saveTextToS3(String text, String path) throws IOException
	{
		File file = File.createTempFile("file", "");
		Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
		saveFileToS3(file, path);
	}

	/**
	 * Return the object pointers to the files that are stored at the paths starting with a given prefix.
	 * 
	 * @param prefix
	 * @return
	 */
	public List<S3ObjectSummary> retrievePathsFromS3(String prefix)
	{
		ObjectListing listing = getS3Connection().listObjects(PathConfiguration.s3BucketName, prefix);
		List<S3ObjectSummary> allSummaries = new ArrayList<>();
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		while(!summaries.isEmpty())
		{
			allSummaries.addAll(summaries);
			summaries.clear();
			if(listing.isTruncated())
			{
				summaries = listing.getObjectSummaries();
			}
		}
		return allSummaries;
	}

	/**
	 * Delete the file given by the path from S3.
	 * 
	 * @param path
	 */
	public void deleteFromS3(String path)
	{
		getS3Connection().deleteObject(PathConfiguration.s3BucketName, path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoS3Manager#getURL(java.lang.String)
	 */
	@Override
	public URL getURL(String path) throws MalformedURLException
	{
		// Example: http://org.geppetto.bucket.s3.amazonaws.com/projects/1/GEPPETTO.xml
		return new URL("http://" + PathConfiguration.s3BucketName + ".s3.amazonaws.com/" + path);
	}
}
