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

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.geppetto.core.data.model.ExperimentStatus;
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
import org.geppetto.core.data.model.local.LocalAspectConfiguration;
import org.geppetto.core.data.model.local.LocalExperiment;
import org.geppetto.core.data.model.local.LocalGeppettoProject;
import org.geppetto.core.data.model.local.LocalParameter;
import org.geppetto.core.data.model.local.LocalPersistedData;
import org.geppetto.core.data.model.local.LocalSimulationResult;
import org.geppetto.core.data.model.local.LocalSimulatorConfiguration;
import org.geppetto.core.data.model.local.LocalUser;
import org.geppetto.core.data.model.local.LocalUserGroup;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;

public class DefaultGeppettoDataManager implements IGeppettoDataManager
{

	Map<Long, LocalGeppettoProject> projects = new ConcurrentHashMap<Long, LocalGeppettoProject>();

	private List<IUser> users = new ArrayList<>();

	private static IUserGroup userGroup = null;

	private volatile static int guestId;

	public DefaultGeppettoDataManager()
	{
		super();
		try
		{
			loadGeppettoProjects();
		}
		catch(IOException e)
		{
			new RuntimeException(e);
		}
		catch(URISyntaxException e)
		{
			new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getName()
	 */
	@Override
	public String getName()
	{
		return "Default data manager";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#isDefault()
	 */
	@Override
	public boolean isDefault()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getUserByLogin(java.lang.String)
	 */
	@Override
	public IUser getUserByLogin(String login)
	{
		List<UserPrivileges> privileges = Arrays.asList(UserPrivileges.READ_PROJECT, UserPrivileges.DOWNLOAD);
		IUserGroup group = new LocalUserGroup("guest", privileges, 0, 0);
		IUser user = new LocalUser(1, login, login, login, login, new ArrayList<LocalGeppettoProject>(), group);

		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getUserGroupById(long)
	 */
	@Override
	public IUserGroup getUserGroupById(long id){
		return getUserGroup();	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getGeppettoProjectById(long)
	 */
	@Override
	public IGeppettoProject getGeppettoProjectById(long id)
	{
		if(projects.containsKey(id))
		{
			return projects.get(id);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getAllUsers()
	 */
	@Override
	public List<IUser> getAllUsers()
	{
		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getAllGeppettoProjects()
	 */
	@Override
	public Collection<LocalGeppettoProject> getAllGeppettoProjects()
	{
		List<LocalGeppettoProject> allProjects = new ArrayList<LocalGeppettoProject>();
		for(LocalGeppettoProject project : projects.values())
		{
			if(!project.isVolatile())
			{
				allProjects.add(project);
			}
		}
		return allProjects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getGeppettoProjectsForUser(java.lang.String)
	 */
	@Override
	public Collection<LocalGeppettoProject> getGeppettoProjectsForUser(String login)
	{
		List<LocalGeppettoProject> allProjects = new ArrayList<LocalGeppettoProject>();
		for(LocalGeppettoProject project : projects.values())
		{
			if(!project.isVolatile())
			{
				allProjects.add(project);
			}
		}
		return allProjects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getExperimentsForProject(long)
	 */
	@Override
	public List<? extends IExperiment> getExperimentsForProject(long projectId)
	{
		IGeppettoProject project = getGeppettoProjectById(projectId);
		return project.getExperiments();
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#createParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public IParameter newParameter(String parameterInstancePath, String value)
	{
		return new LocalParameter(0, parameterInstancePath, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#newExperiment(java.lang.String, java.lang.String)
	 */
	@Override
	public IExperiment newExperiment(String name, String description, IGeppettoProject project)
	{
		LocalExperiment experiment = new LocalExperiment(1, new ArrayList<LocalAspectConfiguration>(), name, description, new Date(), new Date(), ExperimentStatus.DESIGN,
				new ArrayList<LocalSimulationResult>(), new Date(), new Date(), project);
		((LocalGeppettoProject) project).getExperiments().add(experiment);
		return experiment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#newUser(java.lang.String)
	 */
	@Override
	public IUser newUser(String name, String password, boolean persistent, IUserGroup group)
	{
		List<LocalGeppettoProject> list = new ArrayList<LocalGeppettoProject>(projects.values());

		if(group == null)
		{
			List<UserPrivileges> privileges = Arrays.asList(UserPrivileges.READ_PROJECT, UserPrivileges.DOWNLOAD);
			group = new LocalUserGroup("guest", privileges, 0, 0);
		}

		return new LocalUser(0, name, password, name, name, list, group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#addGeppettoProject(org.geppetto.core.data.model.IGeppettoProject)
	 */
	@Override
	public void addGeppettoProject(IGeppettoProject project, IUser user)
	{
		if(project instanceof LocalGeppettoProject)
		{
			projects.put(project.getId(), (LocalGeppettoProject) project);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#deleteGeppettoProject(org.geppetto.core.data.model.IGeppettoProject)
	 */
	@Override
	public Object deleteGeppettoProject(long id, IUser user)
	{
		return new JsonRequestException("Cannot delete a sample project", HttpStatus.BAD_REQUEST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#deleteExperiment(org.geppetto.core.data.model.IExperiment)
	 */
	@Override
	public Object deleteExperiment(IExperiment experiment)
	{
		return new JsonRequestException("Cannot delete experiment from a sample", HttpStatus.BAD_REQUEST);
	}

	/**
	 * @throws URISyntaxException
	 * @throws IOException
	 * 
	 */
	private void loadGeppettoProjects() throws IOException, URISyntaxException
	{

		URL projectFolder = DefaultGeppettoDataManager.class.getResource("/projects/");
		// To retrieve a resource from a JAR file you really need to use getResourceAsStream which doesn't work with the file walker
		if(!projectFolder.toURI().toString().startsWith("jar"))
		{
			FindLocalProjectsVisitor<LocalGeppettoProject> findProjectsVisitor = new FindLocalProjectsVisitor<LocalGeppettoProject>(projects, LocalGeppettoProject.class);
			Files.walkFileTree(Paths.get(projectFolder.toURI()), findProjectsVisitor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getProjectFromJson(com.google.gson.Gson, java.lang.String)
	 */
	@Override
	public IGeppettoProject getProjectFromJson(Gson gson, String json)
	{
		LocalGeppettoProject project = gson.fromJson(json, LocalGeppettoProject.class);
		project.setId(getRandomId());
		project.setVolatile(true);

		// set project as parent for experiments
		for(IExperiment e : project.getExperiments())
		{
			e.setParentProject(project);
		}

		projects.put(project.getId(), project);
		return project;
	}

	private long getRandomId()
	{
		Random rnd = new Random();
		return (long) rnd.nextInt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#getProjectFromJson(com.google.gson.Gson, java.io.Reader)
	 */
	@Override
	public IGeppettoProject getProjectFromJson(Gson gson, Reader json)
	{
		LocalGeppettoProject project = gson.fromJson(json, LocalGeppettoProject.class);
		project.setId(getRandomId());
		project.setVolatile(true);

		// set project as parent for experiments
		for(IExperiment e : project.getExperiments())
		{
			e.setParentProject(project);
		}

		projects.put(project.getId(), project);
		return project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.data.IGeppettoDataManager#clearWatchedVariables(org.geppetto.core.data.model.IAspectConfiguration)
	 */
	@Override
	public void clearWatchedVariables(IAspectConfiguration aspectConfig)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void saveEntity(Object entity)
	{
		// Nothing to do, no DB here
	}

	@Override
	public ISimulationResult newSimulationResult(String parameterPath, IPersistedData results, ResultsFormat format)
	{
		return new LocalSimulationResult(0, parameterPath, (LocalPersistedData) results, format);
	}

	@Override
	public IPersistedData newPersistedData(URL url, PersistedDataType type)
	{
		return new LocalPersistedData(0, url.toString(), type);
	}

	@Override
	public void saveEntity(IExperiment entity)
	{
		// Nothing to do, no DB here
	}

	@Override
	public void saveEntity(IGeppettoProject entity)
	{
		// Nothing to do, no DB here
	}

	@Override
	public IAspectConfiguration newAspectConfiguration(IExperiment experiment, String instancePath, ISimulatorConfiguration simulatorConfiguration)
	{
		LocalAspectConfiguration ac = new LocalAspectConfiguration(0l, instancePath, new ArrayList<String>(), new ArrayList<LocalParameter>(),
				(LocalSimulatorConfiguration) simulatorConfiguration);
		((LocalExperiment) experiment).getAspectConfigurations().add(ac);
		return ac;
	}

	@Override
	public ISimulatorConfiguration newSimulatorConfiguration(String simulator, String conversionService, long timestep, long length)
	{
		return new LocalSimulatorConfiguration(0l, simulator, conversionService, timestep, length, new HashMap<String, String>());
	}

	@Override
	public void addWatchedVariable(IAspectConfiguration aspectConfiguration, String instancePath)
	{
		((LocalAspectConfiguration) aspectConfiguration).getWatchedVariables().add(instancePath);
	}

	@Override
	public IUser updateUser(IUser user, String password)
	{
		// Just return a new user
		return newUser(user.getName(), password, false, null);
	}

	@Override
	public IUserGroup newUserGroup(String name, List<UserPrivileges> privileges, long spaceAllowance, long timeAllowance)
	{
		return new LocalUserGroup(name, privileges, spaceAllowance, timeAllowance);
	}

	/**
	 * @return
	 */
	public static IUser getGuestUser()
	{
		return DataManagerHelper.getDataManager().newUser("Guest " + guestId++, "", false, getUserGroup());
	}

	/**
	 * @return
	 */
	private static IUserGroup getUserGroup()
	{
		if(userGroup == null)
		{
			userGroup = DataManagerHelper.getDataManager().newUserGroup("guest", Arrays.asList(UserPrivileges.READ_PROJECT, UserPrivileges.DOWNLOAD, UserPrivileges.DROPBOX_INTEGRATION),
					1000l * 1000 * 1000, 1000l * 1000 * 1000 * 2);
		}
		return userGroup;
	}

}
