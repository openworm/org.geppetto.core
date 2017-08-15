
package org.geppetto.core.model;

import ncsa.hdf.object.h5.H5File;

/**
 * @author matteocantarelli
 *
 */
public class Recording
{

	H5File _file = null;

	public Recording(H5File file)
	{
		_file = file;
	}

	public H5File getHDF5()
	{
		return _file;
	}

}
