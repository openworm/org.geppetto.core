/**
 * 
 */
package org.geppetto.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geppetto.core.common.ColumnComparator;
import org.geppetto.core.model.values.AValue;

/**
 * @author matteocantarelli
 * 
 */
public class StateSet
{

	private String _modelId;
	private Map<StateInstancePath, List<AValue>> _statesMap = new HashMap<StateInstancePath, List<AValue>>();

	/**
	 * @param modelId
	 */
	public StateSet(String modelId)
	{
		super();
		this._modelId = modelId;
	}

	/**
	 * @return
	 */
	public String getModelId()
	{
		return _modelId;
	}

	/**
	 * @return
	 */
	public Map<StateInstancePath, List<AValue>> getStatesMap()
	{
		return _statesMap;
	}

	/**
	 * @param state
	 * @return
	 */
	public List<AValue> getStateValues(StateInstancePath state)
	{
		return _statesMap.get(state);
	}

	/**
	 * @param state
	 * @param timeStep
	 * @return
	 */
	public AValue getStateValue(StateInstancePath state, int timeStep)
	{
		return _statesMap.get(state).get(timeStep);
	}

	/**
	 * @param state
	 * @param value
	 */
	public void addStateValue(StateInstancePath state, AValue value)
	{
		if(!_statesMap.containsKey(state))
		{
			_statesMap.put(state, new ArrayList<AValue>());
		}
		_statesMap.get(state).add(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		List<StateInstancePath> list = new ArrayList<StateInstancePath>(_statesMap.keySet());
		Collections.sort((List<?>) list, new ColumnComparator(0));

		for(StateInstancePath state : list)
		{
			sb.append(state + " \n");
			int step = 0;
			for(AValue v : _statesMap.get(state))
			{
				sb.append("\t{" + (step++) + "}=" + v + " \n");
			}
		}
		return sb.toString();
	}

	public String lastStateToString()
	{
		StringBuffer sb = new StringBuffer();
		List<StateInstancePath> list = new ArrayList<StateInstancePath>(_statesMap.keySet());
		Collections.sort((List<?>) list, new ColumnComparator(0));

		for(StateInstancePath state : list)
		{
			sb.append(state + " \n");
			int step = _statesMap.get(state).size() - 1;
			sb.append("\t{" + step + "}=" + _statesMap.get(state).get(step) + " \n");
		}
		return sb.toString();
	}

	/**
	 * @param stateInstancePath
	 * @return
	 */
	public AValue getLastValueFor(String stateInstancePath)
	{
		StateInstancePath statePath = new StateInstancePath(stateInstancePath);
		List<AValue> values = _statesMap.get(statePath);
		return values.get(values.size() - 1);
	}

	/**
	 * @return
	 */
	public int getNumberOfStates()
	{
		for(StateInstancePath state : _statesMap.keySet())
		{
			return _statesMap.get(state).size();
		}
		return 0;
	}

	/**
	 * @param numberOfStatesToRemove
	 */
	public void removeOldestStates(int numberOfStatesToRemove)
	{
		for(StateInstancePath state : _statesMap.keySet())
		{
			for(int i = 0; i < numberOfStatesToRemove; i++)
			{
				_statesMap.get(state).remove(0);
			}
		}
	}

	/**
	 * @param stateSet
	 */
	public void appendStateSet(StateSet stateSet)
	{
		for(StateInstancePath state : stateSet.getStatesMap().keySet())
		{
			for(AValue value : stateSet.getStateValues(state))
			{
				addStateValue(state, value);
			}
		}
	}

	/**
	 * This method removes from this StateSet the oldest timestep for every state and returns them as a new StateSet
	 * 
	 * @return
	 */
	public StateSet pullOldestStateSet()
	{
		StateSet oldestStateSet = new StateSet(_modelId);
		for(StateInstancePath state : _statesMap.keySet())
		{
			if(_statesMap.get(state).size() > 0)
			{
				oldestStateSet.addStateValue(state, _statesMap.get(state).get(0));
				_statesMap.get(state).remove(0);
			}
		}
		return oldestStateSet;
	}

	/**
	 * Returns false if there is at least a state with at least a timestep
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		for(StateInstancePath state : _statesMap.keySet())
		{
			if(_statesMap.get(state).size() > 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * @param state
	 * @param geppettoValues
	 */
	public void addStateValues(StateInstancePath state, List<AValue> geppettoValues)
	{
		if(!_statesMap.containsKey(state))
		{
			_statesMap.put(state, geppettoValues);
		}
		else
		{
			_statesMap.get(state).addAll(geppettoValues);
		}
	}
}
