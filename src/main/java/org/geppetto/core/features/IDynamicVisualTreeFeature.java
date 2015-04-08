package org.geppetto.core.features;

import org.geppetto.core.model.runtime.AspectNode;

/**
 * Updates visualization tree
 * 
 * @author Jesus Martinez (jesus@metacell.us)
 *
 */
public interface IDynamicVisualTreeFeature extends IFeature{
	boolean updateVisualTree(AspectNode aspectNode);
}
