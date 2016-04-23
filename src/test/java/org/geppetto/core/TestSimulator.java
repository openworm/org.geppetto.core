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
package org.geppetto.core;

import java.util.ArrayList;
import java.util.Arrays;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.features.IFeature;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.GeppettoFeature;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.core.simulation.ISimulatorCallbackListener;
import org.geppetto.core.simulator.ASimulator;
import org.geppetto.model.DomainModel;
import org.geppetto.model.ExperimentState;
import org.geppetto.model.ModelFormat;

/**
 * Dummy simulator used for testing purposes 
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class TestSimulator extends ASimulator
{

	@Override
	public void simulate() throws GeppettoExecutionException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(DomainModel model, IAspectConfiguration aspectConfiguration, ExperimentState experimentState,ISimulatorCallbackListener listener, GeppettoModelAccess modelAccess) throws GeppettoInitializationException, GeppettoExecutionException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInitialized()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "test";
	}

	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInitialized(boolean initialized) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTimeStepUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerGeppettoService()
	{
		ServicesRegistry.registerSimulatorService(this, new ArrayList<ModelFormat>(Arrays.asList(ServicesRegistry.registerModelFormat("TEST"))));
	}

	@Override
	public boolean isSupported(GeppettoFeature feature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IFeature getFeature(GeppettoFeature feature) {
		// TODO Auto-generated method stub
		return null;
	}

}
