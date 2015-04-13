package org.geppetto.core.features;

import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.runtime.AspectNode;

/**
 * Interface use for classes that need to implement variable watch
 * 
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 *
 */
public interface ISimulationTreeFeature extends IFeature{
	boolean populateSimulationTree(AspectNode aspectNode) throws ModelInterpreterException;
	
}
