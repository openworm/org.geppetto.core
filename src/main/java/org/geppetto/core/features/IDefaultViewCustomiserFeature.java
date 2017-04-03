package org.geppetto.core.features;

import org.geppetto.model.types.Type;

import com.google.gson.JsonObject;

/**
 * 
 * This features lets a ModelInterpreter contribute a default view for a Geppetto Project
 * 
 * @author matteocantarelli
 * 
 */
public interface IDefaultViewCustomiserFeature extends IFeature
{
	/**
	 * This method returns a chunk of JSON which can contribute to a default view.
	 * All chunks of JSON provided all model intepreters will be merged together.
	 * @param type
	 * @return
	 */
	JsonObject getDefaultViewCustomisation(Type type); 
}




