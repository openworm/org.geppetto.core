/**
 * 
 */
package org.geppetto.core.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.services.AService;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.model.ModelFormat;
import org.geppetto.model.values.Pointer;

/**
 * @author matteocantarelli
 * 
 */
public abstract class AModelInterpreter extends AService implements IModelInterpreter
{

	protected List<URL> dependentModels = new ArrayList<URL>();

	@Override
	public List<URL> getDependentModels()
	{
		return dependentModels;
	}

	@Override
	public List<ModelFormat> getSupportedOutputs(Pointer pointer) throws ModelInterpreterException
	{
		return ServicesRegistry.getModelInterpreterServiceFormats(this);
	}
}
