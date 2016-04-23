package org.geppetto.core.features;

import org.geppetto.model.values.Pointer;


/**
 * This feature when implemented allows a simulator to dynamically change the 3D scene during the simulation as result of the evolution of a given model.
 * 
 * @author matteocantarelli
 * @author Jesus Martinez (jesus@metacell.us)
 * 
 */
public interface IDynamicVisualTreeFeature extends IFeature
{
	boolean updateVisualTree(Pointer pointer);
}
