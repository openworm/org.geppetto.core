

package org.geppetto.core.conversion;

import org.geppetto.core.beans.PathConfiguration;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.services.AService;
import org.geppetto.model.ModelFormat;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 */
public abstract class AConversion extends AService implements IConversion
{

	protected boolean convertModel = true;
	private IExperiment experiment;

	public void checkSupportedFormat(ModelFormat input) throws ConversionException
	{
		if(!this.getSupportedInputs().contains(input))
		{
			throw new ConversionException("FORMAT NOT SUPPORTED");
		}
	}

	public String getConvertedResultsFolderName()
	{
		return PathConfiguration.convertedResultsPath;
	}

	public void setConvertModel(boolean convert)
	{
		this.convertModel = convert;
	}

	public void setExperiment(IExperiment experiment)
	{
		this.experiment=experiment;
	}
	
	public IExperiment getExperiment()
	{
		return this.experiment;
	}

}
