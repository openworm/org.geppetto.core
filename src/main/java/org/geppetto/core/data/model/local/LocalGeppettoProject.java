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

import java.util.List;

import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.IView;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LocalGeppettoProject implements IGeppettoProject
{
	private long id;

	private String name;

	private List<LocalExperiment> experiments;

	private LocalPersistedData geppettoModel;

	private long activeExperimentId;

	@JsonIgnore
	private transient boolean volatileProject;

	private List<String> modelReferences;

	private boolean isPublic = false;

	private LocalView view;

	public LocalGeppettoProject(long id, String name, List<LocalExperiment> experiments, LocalPersistedData geppettoModel)
	{
		this.id = id;
		this.name = name;
		this.experiments = experiments;
		this.geppettoModel = geppettoModel;
		this.activeExperimentId = -1;
	}

	@Override
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public List<LocalExperiment> getExperiments()
	{
		return experiments;
	}

	@Override
	public LocalPersistedData getGeppettoModel()
	{
		return geppettoModel;
	}

	@Override
	public boolean equals(Object obj)
	{
		return id == ((LocalGeppettoProject) obj).id;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public boolean isVolatile()
	{
		return this.volatileProject;
	}

	@Override
	public void setVolatile(boolean volatileProject)
	{
		this.volatileProject = volatileProject;
	}

	@Override
	public long getActiveExperimentId()
	{
		return this.activeExperimentId;
	}

	@Override
	public void setActiveExperimentId(long experimentId)
	{
		this.activeExperimentId = experimentId;
	}

	@Override
	public boolean isPublic()
	{
		return this.isPublic;
	}

	public void setPublic(boolean b)
	{
		this.isPublic = b;
	}

	/**
	 * Operation not supported on local experiment
	 */
	@Override
	public void setView(IView view)
	{
		this.view = (LocalView) view;
	}

	@Override
	public IView getView()
	{
		return this.view;
	}
}