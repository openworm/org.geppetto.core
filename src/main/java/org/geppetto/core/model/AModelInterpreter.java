/**
 * 
 */
package org.geppetto.core.model;

import java.net.URL;
import java.util.List;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.HDF5Reader;

import ucar.nc2.NetcdfFile;

/**
 * @author matteocantarelli
 * 
 */
public abstract class AModelInterpreter implements IModelInterpreter
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
					NetcdfFile file = HDF5Reader.readHDF5File(recording);
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
