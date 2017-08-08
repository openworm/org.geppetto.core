

package org.geppetto.core.conversion;

import java.util.List;

import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.IService;
import org.geppetto.model.DomainModel;
import org.geppetto.model.ModelFormat;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 *
 */
public interface IConversion extends IService
{

	List<ModelFormat> getSupportedOutputs(DomainModel model) throws ConversionException;

	List<ModelFormat> getSupportedOutputs() throws ConversionException;

	List<ModelFormat> getSupportedInputs() throws ConversionException;

	DomainModel convert(DomainModel model, ModelFormat output, IAspectConfiguration aspectConfig, GeppettoModelAccess modelAccess) throws ConversionException;

}
