
package org.geppetto.core.services;

import org.geppetto.core.features.IFeature;

/**
 * 
 * This interface defines the common functionalities to be implemented by every service. It provides the ability to register a service in the Service Registry and the ability to selectively implement
 * different features.
 * 
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * @author matteocantarelli
 * 
 */
public interface IService
{
	void registerGeppettoService() throws Exception;

	boolean isSupported(GeppettoFeature feature);

	IFeature getFeature(GeppettoFeature feature);

	void addFeature(IFeature feature);

}
