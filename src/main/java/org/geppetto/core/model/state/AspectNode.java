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

import org.geppetto.core.model.IModelInterpreter;
import org.geppetto.core.model.state.ANode.SUBTREE;
import org.geppetto.core.model.state.visitors.IStateVisitor;
import org.geppetto.core.simulator.ISimulator;

/**
 * Node use to store Aspect values, and serialization.
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class AspectNode extends ACompositeStateNode{

	private String id;
	private StateVariableNode time;
	private IModelInterpreter modelInterpreter;
	private ISimulator simulator;
	private List<VisualModelNode> visualModels = new ArrayList<VisualModelNode>();
	private String instancePath;
	
	private final static String metaType = "Entity Node";
	
	public AspectNode(){}
	
	public AspectNode(String name) {
		super(name);
	}

	public StateVariableNode getTime() {
		return time;
	}

	public void setTime(StateVariableNode time) {
		this.time = time;
	}

	public IModelInterpreter getModelInterpreter() {
		return modelInterpreter;
	}

	public void setModelInterpreter(IModelInterpreter modelInterpreter) {
		this.modelInterpreter = modelInterpreter;
	}

	public ISimulator getSimulator() {
		return simulator;
	}

	public void setSimulator(ISimulator simulator) {
		this.simulator = simulator;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	public ANode getParentEntity() {
		return this.getParent();
	}

	public List<VisualModelNode> getVisualModel() {
		return this.visualModels;
	}

	public void setInstancePath(String instancePath) {
		this.instancePath = instancePath;
	}

	public String getInstancePath() {
		return this.instancePath;
	}
	
	/**
	 * @param tree
	 */
	public void flushSubTree(SUBTREE tree)
	{
		for(ANode node : _children)
		{
			if(node.getName().equals(tree.toString()))
			{
				// re-assign to empty node
				node = new CompositeVariableNode(tree.toString());
				break;
			}
		}
	}

	/**
	 * @param modelTree
	 * @return
	 */
	private ACompositeStateNode addSubTree(SUBTREE modelTree)
	{
		AspectTreeNode subTree = new AspectTreeNode(modelTree.toString());
		addChild(subTree);
		return subTree;
	}
	
	/**
	 * It creates the subtree if it doesn't exist
	 * @param modelTree
	 * @return
	 */
	public ACompositeStateNode getSubTree(SUBTREE modelTree)
	{
		for (ANode node:getChildren())
		{
			if( node.getName().equals(modelTree.toString()))
			{
				return (ACompositeStateNode) node;
			}
		}
		return addSubTree(modelTree);
	}

	@Override
	public String getMetaType() {
		return this.metaType;
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
}