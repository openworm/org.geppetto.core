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

package org.geppetto.core.simulation;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;

/**
 * @author matteocantarelli
 *
 */
public interface ISimulation {

	/**
	 * Initialize the simulation from a URL which points to a simulation configuration
	 * 
	 * @param simConfigURL
	 * @param simulationListener 
	 */
	void init(URL simConfigURL, String requestID,  IProjectManagerCallbackListener simulationListener) throws GeppettoInitializationException;
	
	/**
	 * Initialize the simulation from a string containing the simulation configuration
	 * 
	 * @param simulationConfig - Configuration parameters of simulation
	 * @param simulationListener
	 * @throws GeppettoInitializationException
	 */
	void init(String simulationConfig, String requestID, IProjectManagerCallbackListener simulationListener) throws GeppettoInitializationException;
	
	/**
	 * @param requestID 
	 * 
	 */
	void start(String requestID) throws GeppettoExecutionException;
	
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
	
	String getSimulatorName();
	
	/**
	 * Sets variables to be watched
	 * @throws GeppettoExecutionException 
	 * @throws GeppettoInitializationException 
	 * */
	void setWatchedVariables(List<String> watchedVariables) throws GeppettoExecutionException, GeppettoInitializationException;

	
	/**
	 * Clear watched variables
	 * */
	void clearWatchLists();
	
	List<URL> getScripts();	
	
	int getSimulationCapacity();

	
	String getModelTree(String aspectID);
	
	String getSimulationTree(String aspectID);
	
	String writeModel(String aspectID, String format);

	boolean setParameters(String model, Map<String, String> parameters);
}
