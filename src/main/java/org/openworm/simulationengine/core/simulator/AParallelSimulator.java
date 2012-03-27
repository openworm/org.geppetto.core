package org.openworm.simulationengine.core.simulator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openworm.simulationengine.core.model.IModel;
import org.openworm.simulationengine.core.simulation.ISimulationCallbackListener;
import org.openworm.simulationengine.core.solver.ISolver;

/**
 * @author matteocantarelli
 * 
 */
public abstract class AParallelSimulator implements ISimulator {

	Map<ISolver, List<IModel>> _iModelQueue = new HashMap<ISolver, List<IModel>>();
	
	ISimulationCallbackListener _listener;

	
	/**
	 * @return
	 */
	public ISimulationCallbackListener getListener() 
	{
		return _listener;
	}


	/**
	 * @param _listener
	 */
	public void setListener(ISimulationCallbackListener _listener) 
	{
		this._listener = _listener;
	}


	/* (non-Javadoc)
	 * @see org.openworm.simulationengine.core.simulator.ISimulator#initialize(org.openworm.simulationengine.core.simulation.ISimulationCallbackListener)
	 */
	public void initialize(ISimulationCallbackListener listener) 
	{
		setListener(listener);
	}

	
	/**
	 * @param model
	 */
	protected void enqueue(final IModel model, final ISolver solver) {
		if (!_iModelQueue.containsKey(solver)) 
		{
			_iModelQueue.put(solver, new ArrayList<IModel>());
		}
		_iModelQueue.get(solver).add(model);
	}


	/**
	 * 
	 */
	protected void clearQueue() {
		_iModelQueue.clear();
	}

	/**
	 * @param solver
	 */
	protected void clearQueue(final ISolver solver) {
		if (_iModelQueue.containsKey(solver)) {
			_iModelQueue.remove(solver);
		}
		else
		{
			throw new InvalidParameterException(solver + " is not present in the queue!");
		}
	}

	/**
	 * @param solver
	 * @return
	 */
	protected List<IModel> getQueue(final ISolver solver)
	{
		if(_iModelQueue.containsKey(solver))
		{
			return _iModelQueue.get(solver);
		}
		else
		{
			throw new InvalidParameterException(solver + " is not present in the queue!");
		}
	}
	
}
