package org.geppetto.core.features;

import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.runtime.AspectNode;

/**
 * 
 * This feature allows a service to build a statice 3D scene out of the loaded model.
 * To have the scene change in time as result of the simulation see the IDynamicVisualTreeFeature.
 * The scene is built through the visual objects node inside the visual tree. 
 * 
 * @author matteocantarelli
 * @author Jesus Martinez (jesus@metacell.us)
 * 
 */
public interface IVisualTreeFeature extends IFeature
{

	boolean populateVisualTree(AspectNode aspectNode) throws ModelInterpreterException;
}
