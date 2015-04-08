package org.geppetto.core.features;

import org.geppetto.core.services.GeppettoFeature;

/**
 * Different services can implement different features. 
 * If a service implements a given feature the simulation controller
 * will allow the client to use it.
 * 
 * @author Jesus Martinez (jesus@metacell.us)
 *
 */
public interface IFeature {	
	GeppettoFeature getType();
}
