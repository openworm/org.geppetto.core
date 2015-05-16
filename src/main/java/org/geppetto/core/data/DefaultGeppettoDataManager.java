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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geppetto.core.data.model.ExperimentStatus;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.IUser;
import org.geppetto.core.data.model.local.LocalAspectConfiguration;
import org.geppetto.core.data.model.local.LocalExperiment;
import org.geppetto.core.data.model.local.LocalGeppettoProject;
import org.geppetto.core.data.model.local.LocalSimulationResult;
import org.geppetto.core.data.model.local.LocalUser;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DefaultGeppettoDataManager implements IGeppettoDataManager
{

	private static final List<LocalGeppettoProject> projects = new ArrayList<>();

	private static final List<IUser> users = new ArrayList<>();

	static
	{
		loadGeppettoProjects();
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#getName()
	 */
	@Override
	public String getName()
	{
		return "Default data manager";
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#isDefault()
	 */
	@Override
	public boolean isDefault()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#getUserByLogin(java.lang.String)
	 */
	@Override
	public IUser getUserByLogin(String login)
	{
		IUser user = new LocalUser(1, login, login, login, new ArrayList<LocalGeppettoProject>(), 0, 0);
		return user;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#getGeppettoProjectById(long)
	 */
	@Override
	public IGeppettoProject getGeppettoProjectById(long id)
	{
		for(IGeppettoProject project : projects)
		{
			if(project.getId() == id)
			{
				return project;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#getAllUsers()
	 */
	@Override
	public List<IUser> getAllUsers()
	{
		return users;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#getAllGeppettoProjects()
	 */
	@Override
	public List<LocalGeppettoProject> getAllGeppettoProjects()
	{
		return projects;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#getGeppettoProjectsForUser(java.lang.String)
	 */
	@Override
	public List<LocalGeppettoProject> getGeppettoProjectsForUser(String login)
	{
		return projects;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#getExperimentsForProject(long)
	 */
	@Override
	public List<? extends IExperiment> getExperimentsForProject(long projectId)
	{
		IGeppettoProject project = getGeppettoProjectById(projectId);
		return project.getExperiments();
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#createParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public void createParameter(String name, String value)
	{
		//TODO
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#newExperiment(java.lang.String, java.lang.String)
	 */
	@Override
	public IExperiment newExperiment(String name, String description)
	{
		return new LocalExperiment(0, new ArrayList<LocalAspectConfiguration>(), name, description, new Date(), new Date(), ExperimentStatus.DESIGN, new ArrayList<LocalSimulationResult>(),
				new Date(), new Date());
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#newUser(java.lang.String)
	 */
	@Override
	public IUser newUser(String name)
	{
		return new LocalUser(0, name, name, name, projects, 0, 0);
	}
	
	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#addGeppettoProject(org.geppetto.core.data.model.IGeppettoProject)
	 */
	@Override
	public void addGeppettoProject(IGeppettoProject project)
	{
		if(project instanceof LocalGeppettoProject)
		{
			projects.add((LocalGeppettoProject) project);
		}
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#deleteGeppettoProject(org.geppetto.core.data.model.IGeppettoProject)
	 */
	@Override
	public Object deleteGeppettoProject(IGeppettoProject project)
	{
		return new JsonRequestException("Cannot delete project", HttpStatus.BAD_REQUEST);
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#deleteExperiment(org.geppetto.core.data.model.IExperiment)
	 */
	@Override
	public Object deleteExperiment(IExperiment experiment)
	{
		return new JsonRequestException("Cannot delete experiment", HttpStatus.BAD_REQUEST);
	}

	/**
	 * 
	 */
	private static void loadGeppettoProjects()
	{
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
		{
			@Override
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
			{
				return new Date(json.getAsJsonPrimitive().getAsLong());
			}
		});
		Gson gson = builder.create();
		for(int i = 1; i <= 10; i++)
		{
			InputStream stream = DefaultGeppettoDataManager.class.getResourceAsStream("/project/" + i + ".json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

			LocalGeppettoProject project = gson.fromJson(reader, LocalGeppettoProject.class);
			projects.add(project);
		}
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#getProjectFromJson(com.google.gson.Gson, java.lang.String)
	 */
	@Override
	public IGeppettoProject getProjectFromJson(Gson gson, String json)
	{
		return gson.fromJson(json, LocalGeppettoProject.class);
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#getProjectFromJson(com.google.gson.Gson, java.io.Reader)
	 */
	@Override
	public IGeppettoProject getProjectFromJson(Gson gson, Reader json)
	{
		return gson.fromJson(json, LocalGeppettoProject.class);
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.data.IGeppettoDataManager#clearWatchedVariables(org.geppetto.core.data.model.IAspectConfiguration)
	 */
	@Override
	public void clearWatchedVariables(IAspectConfiguration aspectConfig)
	{
		// TODO Auto-generated method stub
		
	}

}
