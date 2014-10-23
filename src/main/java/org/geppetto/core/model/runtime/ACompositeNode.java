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
import java.util.Collection;
import java.util.List;
/**
 * Abstract node that defines a node capable of having other nodes as children. 
 * Overrides apply method of parent abstract class
 * 
 * @author matteocantarelli
 *
 */
public abstract class ACompositeNode extends ANode
{	
	public ACompositeNode(String id) {
		super(id);
	}
	
	protected List<ANode> _children=new ArrayList<ANode>();;
	
	public ANode addChild(ANode child)
	{
		if (child != null){
			_children.add(child);
			child._parent = this; //double link
			return child;
		}
		return null;
	}
	
	public List<ANode> getChildren()
	{
		return _children;
	}

	public String getBaseName()
	{
		if(isArray())
		{
			return getId().substring(0, getId().indexOf("["));
		}
		else return getId();
	}
	
	public int getIndex()
	{
		//ASSUMPTION only one dimension
		return Integer.parseInt(_id.substring(_id.indexOf("[")+1, _id.indexOf("]")));
	}
	
	
	@Override
	public String toString()
	{
		return _name+"["+_children+"]";
	}

	/**
	 * @param states
	 */
	public void addChildren(Collection<? extends ANode> states)
	{
		for (ANode state : states){
			addChild(state);
		}
		
	}
}
