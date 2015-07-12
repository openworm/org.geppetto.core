/**
 * 
 */
package org.geppetto.core.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.HDF5Reader;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.services.AService;
import org.geppetto.core.services.ModelFormat;
import org.geppetto.core.services.registry.ServicesRegistry;

/**
 * @author matteocantarelli
 * 
 */
public abstract class AModelInterpreter extends AService implements IModelInterpreter
{

	private static final String ID = "RECORDING_";

	protected List<URL> dependentModels = new ArrayList<URL>();

	protected void addRecordings(List<URL> recordings, String instancePath, ModelWrapper modelWrapper) throws ModelInterpreterException
	{
		try
		{
			if(recordings != null)
			{
				int i = 1;
				for(URL recording : recordings)
				{
					H5File file = HDF5Reader.readHDF5File(recording, projectId);
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

	@Override
	public List<URL> getDependentModels()
	{
		return dependentModels;
	}

	@Override
	public List<ModelFormat> getSupportedOutputs(AspectNode aspectNode) throws ModelInterpreterException
	{
		return ServicesRegistry.getModelInterpreterServiceFormats(this);
	}
}
