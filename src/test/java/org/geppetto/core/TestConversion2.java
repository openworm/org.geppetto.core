
package org.geppetto.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geppetto.core.conversion.AConversion;
import org.geppetto.core.conversion.ConversionException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.model.DomainModel;
import org.geppetto.model.ModelFormat;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 */
public class TestConversion2 extends AConversion
{

	@Override
	public void registerGeppettoService()
	{
		//Input Model Format
		List<ModelFormat> inputModelFormats = new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.registerModelFormat("TEST2")));
		
		//Output Model Format
		List<ModelFormat> outputModelFormats = new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.registerModelFormat("TEST")));
		
		ServicesRegistry.registerConversionService(this, inputModelFormats, outputModelFormats);
	}

	@Override
	public List<ModelFormat> getSupportedOutputs(DomainModel model) throws ConversionException
	{
		return new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.getModelFormat("TEST")));
	}

	@Override
	public List<ModelFormat> getSupportedOutputs()
	{
		return new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.getModelFormat("TEST")));
	}

	@Override
	public List<ModelFormat> getSupportedInputs()
	{
		return new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.getModelFormat("TEST2")));
	}

	@Override
	public DomainModel convert(DomainModel model, ModelFormat output, IAspectConfiguration aspectConfig, GeppettoModelAccess modelAccess) throws ConversionException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
