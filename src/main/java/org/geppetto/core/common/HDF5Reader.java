
package org.geppetto.core.common;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.manager.Scope;
import org.geppetto.core.utilities.URLReader;

/**
 * @author matteocantarelli
 * 
 */
public class HDF5Reader
{

	public static H5File readHDF5File(URL url, long projectId) throws GeppettoExecutionException
	{
		try
		{
			// retrieve an instance of H5File
			FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

			// open the file with read and write access
			if(!url.getProtocol().equals("file"))
			{
				// FUN we need to download the file locally to the server and pass it to HDF5 like that since the HDF5 library doesn't support
				// reading from a URL and it cannot be implemented either without changing the native libraries, see here:
				// https://github.com/jrmartin/hdf5-java-bindings/blob/master/src/main/java/ncsa/hdf/hdf5lib/H5.java#L2566

				// The scope is CONNECTION since if we are reading a recording the simulation has already happened
				url = URLReader.createLocalCopy(Scope.CONNECTION, projectId, url, true);
			}
			H5File testFile = (H5File) fileFormat.createInstance(url.getPath(), FileFormat.READ);

			if(!testFile.canRead())
			{
				File recordings = new File("/recordings");
				if(!recordings.exists())
				{
					recordings.mkdir();
				}

				InputStream is = null;
				DataInputStream dis;
				String s;

				try
				{
					is = url.openStream(); // throws an IOException

					dis = new DataInputStream(new BufferedInputStream(is));
				}
				catch(MalformedURLException mue)
				{
					throw new GeppettoExecutionException(mue);
				}
				catch(IOException ioe)
				{
					throw new GeppettoExecutionException(ioe);
				}
				finally
				{
					try
					{
						is.close();
					}
					catch(IOException ioe)
					{
						throw new GeppettoExecutionException(ioe);
					}
				}
			}
			return testFile;
		}
		catch(IOException ioe)
		{
			throw new GeppettoExecutionException(ioe);
		}
		catch(Exception e)
		{
			throw new GeppettoExecutionException(e);
		}
	}
}
