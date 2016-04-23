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

import org.geppetto.core.common.GeppettoInitializationException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class DataManagerServiceCreator
{

	private BundleContext _bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

	private String _type = null;

	public DataManagerServiceCreator(String type)
	{
		super();
		_type = type;
	}

	/**
	 * A method to get a service of a given type
	 * 
	 * @param type
	 * @return
	 * @throws InvalidSyntaxException
	 */
	public IGeppettoDataManager getService() throws GeppettoInitializationException
	{
		IGeppettoDataManager service = null;
		ServiceReference<?>[] sr;
		try
		{
			sr = _bc.getServiceReferences(_type, null);
		}
		catch(InvalidSyntaxException e)
		{
			throw new GeppettoInitializationException(e);
		}
		if(sr != null && sr.length > 0)
		{
			service = (IGeppettoDataManager) _bc.getService(sr[0]);
			for(ServiceReference<?> s : sr)
			{
				if(!((IGeppettoDataManager) _bc.getService(s)).isDefault())
				{
					// TODO Check are we always creating the two of them to decide?
					service = (IGeppettoDataManager) _bc.getService(s);
				}
			}
		}

		return service;
	}

}
