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

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.IUser;
import org.geppetto.core.data.model.PersistedDataType;
import org.geppetto.core.data.model.local.LocalExperiment;
import org.geppetto.core.data.model.local.LocalGeppettoProject;
import org.geppetto.core.data.model.local.LocalPersistedData;
import org.geppetto.core.data.model.local.LocalUser;

public class DefaultGeppettoDataManager implements IGeppettoDataManager
{
	private static final List<LocalGeppettoProject> PROJECTS = new ArrayList<>();

	static
	{
		PROJECTS.add(buildProject(1, "LEMS Sample Hodgkin-Huxley Neuron", "https://raw.github.com/openworm/org.geppetto.samples/master/LEMS/SingleComponentHH/GEPPETTO.xml"));
		PROJECTS.add(buildProject(2, "PCISPH Small Liquid Scene", "https://raw.github.com/openworm/org.geppetto.samples/master/SPH/LiquidSmall/GEPPETTO.xml"));
		PROJECTS.add(buildProject(3, "PCISPH Small Elastic Scene", "https://raw.github.com/openworm/org.geppetto.samples/master/SPH/ElasticSmall/GEPPETTO.xml"));
		PROJECTS.add(buildProject(4, "OpenWorm C.elegans muscle model", "https://raw.githubusercontent.com/openworm/org.geppetto.samples/development/LEMS/MuscleModel/GEPPETTO.xml"));
		PROJECTS.add(buildProject(5, "Primary Auditory Cortex Network", "https://raw.github.com/openworm/org.geppetto.samples/master//NeuroML/ACnet2/GEPPETTO.xml"));
		PROJECTS.add(buildProject(6, "C302 Experimental network of integrate and fire neurons", "https://raw.githubusercontent.com/openworm/org.geppetto.samples/master/LEMS/C302/GEPPETTO.xml"));
		PROJECTS.add(buildProject(7, "NeuroML Purkinje Cell (No Simulation)", "https://raw.github.com/openworm/org.geppetto.samples/master/NeuroML/Purkinje/GEPPETTO.xml"));
		PROJECTS.add(buildProject(8, "NeuroML C.elegans PVDR Neuron (No Simulation)", "https://raw.github.com/openworm/org.geppetto.samples/master/NeuroML/PVDR/GEPPETTO.xml"));
		PROJECTS.add(buildProject(9, "EyeWire Ganglion Cell (No Simulation)", "https://raw.github.com/openworm/org.geppetto.samples/master/obj/Eyewire/GEPPETTO.xml"));
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
		for(LocalGeppettoProject project : PROJECTS)
		{
			if(project.getId() == id)
			{
				return project;
			}
		}
		return null;
	}

	public List<LocalGeppettoProject> getAllGeppettoProjects()
	{
		return PROJECTS;
	}

	public List<LocalGeppettoProject> getGeppettoProjectsForUser(String login)
	{
		return PROJECTS;
	}

	public void createParameter(String name, String value)
	{
	}

	private static LocalGeppettoProject buildProject(long id, String name, String url)
	{
		LocalPersistedData persistedData = new LocalPersistedData(id, url, PersistedDataType.GEPPETTO_PROJECT);
		return new LocalGeppettoProject(id, name, new ArrayList<LocalExperiment>(), persistedData);
	}
}
