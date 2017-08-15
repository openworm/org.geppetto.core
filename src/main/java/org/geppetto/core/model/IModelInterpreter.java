

package org.geppetto.core.model;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.services.IService;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.ModelFormat;
import org.geppetto.model.types.Type;
import org.geppetto.model.values.ImportValue;
import org.geppetto.model.values.Pointer;
import org.geppetto.model.values.Value;

/**
 * @author matteocantarelli
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 */
public interface IModelInterpreter extends IService
{

	Type importType(URL url, String typeName, GeppettoLibrary library, GeppettoModelAccess commonLibraryAccess) throws ModelInterpreterException;
	
	Value importValue(ImportValue importValue) throws ModelInterpreterException;

	File downloadModel(Pointer pointer, ModelFormat format, IAspectConfiguration aspectConfiguration) throws ModelInterpreterException;

	List<ModelFormat> getSupportedOutputs(Pointer pointer) throws ModelInterpreterException;

	String getName();

	List<URL> getDependentModels();
}
