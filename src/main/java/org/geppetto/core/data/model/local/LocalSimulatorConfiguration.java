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

import java.util.Map;

import org.geppetto.core.data.model.ISimulatorConfiguration;

public class LocalSimulatorConfiguration implements ISimulatorConfiguration
{
	private long id;

	private String simulatorId;

	private String conversionServiceId;

	private float timestep;

	private float length;

	private Map<String, String> parameters;

	public LocalSimulatorConfiguration(long id, String simulatorId, String conversionServiceId, float timestep, float length, Map<String, String> parameters)
	{
		this.id = id;
		this.simulatorId = simulatorId;
		this.conversionServiceId = conversionServiceId;
		this.timestep = timestep;
		this.length = length;
		this.parameters = parameters;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public String getSimulatorId()
	{
		return simulatorId;
	}

	@Override
	public String getConversionServiceId()
	{
		return conversionServiceId;
	}

	@Override
	public float getTimestep()
	{
		return timestep;
	}

	@Override
	public float getLength()
	{
		return length;
	}

	@Override
	public Map<String, String> getParameters()
	{
		return parameters;
	}

	@Override
	public void setLength(float length)
	{
		this.length = length;
	}

	@Override
	public void setTimestep(float timestep)
	{
		this.timestep = timestep;
	}

	@Override
	public void setSimulatorId(String simulatorId)
	{
		this.simulatorId = simulatorId;
	}

	@Override
	public void setConversionServiceId(String conversionServiceId)
	{
		this.conversionServiceId = conversionServiceId;
	}

	@Override
	public void setParameters(Map<String, String> parameters)
	{
		this.parameters = parameters;
	}

	@Override
	public void setId(long id)
	{
		this.id = id;
	}

}
