package org.geppetto.core.features;

import org.geppetto.core.data.model.VariableList;

/**
 * Interface use for classes that need to implement variable watch
 * 
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 *
 */
public interface IListVariableWatchFeature extends IFeature{
	VariableList getWatcheableVariables();
}
