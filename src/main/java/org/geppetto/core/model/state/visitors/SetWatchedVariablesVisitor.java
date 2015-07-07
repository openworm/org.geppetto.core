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

import java.util.List;

import org.geppetto.core.data.DataManagerHelper;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IInstancePath;
import org.geppetto.core.data.model.ISimulatorConfiguration;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.VariableNode;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 *         This visitor sets the variables passed as a list of strings to watched in the simulation tree If no list is passed the simulation tree is cleared, i.e. no variables is watched in the
 *         simulation tree.
 */
public class SetWatchedVariablesVisitor extends RuntimeTreeVisitor
{

	private List<String> _watchedVariables;
	private IExperiment experiment;

	public SetWatchedVariablesVisitor()
	{
		super();
	}

	public SetWatchedVariablesVisitor(IExperiment experiment, List<String> lists)
	{
		super();
		this.experiment = experiment;
		this._watchedVariables = lists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.state.visitors.DefaultStateVisitor#inAspectNode (org.geppetto.core.model.runtime.AspectNode)
	 */
	@Override
	public boolean inCompositeNode(CompositeNode node)
	{
		// we only visit the nodes which belong to the same aspect
		return super.inCompositeNode(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.state.visitors.DefaultStateVisitor#visitVariableNode (org.geppetto.core.model.runtime.VariableNode)
	 */
	@Override
	public boolean visitVariableNode(VariableNode node)
	{
		// If watchedVariables is null, clear the simulation tree
		if(this._watchedVariables.contains(node.getInstancePath()))
		{
			node.setWatched(!node.isWatched());
			String aspectPath = node.getAspectNode().getInstancePath();
			IAspectConfiguration found = null;
			if(experiment.getAspectConfigurations() != null)
			{
				for(IAspectConfiguration ac : experiment.getAspectConfigurations())
				{
					if(aspectPath.startsWith(ac.getAspect().getInstancePath()))
					{
						found = ac;
						break;
					}
				}
			}
			if(found==null)
			{
				IInstancePath instancePath=DataManagerHelper.getDataManager().newInstancePath(node.getAspectNode());
				ISimulatorConfiguration simulatorConfiguration=DataManagerHelper.getDataManager().newSimulatorConfiguration("","",0l,0l);
				found=DataManagerHelper.getDataManager().newAspectConfiguration(experiment, instancePath, simulatorConfiguration);
			}
			else
			{
				IInstancePath instancePath=DataManagerHelper.getDataManager().newInstancePath(node);
				DataManagerHelper.getDataManager().addWatchedVariable(found,instancePath);
			}

		}
		else if(this._watchedVariables == null)
		{
			node.setWatched(false);
			
			//TODO Remove from the project too
		}
		return super.visitVariableNode(node);
	}

}
