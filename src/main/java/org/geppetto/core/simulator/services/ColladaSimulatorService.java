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
package org.geppetto.core.simulator.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geppetto.core.beans.SimulatorConfig;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.services.GeppettoFeature;
import org.geppetto.core.services.IModelFormat;
import org.geppetto.core.services.ModelFormat;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.core.simulation.IRunConfiguration;
import org.geppetto.core.simulation.ISimulatorCallbackListener;
import org.geppetto.core.simulator.ASimulator;
import org.geppetto.core.simulator.AVariableWatchFeature;
import org.geppetto.core.simulator.AWatchableVariableListFeature;
import org.geppetto.core.simulator.FindSimulationTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColladaSimulatorService extends ASimulator{

	@Autowired
	private SimulatorConfig colladaSimulatorConfig;
	
	@Override
	public void simulate(IRunConfiguration runConfiguration, AspectNode aspect)
			throws GeppettoExecutionException {
		advanceTimeStep(0, aspect);
		RuntimeTreeRoot root;
		ANode n = aspect.getParent();
		while(n.getParent()!=null){
			n  = n.getParent();
		}
		
		root = (RuntimeTreeRoot) n;
		//traverse scene root to get all simulation trees for all aspects
		FindSimulationTree mappingVisitor = new FindSimulationTree();
		root.apply(mappingVisitor);
		HashMap<String, AspectNode> aspects = 
				mappingVisitor.getAspects();
		Iterator it = aspects.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry o = (Map.Entry)it.next();
			AspectNode a = (AspectNode) o.getValue();
			advanceRecordings(a);
		}
		notifyStateTreeUpdated();
	}

	@Override
	public void initialize(List<IModel> models,
			ISimulatorCallbackListener listener)
			throws GeppettoInitializationException, GeppettoExecutionException {
		super.initialize(models, listener);

		//add variable watch feature
		if(this.getFeature(GeppettoFeature.VARIABLE_WATCH_FEATURE)==null){
			this.addFeature(new AVariableWatchFeature());
		}
	}

	@Override
	public String getName() {
		return this.colladaSimulatorConfig.getSimulatorName();
	}

	@Override
	public String getId() {
		return this.colladaSimulatorConfig.getSimulatorID();
	}
	@Override
	public void registerGeppettoService()
	{
		List<IModelFormat> modelFormatList = new ArrayList<IModelFormat>();
		modelFormatList.add(ModelFormat.COLLADA);
		ServicesRegistry.registerSimulatorService(this, modelFormatList);
	}
}
