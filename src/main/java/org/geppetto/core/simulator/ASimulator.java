/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011, 2013 OpenWorm.
 * http://openworm.org
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *     	OpenWorm - http://openworm.org/people.html
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/

package org.geppetto.core.simulator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.VariableList;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.state.StateTreeRoot;
import org.geppetto.core.simulation.ISimulatorCallbackListener;

/**
 * @author matteocantarelli
 * 
 */
public abstract class ASimulator implements ISimulator
{

	protected List<IModel> _models;

	private ISimulatorCallbackListener _listener;

	private boolean _initialized = false;

	private boolean _watching = false;

	protected StateTreeRoot _stateTree;

	private VariableList _forceableVariables=new VariableList();

	private VariableList _watchableVariables=new VariableList();
	
	private Set<String> _watchList = new HashSet<String>();

	private boolean _watchListModified=false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#initialize(org.geppetto.core.model.IModel, org.geppetto.core.simulation.ISimulatorCallbackListener)
	 */
	@Override
	public void initialize(List<IModel> models, ISimulatorCallbackListener listener) throws GeppettoInitializationException, GeppettoExecutionException
	{
		setListener(listener);
		_models = models;
		_initialized = true;
	}

	/**
	 * @return
	 */
	public ISimulatorCallbackListener getListener()
	{
		return _listener;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#addWatchVariables(java.util.List)
	 */
	@Override
	public void addWatchVariables(List<String> variableNames)
	{
		_watchList.addAll(variableNames);
		_watchListModified=true;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#startWatch()
	 */
	@Override
	public void startWatch()
	{
		_watching = true;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#stopWatch()
	 */
	@Override
	public void stopWatch()
	{
		_watching = false;

		// reset variable-watch branch of the state tree
		_stateTree.flushSubTree(StateTreeRoot.SUBTREE.WATCH_TREE);
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#clearWatchVariables()
	 */
	@Override
	public void clearWatchVariables()
	{
		_watchList.clear();
	}

	/**
	 * @return
	 */
	public boolean isWatching()
	{
		return _watching;
	}
	
	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#getForceableVariables()
	 */
	@Override
	public VariableList getForceableVariables()
	{
		return _forceableVariables;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#getWatchableVariables()
	 */
	@Override
	public VariableList getWatchableVariables()
	{
		return _watchableVariables;
	}
	
	/**
	 * @return
	 */
	protected Collection<String> getWatchList()
	{
		return _watchList;
	}
	
	/**
	 * @return
	 */
	protected boolean watchListModified()
	{
		return _watchListModified;
	}
	
	/**
	 * @param watchListModified
	 */
	protected void watchListModified(boolean watchListModified)
	{
		_watchListModified=watchListModified;
		
	}

}
