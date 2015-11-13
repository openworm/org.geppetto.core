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
package org.geppetto.core.library;

import java.util.HashMap;
import java.util.Map;

import org.geppetto.core.model.geppettomodel.GeppettoModel;
import org.geppetto.core.model.typesystem.aspect.IAspect;
import org.geppetto.core.model.typesystem.types.IType;

/**
 * @author matteocantarelli
 *
 */
public class LibraryManager implements ILibraryManager
{

	Map<IAspect, IGeppettoLibrary> libraries;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.library.ILibraryManager#init(org.geppetto.core.model.geppettomodel.GeppettoModel)
	 */
	@Override
	public void init(GeppettoModel model)
	{
		libraries = new HashMap<IAspect, IGeppettoLibrary>();
		// TODO initialise the library from a geppetto model
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.library.ILibraryManager#getTypeByName(org.geppetto.core.model.typesystem.IAspect, java.lang.String)
	 */
	@Override
	public IType getTypeByName(IAspect aspect, String name) throws GeppettoTypeException
	{
		if(libraries.containsKey(aspect))
		{
			return libraries.get(aspect).getTypeByName(name);
		}
		throw new GeppettoTypeException("Type " + name + " not found in the library for aspect " + aspect.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.library.ILibraryManager#getTypeByName(java.lang.String, java.lang.String)
	 */
	@Override
	public IType getTypeByName(String aspectId, String name) throws GeppettoTypeException
	{
		for(IAspect aspect : libraries.keySet())
		{
			if(aspect.getId().equals(aspectId))
			{
				return getTypeByName(aspect, name);
			}
		}
		throw new GeppettoTypeException("Type " + name + " not found in the library for aspect " + aspectId);
	}

}
