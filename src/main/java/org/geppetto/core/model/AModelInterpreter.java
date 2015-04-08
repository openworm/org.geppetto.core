/**
 * 
 */
package org.geppetto.core.model;

import java.net.URL;
import java.util.List;

import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.HDF5Reader;
import org.geppetto.core.services.AService;
/**
 * @author matteocantarelli
 * 
 */
public abstract class AModelInterpreter extends AService implements IModelInterpreter
{

	private static final String ID = "RECORDING_";
	
	protected void addRecordings(List<URL> recordings, String instancePath, ModelWrapper modelWrapper) throws ModelInterpreterException
	{
		try
		{
			if(recordings != null)
			{
				int i = 1;
				for(URL recording : recordings)
				{
					H5File file = HDF5Reader.readHDF5File(recording);
					RecordingModel recordingModel = new RecordingModel(file);
					recordingModel.setInstancePath(instancePath);
					modelWrapper.wrapModel(ID + i++, recordingModel);
				}
			}
		}
		catch(GeppettoExecutionException e)
		{
			throw new ModelInterpreterException(e);
		}
	}

}
