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

package org.geppetto.core.data.model.local;

import java.util.Date;
import java.util.List;

import org.geppetto.core.data.model.ExperimentStatus;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.ISimulationResult;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "parentProject" })
public class LocalExperiment implements IExperiment
{
	private long id;

	private List<LocalAspectConfiguration> aspectConfigurations;

	private String name;

	private String description;

	private Date creationDate;

	private Date lastModified;

	private ExperimentStatus status;

	private List<LocalSimulationResult> simulationResults;

	private Date startDate;

	private Date endDate;

	@JsonIgnore
	private transient IGeppettoProject parentProject;

	public LocalExperiment(long id, List<LocalAspectConfiguration> aspectConfigurations, String name, String description, Date creationDate, Date lastModified, ExperimentStatus status,
			List<LocalSimulationResult> simulationResults, Date startDate, Date endDate, IGeppettoProject project)
	{
		this.id = id;
		this.aspectConfigurations = aspectConfigurations;
		this.name = name;
		this.description = description;
		this.creationDate = creationDate;
		this.lastModified = lastModified;
		this.status = status;
		this.simulationResults = simulationResults;
		this.startDate = startDate;
		this.endDate = endDate;
		this.parentProject = project;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public List<LocalAspectConfiguration> getAspectConfigurations()
	{
		return aspectConfigurations;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public Date getCreationDate()
	{
		return creationDate;
	}

	@Override
	public Date getLastModified()
	{
		return lastModified;
	}

	public void addSimulationResult(ISimulationResult result)
	{
		if(result instanceof LocalSimulationResult)
		{
			simulationResults.add((LocalSimulationResult) result);
		}
	}

	@Override
	public List<LocalSimulationResult> getSimulationResults()
	{
		return simulationResults;
	}

	@Override
	public ExperimentStatus getStatus()
	{
		return status;
	}

	@Override
	public synchronized void setStatus(ExperimentStatus status)
	{
		this.status = status;
	}

	@Override
	public Date getStartDate()
	{
		return startDate;
	}

	@Override
	public Date getEndDate()
	{
		return endDate;
	}

	@Override
	@JsonIgnore
	public IGeppettoProject getParentProject()
	{
		return parentProject;
	}

	@Override
	@JsonIgnore
	public void setParentProject(IGeppettoProject project)
	{
		this.parentProject = project;

	}
}