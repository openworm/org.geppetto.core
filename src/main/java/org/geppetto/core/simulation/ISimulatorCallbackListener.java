/**
 * 
 */
package org.geppetto.core.simulation;

import java.util.List;

import org.geppetto.core.model.IModel;

/**
 * @author matteocantarelli
 *
 */
public interface ISimulatorCallbackListener {

	void resultReady(final List<IModel> models);
	
	
}
