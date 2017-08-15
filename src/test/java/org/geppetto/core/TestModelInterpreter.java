
package org.geppetto.core;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.model.AModelInterpreter;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.ModelFormat;
import org.geppetto.model.types.Type;
import org.geppetto.model.values.ImportValue;
import org.geppetto.model.values.Pointer;
import org.geppetto.model.values.Value;

/**
 * Dummy model interpreter used for testing purposes
 * 
 * @author Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class TestModelInterpreter extends AModelInterpreter
{


	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "Test Model interpreter";
	}

	@Override
	public void registerGeppettoService()
	{
		List<ModelFormat> modelFormats = new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.registerModelFormat("TEST")));
		ServicesRegistry.registerModelInterpreterService(this, modelFormats);
	}


	@Override
	public List<ModelFormat> getSupportedOutputs(Pointer pointer) throws ModelInterpreterException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type importType(URL url, String typeName, GeppettoLibrary library, GeppettoModelAccess commonLibrary) throws ModelInterpreterException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File downloadModel(Pointer pointer, ModelFormat format, IAspectConfiguration aspectConfiguration) throws ModelInterpreterException
	{
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Value importValue(ImportValue importValue)
			throws ModelInterpreterException {
		// TODO Auto-generated method stub
		return null;
	}

}
