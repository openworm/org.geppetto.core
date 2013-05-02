/**
 * 
 */
package org.geppetto.core.simulator;

import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.model.IModel;
import org.geppetto.core.simulation.ISimulatorCallbackListener;

/**
 * @author matteocantarelli
 *
 */
public abstract class ASimulator implements ISimulator
{

	protected IModel _model;
	
	private ISimulatorCallbackListener _listener;
	
	private boolean _initialized=false;

	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#initialize(org.geppetto.core.model.IModel, org.geppetto.core.simulation.ISimulatorCallbackListener)
	 */
	@Override
	public void initialize(IModel model, ISimulatorCallbackListener listener) throws GeppettoInitializationException
	{
		setListener(listener);
		_model=model;
		_initialized=true;
	}
	
	/**
	 * @return
	 */
	public ISimulatorCallbackListener getListener() 
	{
		return _listener;
	}


	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#isInitialized()
	 */
	public boolean isInitialized()
	{
		return _initialized;
	}
	
	/**
	 * @param _listener
	 */
	public void setListener(ISimulatorCallbackListener listener) 
	{
		this._listener = listener;
	}

}
