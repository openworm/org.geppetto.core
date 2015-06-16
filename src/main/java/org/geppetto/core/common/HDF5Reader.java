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
package org.geppetto.core.common;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.geppetto.core.utilities.URLReader;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

/**
 * @author matteocantarelli
 * 
 */
public class HDF5Reader
{

	public static H5File readHDF5File(URL url) throws GeppettoExecutionException
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
				url = URLReader.createLocalCopy(url);
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
