/**
 * 
 */
package org.geppetto.core.solver;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.StateSet;
import org.geppetto.core.simulation.IRunConfiguration;

/**
 * @author matteocantarelli
 *
 */
public interface ISolver {
		
	public StateSet solve(final IRunConfiguration timeConfiguration) throws GeppettoExecutionException;
	
	public void initialize(final IModel model) throws GeppettoInitializationException;
	
	public void dispose();
}
