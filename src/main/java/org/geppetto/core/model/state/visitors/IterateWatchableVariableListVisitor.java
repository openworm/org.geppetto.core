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
package org.geppetto.core.model.state.visitors;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.data.model.WatchList;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.VariableNode;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 *         This method updates the particles already present in the tree adding
 *         new values as found on the position pointer
 */
public class IterateWatchableVariableListVisitor extends DefaultStateVisitor {

	private List<String> _watchableVariableList = new ArrayList<String>();
	private String _mode = "read";
	private List<WatchList> _lists;
	
	public IterateWatchableVariableListVisitor()
	{
		super();
	}
	
	public IterateWatchableVariableListVisitor(List<WatchList> lists)
	{
		super();
		this._mode = "setWatched";
		this._lists = lists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geppetto.core.model.state.visitors.DefaultStateVisitor#inAspectNode
	 * (org.geppetto.core.model.runtime.AspectNode)
	 */
	@Override
	public boolean inCompositeNode(CompositeNode node) {
		// we only visit the nodes which belong to the same aspect
		return super.inCompositeNode(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geppetto.core.model.state.visitors.DefaultStateVisitor#visitVariableNode
	 * (org.geppetto.core.model.runtime.VariableNode)
	 */
	@Override
	public boolean visitVariableNode(VariableNode node) {
		
		if (_mode.equals("read")){
			if (node.isWatched())
				this._watchableVariableList.add(node.getInstancePath());
		}
		else{
			for(WatchList list : this._lists)
			{
				for(String varPath : list.getVariablePaths())
				{
					if (node.getInstancePath().equals(varPath)){
						node.setWatched(true);
					}
				}
			}	
			
		}
		return super.visitVariableNode(node);
	}

	public List<String> getWatchableVariableList()
	{
		return _watchableVariableList;
	}

}
