

package org.geppetto.core.data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This interface contains methods to deal with persisting stuff to S3.
 * 
 * @author dandromerecschi
 * 
 */
public interface IGeppettoS3Manager
{

	void saveFileToS3(File file, String path);

	void saveTextToS3(String text, String path) throws IOException;

	void deleteFromS3(String path);

	URL getURL(String path) throws MalformedURLException;

}