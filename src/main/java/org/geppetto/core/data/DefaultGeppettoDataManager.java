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
import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.IUser;
import org.geppetto.core.data.model.local.LocalGeppettoProject;
import org.geppetto.core.data.model.local.LocalUser;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;

public class DefaultGeppettoDataManager implements IGeppettoDataManager
{

	private static final List<IGeppettoProject> PROJECTS = new ArrayList<>();

	static
	{
		loadGeppettoProjects();
	}

	public String getName()
	{
		return "Default data manager";
	}

	public boolean isDefault()
	{
		return true;
	}

	public IUser getCurrentUser()
	{
		return new LocalUser(1, "guest", "guest", "guest", new ArrayList<LocalGeppettoProject>(), 0, 0);
	}

	public IUser getUserByLogin(String login)
	{
		return new LocalUser(1, login, login, login, new ArrayList<LocalGeppettoProject>(), 0, 0);
	}

	public IGeppettoProject getGeppettoProjectById(long id)
	{
		for(IGeppettoProject project : PROJECTS)
		{
			if(project.getId() == id)
			{
				return project;
			}
		}
		return null;
	}

	public List<IGeppettoProject> getAllGeppettoProjects()
	{
		return PROJECTS;
	}

	public List<IGeppettoProject> getGeppettoProjectsForUser(String login)
	{
		return PROJECTS;
	}

	public void createParameter(String name, String value)
	{
	}

	public Object deleteGeppettoProject(IGeppettoProject project)
	{
		return new JsonRequestException("Cannot delete project", HttpStatus.BAD_REQUEST);
	}

	private static void loadGeppettoProjects()
	{
		for(int i = 1; i <= 10; i++)
		{
			InputStream stream = DefaultGeppettoDataManager.class.getResourceAsStream("/project/" + i + ".json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			IGeppettoProject project = new Gson().fromJson(reader, LocalGeppettoProject.class);
			PROJECTS.add(project);
		}
	}
}
