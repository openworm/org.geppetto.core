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
