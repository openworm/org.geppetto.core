package org.geppetto.core.features;

import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.model.VariableValue;

/**
 * 
 * This interface allows the users to change the value of the parameters in a given model.
 * 
 * @author matteocantarelli
 * 
 */
public interface ISetParameterFeature extends IFeature
{
	void setParameter(VariableValue variableValue) throws ModelInterpreterException;
}
