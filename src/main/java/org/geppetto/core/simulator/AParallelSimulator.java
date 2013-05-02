package org.geppetto.core.simulator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geppetto.core.model.IModel;
import org.geppetto.core.solver.ISolver;

/**
 * @author matteocantarelli
 * 
 */
public abstract class AParallelSimulator extends ASimulator {
	
	public abstract void startSimulatorCycle();
	
	public abstract void endSimulatorCycle();

	Map<ISolver, List<IModel>> _iModelQueue = new HashMap<ISolver, List<IModel>>();
	

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
