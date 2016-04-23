package org.geppetto.core.features;

import org.geppetto.core.services.GeppettoFeature;

/**
 * A feature defines an atomic functionality that can be implemented by a service. Different services can implement different features, e.g. a simulator can support the feature to set parameters while
 * another one could not. If a service implements a given feature its functionality will be available to the users.
 * 
 * @author matteocantarelli
 * @author Jesus Martinez (jesus@metacell.us)
 * 
 */
public interface IFeature
{
	GeppettoFeature getType();
}
