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

import java.util.Collection;
import java.util.Map;

import org.geppetto.core.model.typesystem.IAspect;
import org.geppetto.core.model.typesystem.types.IType;

/**
 * @author matteocantarelli
 *
 */
public abstract class AGeppettoLibrary implements IGeppettoLibrary
{

	private IAspect aspect;

	private boolean caching = true;

	// TODO: Do we introduce a mechanism for type caching?
	protected Map<String, IType> types;

	@Override
	public IAspect getAspect()
	{
		return aspect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.library.IGeppettoLibrary#getTypes()
	 */
	@Override
	public Collection<IType> getTypes()
	{
		return types.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.library.IGeppettoLibrary#getTypeByName(java.lang.String)
	 */
	@Override
	public IType getTypeByName(String name) throws GeppettoTypeException
	{
		if(types.containsKey(name))
		{
			return types.get(name);
		}
		throw new GeppettoTypeException("Type " + name + " not found in the library for aspect " + aspect.getId());
	}

	/**
	 * @param typeName
	 * @return
	 */
	public boolean hasType(String typeName)
	{
		return types.containsKey(typeName);
	}

	/**
	 * @param type
	 * @param overwrite
	 * @throws GeppettoTypeException 
	 */
	protected void addType(IType type, boolean overwrite) throws GeppettoTypeException
	{
		if(!isCaching() || overwrite)
		{
			addType(type);
		}
		else
		{
			if(hasType(type.getName()))
			{
				if(!getTypeByName(type.getName()).equivalent(type))
				{
					//they are not the same, old type is discarded
					addType(type);
				}
				else
				{
					//do nothing, there is an equivalent type
				}
			}
		}
	}
	
	/**
	 * @param type
	 */
	protected void addType(IType type)
	{
		types.put(type.getName(),type);
	}

	/**
	 * @param caching
	 */
	public void setCaching(boolean caching)
	{
		this.caching = caching;

	}

	/**
	 * @return
	 */
	public boolean isCaching()
	{
		return caching;
	}
}
