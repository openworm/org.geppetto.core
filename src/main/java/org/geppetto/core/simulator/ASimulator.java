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

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.manager.Scope;
import org.geppetto.core.services.AService;
import org.geppetto.core.simulation.ISimulatorCallbackListener;
import org.geppetto.model.DomainModel;
import org.geppetto.model.ExperimentState;
import org.geppetto.model.values.Pointer;

/**
 * @author matteocantarelli
 * 
 */
public abstract class ASimulator extends AService implements ISimulator
{

	private ISimulatorCallbackListener listener;

	private boolean initialized = false;

	private String timeStepUnit = "ms";

	private double runtime;

	protected DomainModel model;

	protected IAspectConfiguration aspectConfiguration;

	protected ExperimentState experimentState;

	public ASimulator()
	{
		scope = Scope.RUN;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#initialize(org.geppetto.core.model.IModel, org.geppetto.core.simulation.ISimulatorCallbackListener)
	 */
	@Override
	public void initialize(DomainModel model, IAspectConfiguration aspectConfiguration, ExperimentState experimentState, ISimulatorCallbackListener listener) throws GeppettoInitializationException,
			GeppettoExecutionException
	{
		setListener(listener);
		this.model = model;
		this.aspectConfiguration = aspectConfiguration;
		this.runtime = 0;
		this.initialized = true;
		this.experimentState = experimentState;
	}

	/**
	 * @return
	 */
	public ISimulatorCallbackListener getListener()
	{
		return listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#isInitialized()
	 */
	public boolean isInitialized()
	{
		return initialized;
	}

	@Override
	public void setInitialized(boolean initialized)
	{
		this.initialized = initialized;
	}

	/**
	 * @param listener
	 */
	public void setListener(ISimulatorCallbackListener listener)
	{
		this.listener = listener;
	}

	/**
	 * @param timeStepUnit
	 */
	public void setTimeStepUnit(String timeStepUnit)
	{
		this.timeStepUnit = timeStepUnit;
	}

	/**
	 * @param timestep
	 * @param aspect
	 */
	public void advanceTimeStep(double timestep, Pointer pointer)
	{
		runtime += timestep;
	}

	/**
	 * @throws GeppettoExecutionException
	 */
	protected void notifySimulatorHasStepped(Pointer pointer) throws GeppettoExecutionException
	{
		getListener().stepped(pointer);
	}

	@Override
	public double getTime()
	{
		return runtime;
	}

	@Override
	public String getTimeStepUnit()
	{
		return timeStepUnit;
	}
}
