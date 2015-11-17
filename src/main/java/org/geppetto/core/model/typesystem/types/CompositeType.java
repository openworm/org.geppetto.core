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
package org.geppetto.core.model.typesystem.types;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.model.typesystem.aspect.IAspect;
import org.geppetto.core.model.typesystem.values.IValue;
import org.geppetto.core.model.typesystem.variables.IVariable;
import org.geppetto.core.model.typesystem.visitor.IAnalysis;

/**
 * @author matteocantarelli
 *
 */
public class CompositeType extends AType
{

	List<IVariable> variables;

	public CompositeType(IAspect aspect, String name)
	{
		super(name);
		this.aspect = aspect;
	}

	public CompositeType(IAspect aspect, String name, IValue defaultValue)
	{
		this(aspect, name);
		this.defaultValue = defaultValue;
	}

	public CompositeType(IAspect aspect, String name, IValue defaultValue, IType baseType)
	{
		this(aspect, name, defaultValue);
		this.baseType = baseType;
	}

	/**
	 * @param variable
	 */
	public void addVariable(IVariable variable)
	{
		getVariables().add(variable);
		variable.setParent(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.typesystem.IType#equivalent(org.geppetto.core.model.typesystem.IType)
	 */
	@Override
	public boolean equivalent(IType type)
	{
		// TODO Implement method if needed
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.typesystem.visitor.IVisitable#apply(org.geppetto.core.model.typesystem.visitor.IAnalysis)
	 */
	@Override
	public boolean apply(IAnalysis visitor)
	{
		if(visitor.inCompositeType(this)) // enter this node?
		{
			for(IVariable variable : getVariables())
			{
				variable.apply(visitor);
				if(visitor.stopVisiting())
				{
					break;
				}
			}
		}
		return visitor.outCompositeType(this);
	}

	/**
	 * @return
	 */
	private List<IVariable> getVariables()
	{
		if(variables == null)
		{
			variables = new ArrayList<IVariable>();
		}
		return variables;
	}

}
