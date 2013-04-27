/**
 * 
 */
package org.geppetto.core.solver;

import java.util.List;

import org.geppetto.core.model.IModel;
import org.geppetto.core.simulation.ITimeConfiguration;

/**
 * @author matteocantarelli
 *
 */
public interface ISolver {
		
	public List<List<IModel>> solve(final List<IModel> models,final ITimeConfiguration timeConfiguration);
	
}
