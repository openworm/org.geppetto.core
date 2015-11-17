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
package org.geppetto.core.model.typesystem.variables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.geppetto.core.library.GeppettoTypeException;
import org.geppetto.core.model.typesystem.ANode;
import org.geppetto.core.model.typesystem.types.IType;
import org.geppetto.core.model.typesystem.values.IValue;
import org.geppetto.core.model.typesystem.visitor.IAnalysis;

/**
 * @author matteocantarelli
 *
 */
public class Variable extends ANode implements IVariable
{

	Collection<IType> types = null;

	Map<IType, IValue> initialValues = null;

	IType type;
	
	IValue initialValue;


	public Variable(String name, IType type, IValue initialValue)
	{
		this(name, type);
		this.initialValue = initialValue;
	}

	public Variable(String name, IType type)
	{
		this(name);
		this.type = type;
	}

	public Variable(String name)
	{
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.typesystem.IVariable#getTypes()
	 */
	@Override
	public Collection<IType> getTypes()
	{
		return types;
	}

	public boolean hasMultipleTypes()
	{
		return initialValues != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.typesystem.IVariable#getInitialValue()
	 */
	@Override
	public IValue getInitialValue() throws GeppettoTypeException
	{
		if(hasMultipleTypes())
		{
			throw new GeppettoTypeException("This variable as more than one type");
		}
		return initialValue;
	}

	@Override
	public IValue getInitialValue(IType type) throws GeppettoTypeException
	{
		if(initialValues == null)
		{
			if(this.type.equals(type))
			{
				return initialValue;
			}
			else
			{
				throw new GeppettoTypeException("Type is invalid for the current variable, type found: " + this.type);
			}

		}
		else
		{
			if(!initialValues.containsKey(type))
			{
				throw new GeppettoTypeException("Type is invalid for the current variable, types found: " + this.types);
			}
		}
		return initialValues.get(type);
	}

	@Override
	public IType getType() throws GeppettoTypeException
	{
		if(types != null)
		{
			throw new GeppettoTypeException("This variable as more than one type");
		}
		return type;
	}

	@Override
	public void addType(IType type)
	{
		if(hasMultipleTypes())
		{
			types.add(type);
		}
		else
		{
			if(this.type == null)
			{
				this.type = type;
			}
			else
			{
				// if more than one type starts getting used we switch from one data structure to the other
				types = new ArrayList<IType>();
				types.add(this.type);
				types.add(type);
				initialValues = new HashMap<IType, IValue>();
				initialValues.put(this.type, this.initialValue);
				this.type = null;
				this.initialValue = null;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.typesystem.visitor.IVisitable#apply(org.geppetto.core.model.typesystem.visitor.IAnalysis)
	 */
	@Override
	public synchronized boolean apply(IAnalysis visitor)
	{
		if(visitor.inVariable(this)) // enter this node?
		{
			try
			{
				if(hasMultipleTypes())
				{
					for(IType type : this.getTypes())
					{
						type.apply(visitor);

						getInitialValue(type).apply(visitor);

						if(visitor.stopVisiting())
						{
							break;
						}
					}
				}
				else
				{
					getType().apply(visitor);
					getInitialValue().apply(visitor);
				}
			}
			catch(GeppettoTypeException e)
			{
				// Should never happen really
				throw new RuntimeException(e);
			}
		}
		return visitor.outVariable(this);
	}

}
