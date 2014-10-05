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

import org.geppetto.core.model.state.visitors.IVisitable;

/**
 * Parent Node, use for serialization. 
 * 
 * @author matteocantarelli
 * 
 */
public abstract class ANode implements IVisitable
{
	protected ANode _parent;
	protected String _name;
	protected String _id;

	public String getMetaType(){
		return this.getClass().getSimpleName();
	};

	public ANode(String id)
	{
		super();
		_id = id;
		_parent = null;
	}

	public void setName(String name){
		this._name = name;
	}
	
	public String getName()
	{
		return _name;
	}

	public void setId(String id){
		this._id = id;
	}
	
	public String getId() {
		return this._id;
	}
	
	/**
	 * @return the next sibling of this node
	 */
	public ANode nextSibling()
	{
		if(!(getParent() instanceof ACompositeNode))
		{
			return null;
		}
		else
		{
			int currentIndex = ((ACompositeNode) getParent()).getChildren().indexOf(this);
			if(((ACompositeNode) getParent()).getChildren().size() < currentIndex + 2)
			{
				return null;
			}
			else
			{
				return ((ACompositeNode) getParent()).getChildren().get(currentIndex + 1);
			}
		}
	}

	public ANode getParent()
	{
		return _parent;
	}
	
	public void setParent(ANode parent){
		this._parent = parent;
	}

	public boolean isArray()
	{
		return _id.contains("[");
	}
	
	public String getInstancePath()
	{
		StringBuffer fullName = new StringBuffer();
		ANode iterateState = this;
		while(iterateState != null)
		{
			if(iterateState._parent != null)
			{
				if(!fullName.toString().isEmpty())
				{
					fullName.insert(0, ".");
				}
				fullName.insert(0, iterateState._id);
			}
			iterateState = iterateState._parent;
		}
		return fullName.toString();
	}

}
