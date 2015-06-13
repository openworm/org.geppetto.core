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
package org.geppetto.core.model.runtime;

import org.geppetto.core.model.state.visitors.IStateVisitor;


/**
 * Node use for children of AspectNode, stores; model, visualization and simulation trees.
 * 
 * @author matteocantarelli
 * 
 */
public class AspectSubTreeNode extends ACompositeNode
{
	
	public enum AspectTreeType
	{
		MODEL_TREE("ModelTree"),
		VISUALIZATION_TREE("VisualizationTree"),
		SIMULATION_TREE("SimulationTree");
		
		private final String text;

	    /**
	     * @param text
	     */
	    private AspectTreeType(final String text) {
	        this.text = text;
	    }


	    /* (non-Javadoc)
	     * @see java.lang.Enum#toString()
	     */
	    @Override
	    public String toString() {
	        return text;
	    }
	}

	private AspectTreeType _type;
	
	/**
	 * @param modelTree.toString()
	 */
	public AspectSubTreeNode(AspectTreeType treeType)
	{
		super(treeType.toString());
		this._type = treeType;
		String id = treeType.toString();
		String name = id.replace("Tree", "");
		this.setName(name);
	}
	
	/**
	 * 
	 */
	public AspectSubTreeNode()
	{
		super(null);
	}
	
	public AspectSubTreeNode(String treeType)
	{
		super(treeType);
		this.setName(treeType);
	}

	public AspectTreeType getType(){
		return this._type;
	}
	
	/**
	 * @param tree
	 */
	public void flushSubTree(AspectTreeType tree)
	{
		for(ANode node : _children)
		{
			if(node.getName().equals(tree.toString()))
			{
				// re-assign to empty node
				node = new CompositeNode(tree.toString());
				break;
			}
		}
	}

	/**
	 * @param modelTree
	 * @return
	 */
	private AspectSubTreeNode addSubTree(AspectTreeType treeType)
	{
		AspectSubTreeNode subTree = new AspectSubTreeNode(treeType.toString());
		subTree.setId(treeType.toString());
		String id = treeType.toString();
		String name = id.replace("Tree", "");
		subTree.setName(name);
		addChild(subTree);
		return subTree;
	}
	
	/**
	 * It creates the subtree if it doesn't exist
	 * @param modelTree
	 * @return
	 */
	public ACompositeNode getSubTree(AspectTreeType treeType)
	{
		for (ANode node:getChildren())
		{
			if( node.getName().equals(treeType.toString()))
			{
				return (ACompositeNode) node;
			}
		}
		return addSubTree(treeType);
	}
	
	@Override
	public synchronized boolean apply(IStateVisitor visitor)
	{
		if (visitor.inAspectSubTreeNode(this))  // enter this node?
		{
			for(ANode stateNode:this.getChildren())
			{
				if (stateNode != null){
					stateNode.apply(visitor);
					if(visitor.stopVisiting())
					{
						break;
					}
				}
			}
		}
		return visitor.outAspectSubTreeNode( this );
	}
}
