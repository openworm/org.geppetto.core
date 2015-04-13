/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011 - 2015 OpenWorm.
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

import java.util.List;

import org.geppetto.core.data.model.SimpleVariable;
import org.geppetto.core.data.model.VariableList;
import org.geppetto.core.features.IVariableWatchFeature;
import org.geppetto.core.services.GeppettoFeature;

/**
 * Abstract feature class for variable watch
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class AVariableWatchFeature implements IVariableWatchFeature{

	private VariableList _watchableVariables = new VariableList();
	private boolean _watching = false;
	protected int _currentRecordingIndex = 0;
	private GeppettoFeature _type = GeppettoFeature.VARIABLE_WATCH_FEATURE;
	private boolean _watchListModified;

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
		this._watchListModified = true;
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
	
	@Override
	public boolean watchListModified(){
		return _watchListModified;
	}
	
	@Override
	public void setWatchListModified(boolean modified){
		_watchListModified = modified;
	}
}
