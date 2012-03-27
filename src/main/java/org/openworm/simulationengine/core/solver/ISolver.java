/**
 * 
 */
package org.openworm.simulationengine.core.solver;

import java.util.List;

import org.openworm.simulationengine.core.model.IModel;
import org.openworm.simulationengine.core.simulation.ITimeConfiguration;

/**
 * @author matteocantarelli
 *
 */
public interface ISolver {
		
	public List<List<IModel>> solve(final List<IModel> models,final ITimeConfiguration timeConfiguration);
	
}
