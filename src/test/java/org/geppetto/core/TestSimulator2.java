
package org.geppetto.core;

import java.util.ArrayList;
import java.util.Arrays;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.features.IFeature;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.GeppettoFeature;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.core.simulation.ISimulatorCallbackListener;
import org.geppetto.core.simulator.ASimulator;
import org.geppetto.model.DomainModel;
import org.geppetto.model.ExperimentState;
import org.geppetto.model.ModelFormat;

/**
 * Dummy simulator used for testing purposes 
 * 
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 *
 */
public class TestSimulator2 extends ASimulator
{

	@Override
	public void simulate() throws GeppettoExecutionException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(DomainModel model,IAspectConfiguration aspectConfiguration, ExperimentState experimentState, ISimulatorCallbackListener listener, GeppettoModelAccess modelAccess) throws GeppettoInitializationException, GeppettoExecutionException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInitialized()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "test";
	}

	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInitialized(boolean initialized) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTimeStepUnit() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void registerGeppettoService()
	{
		ServicesRegistry.registerSimulatorService(this, new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.registerModelFormat("TEST2"))));
	}

	@Override
	public boolean isSupported(GeppettoFeature feature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IFeature getFeature(GeppettoFeature feature) {
		// TODO Auto-generated method stub
		return null;
	}

}
