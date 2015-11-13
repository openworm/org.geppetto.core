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

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.features.IVariableWatchFeature;
import org.geppetto.core.services.GeppettoFeature;

/**
 * Abstract feature class for variable watch
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 *
 */
public class AVariableWatchFeature implements IVariableWatchFeature
{

	protected int _currentRecordingIndex = 0;
	private boolean _watchListModified;
	private GeppettoFeature _type = GeppettoFeature.VARIABLE_WATCH_FEATURE;
	List<String> watchedVariables = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#addWatchVariables(java.util.List)
	 */
	@Override
	public void setWatchedVariables(List<String> variableNames)
	{
		this._watchListModified = true;
		for(String s : variableNames)
		{
			this.watchedVariables.add(s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#clearWatchVariables()
	 */
	@Override
	public void clearWatchVariables()
	{
		this._watchListModified = true;
	}

	@Override
	public GeppettoFeature getType()
	{
		return _type;
	}

	@Override
	public boolean watchListModified()
	{
		return _watchListModified;
	}

	@Override
	public void setWatchListModified(boolean modified)
	{
		_watchListModified = modified;
	}

	@Override
	public List<String> getWatchedVariables()
	{
		return this.watchedVariables;
	}

}
