/**
 * 
 */
package org.geppetto.core.conversion;

import org.geppetto.core.services.ModelFormat;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 */
public abstract class AConversion implements IConversion
{

	private ModelFormat _supportedInput;
	
	@Override
	public ModelFormat getSupportedInput(){
		return _supportedInput;
	}
	
	@Override
	public void setSupportedInput(ModelFormat supportedInput){
		this._supportedInput = supportedInput;
	}
	
	
}
