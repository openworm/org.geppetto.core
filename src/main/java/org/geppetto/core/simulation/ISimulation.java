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

package org.geppetto.core.simulation;

import java.net.URL;
import java.util.List;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.VariableList;
import org.geppetto.core.data.model.WatchList;

/**
 * @author matteocantarelli
 *
 */
public interface ISimulation {

	/**
	 * @param simConfigURL
	 * @param simulationListener 
	 */
	void init(URL simConfigURL, ISimulationCallbackListener simulationListener) throws GeppettoInitializationException;
	
	/**
	 * Different approach to initializing the simulation. 
	 * 
	 * @param simulationConfig - Configuration parameters of simulation
	 * @param simulationListener
	 * @throws GeppettoInitializationException
	 */
	void init(String simulationConfig, ISimulationCallbackListener simulationListener) throws GeppettoInitializationException;
	
	/**
	 * 
	 */
	void start() throws GeppettoExecutionException;
	
	/**
	 * 
	 */
	void pause() throws GeppettoExecutionException;
	
	/**
	 * 
	 */
	void stop() throws GeppettoExecutionException;
	
	
	/**
	 * 
	 */
	boolean isRunning();
	
	/**
	 * Return the simulation's configuration.
	 * 
	 * @param simURL - Location of Simulation
	 * @return
	 * @throws GeppettoInitializationException 
	 */
	String getSimulationConfig(URL simURL) throws GeppettoInitializationException;
	
	/**
	 * Returns a list of watchable variables for the entire simulation
	 * */
	VariableList listWatchableVariables();
	
	/**
	 * Returns a list of forceable variables for the entire simulation
	 * */
	VariableList listForceableVariables();
	
	int getSimulatorCapacity(); 
	
	String getSimulatorName();
	
	/**
	 * Sets variables to be watched
	 * @throws GeppettoExecutionException 
	 * */
	void addWatchLists(List<WatchList> list) throws GeppettoExecutionException;
	
	/**
	 * Starts recording watched variables values 
	 * */
	void startWatch();
	
	
	/**
	 * Number of connections allowed on simulation
	 *
	 * Starts recording watched variables values 
	 * */
	void stopWatch();
	
	/**
	 * Starts recording watched variables values
	 * */
	void clearWatchLists();
	
	/**
	 * Gets existing watch lists  
	 * */
	List<WatchList> getWatchLists();
	
	List<URL> getScripts();	
}
