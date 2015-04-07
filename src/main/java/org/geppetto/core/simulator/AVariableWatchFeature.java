package org.geppetto.core.simulator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geppetto.core.data.model.SimpleVariable;
import org.geppetto.core.data.model.VariableList;
import org.geppetto.core.features.IVariableWatchFeature;
import org.geppetto.core.services.GeppettoFeature;

public class AVariableWatchFeature implements IVariableWatchFeature{

	private VariableList _watchableVariables = new VariableList();
	
	private boolean _watching = false;

	protected int _currentRecordingIndex = 0;

	private GeppettoFeature _type = GeppettoFeature.VARIALE_WATCH_FEATURE;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#addWatchVariables(java.util.List)
	 */
	@Override
	public void addWatchVariables(List<String> variableNames)
	{
		for(String s : variableNames){
			SimpleVariable var = new SimpleVariable();
			var.setName(s);
			_watchableVariables.getVariables().add(var);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#stopWatch()
	 */
	@Override
	public void stopWatch()
	{
		_watching = false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#startWatch()
	 */
	@Override
	public void startWatch()
	{
		_watching = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#clearWatchVariables()
	 */
	@Override
	public void clearWatchVariables()
	{
		_watchableVariables.getVariables().clear();
	}

	/**
	 * @return
	 */
	@Override
	public boolean isWatching()
	{
		return _watching;
	}
	
	@Override
	public VariableList getWatcheableVariables()
	{
		return _watchableVariables;
	}

	@Override
	public GeppettoFeature getType() {
		return _type ;
	}
}
