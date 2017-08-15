

package org.geppetto.core.simulator;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.IService;
import org.geppetto.core.simulation.ISimulatorCallbackListener;
import org.geppetto.model.DomainModel;
import org.geppetto.model.ExperimentState;

/**
 * @author matteocantarelli
 * @author giovanniidili
 */
public interface ISimulator extends IService
{

	/**
	 * The simulate method will tell the simulator to execute to run the simulation. A simulator might send back updates after a number of steps or at the end of the simulation. The simulate is
	 * nonetheless always called just once.
	 * 
	 * @param aspectConfiguration
	 * @param variable
	 * @throws GeppettoExecutionException
	 */
	void simulate() throws GeppettoExecutionException;

	/**
	 * This interface assumes the simulator will use the domain model (IModel) to simulate. 
	 * In the future some native simulator could work directly with a Pointer instead.
	 * 
	 * @param model
	 * @param aspectConfiguration
	 * @param listener
	 * @throws GeppettoInitializationException
	 * @throws GeppettoExecutionException
	 */
	void initialize(DomainModel model, IAspectConfiguration aspectConfiguration, ExperimentState experimentState, ISimulatorCallbackListener listener, GeppettoModelAccess modelAccess) throws GeppettoInitializationException, GeppettoExecutionException;

	boolean isInitialized();

	String getName();

	String getId();

	double getTime();

	String getTimeStepUnit();

	void setInitialized(boolean initialized);
}
