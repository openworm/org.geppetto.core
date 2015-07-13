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

package org.geppetto.core.data;

import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.IInstancePath;
import org.geppetto.core.data.model.IParameter;
import org.geppetto.core.data.model.IPersistedData;
import org.geppetto.core.data.model.ISimulationResult;
import org.geppetto.core.data.model.ISimulatorConfiguration;
import org.geppetto.core.data.model.IUser;
import org.geppetto.core.data.model.PersistedDataType;
import org.geppetto.core.data.model.ResultsFormat;
import org.geppetto.core.model.runtime.ANode;

import com.google.gson.Gson;

/**
 * This interface contains methods to deal with the persisted data model of Geppetto this includes fetching stuff from the database (or whatever data source) and adding stuff to the database
 * 
 * @author dandromerecschi
 * @author matteocantarelli
 * 
 */
public interface IGeppettoDataManager
{
	String getName();

	boolean isDefault();

	IUser getUserByLogin(String login);
	
	IGeppettoProject getGeppettoProjectById(long id);

	List<? extends IUser> getAllUsers();

	Collection<? extends IGeppettoProject> getAllGeppettoProjects();

	Collection<? extends IGeppettoProject> getGeppettoProjectsForUser(String login);
	
	IGeppettoProject getProjectFromJson(Gson gson, String json);

	IGeppettoProject getProjectFromJson(Gson gson, Reader json);

	List<? extends IExperiment> getExperimentsForProject(long projectId);

	
	ISimulationResult newSimulationResult(IInstancePath parameterPath, IPersistedData results, ResultsFormat format);
	
	void addWatchedVariable(IAspectConfiguration found, IInstancePath instancePath);
	
	IInstancePath newInstancePath(ANode aspectNode);

	IPersistedData newPersistedData(URL url, PersistedDataType type);

	IParameter newParameter(IInstancePath parameterPath, String value);
	
	IInstancePath newInstancePath(String entityPath, String aspectPath, String localPath);

	IExperiment newExperiment(String name, String description, IGeppettoProject project);
	
	IUser newUser(String name, String password, boolean persistent);
	
	IAspectConfiguration newAspectConfiguration(IExperiment experiment, IInstancePath instancePath, ISimulatorConfiguration simulatorConfiguration);

	ISimulatorConfiguration newSimulatorConfiguration(String simulator, String conversionService, long timestep, long length);
	
	void addGeppettoProject(IGeppettoProject project, IUser user);
	
	Object deleteGeppettoProject(long id, IUser user);

	Object deleteExperiment(IExperiment experiment);


	void clearWatchedVariables(IAspectConfiguration aspectConfig);
	

	void saveEntity(Object entity);

	void saveEntity(IExperiment entity);

	void saveEntity(IGeppettoProject entity);



}