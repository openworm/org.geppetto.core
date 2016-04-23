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

import org.geppetto.core.data.model.ISimulationResult;
import org.geppetto.core.data.model.ResultsFormat;

public class LocalSimulationResult implements ISimulationResult
{
	private long id;

	private String simulatedInstance;

	private LocalPersistedData result;

	private ResultsFormat format;

	public LocalSimulationResult(long id, String simulatedInstance, LocalPersistedData result, ResultsFormat format)
	{
		this.id = id;
		this.simulatedInstance = simulatedInstance;
		this.result = result;
		this.format = format;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public LocalPersistedData getResult()
	{
		return result;
	}

	@Override
	public String getSimulatedInstance()
	{
		return simulatedInstance;
	}

	@Override
	public void setId(long id)
	{
		this.id = id;
	}

	@Override
	public ResultsFormat getFormat()
	{
		return this.format;
	}

}