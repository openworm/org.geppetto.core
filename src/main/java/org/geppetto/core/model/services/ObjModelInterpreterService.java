

package org.geppetto.core.model.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.model.AModelInterpreter;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.ModelFormat;
import org.geppetto.model.types.Type;
import org.geppetto.model.types.TypesFactory;
import org.geppetto.model.types.VisualType;
import org.geppetto.model.values.ImportValue;
import org.geppetto.model.values.OBJ;
import org.geppetto.model.values.Pointer;
import org.geppetto.model.values.Value;
import org.geppetto.model.values.ValuesFactory;
import org.springframework.stereotype.Service;

/**
 * @author matteocantarelli
 * 
 */
@Service
public class ObjModelInterpreterService extends AModelInterpreter
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.IModelInterpreter#importType(java.net.URL, java.lang.String, org.geppetto.model.GeppettoLibrary)
	 */
	@Override
	public Type importType(URL url, String typeName, GeppettoLibrary library, GeppettoModelAccess commonLibrary) throws ModelInterpreterException
	{

		try
		{
			VisualType visualType = TypesFactory.eINSTANCE.createVisualType();
			Scanner scanner = new Scanner(url.openStream(), "UTF-8");
			String objContent = scanner.useDelimiter("\\A").next();
			scanner.close();
			OBJ obj = ValuesFactory.eINSTANCE.createOBJ();
			obj.setObj(objContent);
			visualType.setId(typeName);
			visualType.setName(typeName);
			visualType.setDefaultValue(obj);
			library.getTypes().add(visualType);
			return visualType;
		}
		catch(IOException e)
		{
			throw new ModelInterpreterException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.IModelInterpreter#downloadModel(org.geppetto.model.values.Pointer, org.geppetto.core.services.ModelFormat, org.geppetto.core.data.model.IAspectConfiguration)
	 */
	@Override
	public File downloadModel(Pointer pointer, ModelFormat format, IAspectConfiguration aspectConfiguration) throws ModelInterpreterException
	{
		throw new ModelInterpreterException("Download model not implemented for OBJ model interpreter");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.IModelInterpreter#getName()
	 */
	@Override
	public String getName()
	{
		return "OBJ Model Interpreter";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.services.IService#registerGeppettoService()
	 */
	@Override
	public void registerGeppettoService()
	{
		List<ModelFormat> modelFormats = new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.registerModelFormat("OBJ")));
		ServicesRegistry.registerModelInterpreterService(this, modelFormats);
	}


}
