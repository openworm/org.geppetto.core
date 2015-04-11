package org.geppetto.core.features;

import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.runtime.AspectNode;

/**
 * Interface feature use for populatin of visualization trees
 * 
 * @author Jesus Martinez (jesus@metacell.us)
 *
 */
public interface IVisualTreeFeature extends IFeature{
	boolean populateVisualTree(AspectNode aspectNode) throws ModelInterpreterException;
}
