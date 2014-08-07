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

import org.geppetto.core.model.IModel;
import org.geppetto.core.model.IModelInterpreter;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.state.visitors.IStateVisitor;
import org.geppetto.core.simulator.ISimulator;

/**
 * Node use to store Aspect values, and serialization.
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class AspectNode extends ACompositeNode{

	private String _id;
	private VariableNode _time;
	private IModelInterpreter _modelInterpreter;
	private ISimulator _simulator;
	private IModel _model;
		
	public AspectNode(){}
	
	public AspectNode(String name) {
		super(name);
	}

	public VariableNode getTime() {
		return _time;
	}

	public void setTime(VariableNode time) {
		this._time = time;
	}

	public IModelInterpreter getModelInterpreter() {
		return _modelInterpreter;
	}

	public void setModelInterpreter(IModelInterpreter modelInterpreter) {
		this._modelInterpreter = modelInterpreter;
	}

	public ISimulator getSimulator() {
		return _simulator;
	}

	public void setSimulator(ISimulator simulator) {
		this._simulator = simulator;
	}
	
	public void setId(String id){
		this._id = id;
	}
	
	public String getId() {
		return this._id;
	}

	public ANode getParentEntity() {
		return this.getParent();
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
				((AspectSubTreeNode)node).getChildren().clear();
				break;
			}
		}
	}

	/**
	 * @param modelTree
	 * @return
	 */
	private ACompositeNode addSubTree(AspectTreeType modelTree)
	{
		AspectSubTreeNode subTree = new AspectSubTreeNode(modelTree);
		addChild(subTree);
		return subTree;
	}
	
	/**
	 * It creates the subtree if it doesn't exist
	 * @param modelTree
	 * @return
	 */
	public ACompositeNode getSubTree(AspectTreeType modelTree)
	{
		for (ANode node:getChildren())
		{
			if( node.getName().equals(modelTree.toString()))
			{
				return (ACompositeNode) node;
			}
		}
		return addSubTree(modelTree);
	}
	
	@Override
	public synchronized boolean apply(IStateVisitor visitor)
	{
		if (visitor.inAspectNode(this))  // enter this node?
		{
			for(ANode stateNode:this.getChildren())
			{
				stateNode.apply(visitor);
				if(visitor.stopVisiting())
				{
					break;
				}
			}
		}
		return visitor.outAspectNode( this );
	}
	
	public IModel getModel()
	{
		return _model;
	}

	public void setModel(IModel _model)
	{
		this._model = _model;
	}
}