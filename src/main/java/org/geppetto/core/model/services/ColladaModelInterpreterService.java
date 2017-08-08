

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
import org.geppetto.model.types.TypesPackage;
import org.geppetto.model.types.VisualType;
import org.geppetto.model.util.GeppettoVisitingException;
import org.geppetto.model.values.Collada;
import org.geppetto.model.values.Pointer;
import org.geppetto.model.values.ValuesFactory;
import org.springframework.stereotype.Service;

/**
 * @author matteocantarelli
 * 
 */
@Service
public class ColladaModelInterpreterService extends AModelInterpreter
{

	@Override
	public String getName()
	{
		return "Collada Model Interpreter";
	}

	@Override
	public void registerGeppettoService()
	{
		List<ModelFormat> modelFormats = new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.registerModelFormat("COLLADA")));
		ServicesRegistry.registerModelInterpreterService(this, modelFormats);
	}

	@Override
	public Type importType(URL url, String typeName, GeppettoLibrary library, GeppettoModelAccess commonLibrary) throws ModelInterpreterException
	{
		try
		{
			VisualType visualType = (VisualType) commonLibrary.getType(TypesPackage.Literals.VISUAL_TYPE);
			Scanner scanner = new Scanner(url.openStream(), "UTF-8");
			String colladaContent = scanner.useDelimiter("\\A").next();
			scanner.close();
			Collada collada = ValuesFactory.eINSTANCE.createCollada();
			collada.setCollada(colladaContent);
			visualType.setId(typeName);
			visualType.setName(typeName);
			visualType.setDefaultValue(collada);
			library.getTypes().add(visualType);
			return visualType;
		}
		catch(IOException | GeppettoVisitingException e)
		{
			throw new ModelInterpreterException(e);
		}
	}

	@Override
	public File downloadModel(Pointer pointer, ModelFormat format, IAspectConfiguration aspectConfiguration) throws ModelInterpreterException
	{
		throw new ModelInterpreterException("Download model not implemented for Collada model interpreter");
	}


}
