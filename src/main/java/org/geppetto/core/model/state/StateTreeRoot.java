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

/**
 * @author matteocantarelli
 * 
 */
public class StateTreeRoot extends CompositeStateNode
{

	public enum SUBTREE
	{
		WATCH_TREE,
		MODEL_TREE, 
		TIME_STEP
	}

	/**
	 * @param modelId
	 * @deprecated
	 */
	public StateTreeRoot(String modelId)
	{
		super(modelId);
		_children = new ArrayList<AStateNode>();
	}
	
	/**
	 * 
	 */
	public StateTreeRoot()
	{
		super(null);
		_children = new ArrayList<AStateNode>();
	}
	
	/**
	 * @param tree
	 */
	public void flushSubTree(SUBTREE tree)
	{
		for(AStateNode node : _children)
		{
			if(node.getName().equals(tree.toString()))
			{
				// re-assign to empty node
				node = new CompositeStateNode(tree.toString());
				break;
			}
		}
	}

	/**
	 * @param modelTree
	 * @return
	 */
	private CompositeStateNode addSubTree(SUBTREE modelTree)
	{
		CompositeStateNode subTree = new CompositeStateNode(modelTree.toString());
		addChild(subTree);
		return subTree;
	}
	
	/**
	 * It creates the subtree if it doesn't exist
	 * @param modelTree
	 * @return
	 */
	public CompositeStateNode getSubTree(SUBTREE modelTree)
	{
		for (AStateNode node:getChildren())
		{
			if( node.getName().equals(modelTree.toString()))
			{
				return (CompositeStateNode) node;
			}
		}
		return addSubTree(modelTree);
	}

}
