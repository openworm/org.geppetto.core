

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * @author matteocantarelli
 * 
 */
@Service
public class ObjModelInterpreterService extends AModelInterpreter
{

	private static final Log logger = LogFactory.getLog(ObjModelInterpreterService.class);

	// Retry policy for fetching remote OBJ meshes. The mesh host occasionally
	// returns transient failures (e.g. HTTP 503 / dropped connections); a short
	// retry recovers most of them without failing the whole model load.
	private static final int MAX_ATTEMPTS = 3;
	private static final long RETRY_BASE_DELAY_MS = 1000;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.geppetto.core.model.IModelInterpreter#importType(java.net.URL, java.lang.String, org.geppetto.model.GeppettoLibrary)
	 */
	@Override
	public Type importType(URL url, String typeName, GeppettoLibrary library, GeppettoModelAccess commonLibrary) throws ModelInterpreterException
	{

		IOException lastError = null;

		for(int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++)
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
				lastError = e;
				// Always include the type id + url so failures are debuggable from the log.
				logger.warn("Failed to load OBJ mesh [" + typeName + "] from [" + url + "] (attempt " + attempt + " of " + MAX_ATTEMPTS + "): " + e.getMessage());
				if(attempt < MAX_ATTEMPTS)
				{
					try
					{
						Thread.sleep(RETRY_BASE_DELAY_MS * attempt);
					}
					catch(InterruptedException ie)
					{
						Thread.currentThread().interrupt();
						break;
					}
				}
			}
		}

		// Gave up after MAX_ATTEMPTS. Rather than abort the whole model load, log the
		// failing type id + url and return an empty visual type so the rest of the
		// experiment continues to load safely without this one mesh.
		logger.error("Giving up on OBJ mesh [" + typeName + "] from [" + url + "] after " + MAX_ATTEMPTS
				+ " attempts - continuing without geometry for this type. Last error: "
				+ (lastError != null ? lastError.getMessage() : "unknown"));

		VisualType visualType = TypesFactory.eINSTANCE.createVisualType();
		OBJ obj = ValuesFactory.eINSTANCE.createOBJ();
		obj.setObj("");
		visualType.setId(typeName);
		visualType.setName(typeName);
		visualType.setDefaultValue(obj);
		library.getTypes().add(visualType);
		return visualType;
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
