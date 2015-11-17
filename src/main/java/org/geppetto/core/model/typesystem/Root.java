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
package org.geppetto.core.model.typesystem;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.model.typesystem.aspect.IAspect;
import org.geppetto.core.model.typesystem.values.VariableValue;
import org.geppetto.core.model.typesystem.variables.IVariable;
import org.geppetto.core.model.typesystem.visitor.IAnalysis;

/**
 * @author matteocantarelli
 *
 */
public class Root extends ANode
{

	public Root()
	{
		super("__ROOT__");
	}

	private List<IAspect> aspects;

	private List<IVariable> variables;

	private VariableValue time;

	/**
	 * @return
	 */
	public List<IAspect> getAspects()
	{
		if(aspects == null)
		{
			aspects = new ArrayList<IAspect>();
		}
		return aspects;
	}

	/**
	 * @param aspect
	 */
	public void addAspect(IAspect aspect)
	{
		getAspects().add(aspect);
		aspect.setParent(this);
	}

	/**
	 * @return
	 */
	public List<IVariable> getVariables()
	{
		if(variables == null)
		{
			variables = new ArrayList<IVariable>();
		}
		return variables;
	}

	/**
	 * @param variable
	 */
	public void addVariable(IVariable variable)
	{
		getVariables().add(variable);
		variable.setParent(this);
	}

	/**
	 * @return
	 */
	public VariableValue getTime()
	{
		return time;
	}

	/**
	 * @param time
	 */
	public void setTime(VariableValue time)
	{
		this.time = time;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.model.typesystem.visitor.IVisitable#apply(org.geppetto.core.model.typesystem.visitor.IAnalysis)
	 */
	@Override
	public synchronized boolean apply(IAnalysis visitor)
	{
		if(visitor.inRoot(this)) // enter this node?
		{
			for(IAspect aspect : this.getAspects())
			{
				aspect.apply(visitor);
				if(visitor.stopVisiting())
				{
					break;
				}
			}
			for(IVariable variable : this.getVariables())
			{
				variable.apply(visitor);
				if(visitor.stopVisiting())
				{
					break;
				}
			}
		}
		return visitor.outRoot(this);
	}

}
