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

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.data.model.IParameter;

public class LocalAspectConfiguration implements IAspectConfiguration
{
	private long id;

	private LocalInstancePath aspect = new LocalInstancePath(id, null, null, null);

	private List<LocalInstancePath> watchedVariables = new ArrayList<LocalInstancePath>();

	private List<LocalParameter> modelParameters = new ArrayList<LocalParameter>();

	private LocalSimulatorConfiguration simulatorConfiguration = new LocalSimulatorConfiguration(id, null, null, id, id, null);

	public LocalAspectConfiguration(long id, LocalInstancePath aspect, List<LocalInstancePath> watchedVariables, List<LocalParameter> modelParameter, LocalSimulatorConfiguration simulatorConfiguration)
	{
		this.id = id;
		this.aspect = aspect;
		this.watchedVariables = watchedVariables;
		this.modelParameters = modelParameter;
		this.simulatorConfiguration = simulatorConfiguration;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public LocalInstancePath getAspect()
	{
		return aspect;
	}

	@Override
	public List<LocalInstancePath> getWatchedVariables()
	{
		return watchedVariables;
	}

	@Override
	public List<LocalParameter> getModelParameter()
	{
		return modelParameters;
	}

	@Override
	public LocalSimulatorConfiguration getSimulatorConfiguration()
	{
		return simulatorConfiguration;
	}

	@Override
	public void addModelParameter(IParameter modelParameter)
	{
		if(modelParameters == null)
		{
			modelParameters = new ArrayList<LocalParameter>();
		}
		if(modelParameter instanceof LocalParameter)
		{
			modelParameters.add((LocalParameter) modelParameter);
		}
	}

	@Override
	public void setId(long id)
	{
		id = id;
	}

}