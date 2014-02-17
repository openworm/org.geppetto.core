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
package org.geppetto.core.model.state;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * @author matteocantarelli
 *
 */
public class CompositeStateNode extends AStateNode
{

	protected List<AStateNode> _children;
	
	public CompositeStateNode(String name)
	{
		super(name);
		_children=new ArrayList<AStateNode>();
	}
	
	public AStateNode addChild(AStateNode child)
	{
		if(_children==null)
		{
			_children=new ArrayList<AStateNode>();
		}
		_children.add(child);
		child._parent=this; //double link
		return child;
	}
	
	public List<AStateNode> getChildren()
	{
		return _children;
	}

	public String getBaseName()
	{
		if(isArray())
		{
		return _name.substring(0, _name.indexOf("["));
		}
		else return getName();
	}
	
	public int getIndex()
	{
		//ASSUMPTION only one dimension
		return Integer.parseInt(_name.substring(_name.indexOf("[")+1, _name.indexOf("]")));
	}
	
	
	@Override
	public String toString()
	{
		return _name+"["+_children+"]";
	}

	@Override
	public synchronized boolean apply(IStateVisitor visitor)
	{
		if (visitor.inCompositeStateNode(this))  // enter this node?
		{
			for(AStateNode stateNode:_children)
			{
				stateNode.apply(visitor);
				if(visitor.stopVisiting())
				{
					break;
				}
			}
		}
		return visitor.outCompositeStateNode( this );
	}
}
