package org.geppetto.core.features;

import java.util.Map;

import org.geppetto.core.model.ModelInterpreterException;

/**
 * 
 * This interface allows the users to change the value of the parameters
 * in a given model.
 * 
 * @author matteocantarelli
 * 
 */
public interface ISetParameterFeature extends IFeature
{
	void setParameter(Map<String,String> parameter) throws ModelInterpreterException;
}
