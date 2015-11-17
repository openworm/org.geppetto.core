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
package org.geppetto.core.model.typesystem.aspect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.geppetto.core.library.GeppettoTypeException;
import org.geppetto.core.model.typesystem.ANode;
import org.geppetto.core.model.typesystem.types.IType;
import org.geppetto.core.model.typesystem.visitor.IAnalysis;

/**
 * @author matteocantarelli
 *
 */
public class Aspect extends ANode implements IAspect
{

	private Map<String, IType> types;

	/**
	 * @param name
	 * @param id
	 */
	public Aspect(String name)
	{
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.typesystem.visitor.IVisitable#apply(org.geppetto.core.model.typesystem.visitor.IAnalysis)
	 */
	@Override
	public synchronized boolean apply(IAnalysis visitor)
	{
		if(visitor.inAspect(this)) // enter this node?
		{
			for(IType type : getTypes())
			{
				type.apply(visitor);
				if(visitor.stopVisiting())
				{
					break;
				}
			}
		}
		return visitor.outAspect(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.typesystem.aspect.IAspect#getTypes()
	 */
	@Override
	public Collection<IType> getTypes()
	{
		if(types == null)
		{
			types = new HashMap<String, IType>();
		}
		return types.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.typesystem.aspect.IAspect#getTypeByName(java.lang.String)
	 */
	@Override
	public IType getTypeByName(String name) throws GeppettoTypeException
	{
		if(types.containsKey(name))
		{
			return types.get(name);
		}
		throw new GeppettoTypeException("Type " + name + " not found inside aspect " + getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.typesystem.aspect.IAspect#addTypes(java.util.List)
	 */
	@Override
	public void addTypes(Collection<IType> types)
	{
		for(IType t : types)
		{
			this.types.put(t.getName(), t);
		}
	}

}
