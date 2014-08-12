/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011, 2013 OpenWorm.
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

package org.geppetto.core.model.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author matteocantarelli
 * This class represents an instance of a state in Geppetto
 */
public class StateInstancePath
{
	private String _instancePath;

	public StateInstancePath(String instancePath)
	{
		super();
		this._instancePath = instancePath;
	}
	

	public String getInstancePath()
	{
		return _instancePath;
	}
	
	private List<String> tokenize(String instancePathString)
	{
		StringTokenizer st=new StringTokenizer(instancePathString,".");
		List<String> instancePath=new ArrayList<String>();
		while(st.hasMoreTokens())
		{
			instancePath.add(st.nextToken());
		}
		return instancePath;
	}

	@Override
	public String toString()
	{
		return _instancePath;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_instancePath == null) ? 0 : _instancePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		StateInstancePath other = (StateInstancePath) obj;
		if(_instancePath == null)
		{
			if(other._instancePath != null) return false;
		}
		else if(!_instancePath.equals(other._instancePath)) return false;
		return true;
	}
	

	
	
}
