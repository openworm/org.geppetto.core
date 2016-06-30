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
import org.geppetto.core.data.model.IParameter;
import org.geppetto.core.data.model.IPersistedData;
import org.geppetto.core.data.model.ISimulationResult;
import org.geppetto.core.data.model.ISimulatorConfiguration;
import org.geppetto.core.data.model.IUser;
import org.geppetto.core.data.model.IUserGroup;
import org.geppetto.core.data.model.PersistedDataType;
import org.geppetto.core.data.model.ResultsFormat;
import org.geppetto.core.data.model.UserPrivileges;

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
	
	IUserGroup getUserGroupById(long id);

	IGeppettoProject getGeppettoProjectById(long id);

	List<? extends IUser> getAllUsers();

	Collection<? extends IGeppettoProject> getAllGeppettoProjects();

	Collection<? extends IGeppettoProject> getGeppettoProjectsForUser(String login);

	IGeppettoProject getProjectFromJson(Gson gson, String json);

	IGeppettoProject getProjectFromJson(Gson gson, Reader json);

	List<? extends IExperiment> getExperimentsForProject(long projectId);

	ISimulationResult newSimulationResult(String parameterPath, IPersistedData results, ResultsFormat format);

	void addWatchedVariable(IAspectConfiguration found, String instancePath);

	IPersistedData newPersistedData(URL url, PersistedDataType type);

	IParameter newParameter(String parameterPath, String value);

	IExperiment newExperiment(String name, String description, IGeppettoProject project);

	IUser newUser(String name, String password, boolean persistent, IUserGroup group);
	
	IUserGroup newUserGroup(String name, List<UserPrivileges> privileges, long spaceAllowance, long timeAllowance);

	IUser updateUser(IUser user, String password);

	IAspectConfiguration newAspectConfiguration(IExperiment experiment, String instancePath, ISimulatorConfiguration simulatorConfiguration);

	ISimulatorConfiguration newSimulatorConfiguration(String simulator, String conversionService, long timestep, long length);

	void addGeppettoProject(IGeppettoProject project, IUser user);

	Object deleteGeppettoProject(long id, IUser user);

	Object deleteExperiment(IExperiment experiment);

	void clearWatchedVariables(IAspectConfiguration aspectConfig);

	void saveEntity(Object entity);

	void saveEntity(IExperiment entity);

	void saveEntity(IGeppettoProject entity);

	IExperiment cloneExperiment(String name, String description,
			IGeppettoProject project, IExperiment originalExperiment);

}