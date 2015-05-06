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

package org.geppetto.core.model.services;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.geppetto.core.model.AModelInterpreter;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.IModelInterpreter;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.services.AService;
import org.geppetto.core.services.GeppettoFeature;
import org.geppetto.core.services.IModelFormat;
import org.geppetto.core.services.ModelFormat;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.core.simulator.AWatchableVariableListFeature;
import org.geppetto.core.simulator.services.ColladaVisualTreeFeature;
import org.springframework.stereotype.Service;

/**
 * @author matteocantarelli
 * 
 */
@Service
public class ColladaModelInterpreterService extends AModelInterpreter
{

	@Override
	public IModel readModel(URL url, List<URL> recordings, String instancePath) throws ModelInterpreterException
	{
		ModelWrapper collada = new ModelWrapper(instancePath);
		try
		{
			Scanner scanner = new Scanner(url.openStream(), "UTF-8");
			String colladaContent = scanner.useDelimiter("\\A").next();
			scanner.close();
			collada.wrapModel(ModelFormat.COLLADA, colladaContent);

			addRecordings(recordings, instancePath, collada);
			
			if(this.getFeature(GeppettoFeature.VISUAL_TREE_FEATURE)==null){
				this.addFeature(new ColladaVisualTreeFeature());
			}
			if(this.getFeature(GeppettoFeature.WATCHABLE_VARIABLE_LIST_FEATURE)==null){
				this.addFeature(new AWatchableVariableListFeature());
			}
		}
		catch(IOException e)
		{
			throw new ModelInterpreterException(e);
		}

		return collada;
	}

	@Override
	public boolean populateModelTree(AspectNode aspectNode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean populateRuntimeTree(AspectNode aspectNode) {
		AspectSubTreeNode modelTree = (AspectSubTreeNode) aspectNode.getSubTree(AspectTreeType.MODEL_TREE);
		AspectSubTreeNode visualizationTree = (AspectSubTreeNode) aspectNode.getSubTree(AspectTreeType.VISUALIZATION_TREE);
		AspectSubTreeNode simulationTree = (AspectSubTreeNode) aspectNode.getSubTree(AspectTreeType.SIMULATION_TREE);

		modelTree.setId(AspectTreeType.MODEL_TREE.toString());
		visualizationTree.setId(AspectTreeType.VISUALIZATION_TREE.toString());
		simulationTree.setId(AspectTreeType.SIMULATION_TREE.toString());
		
		return true;
	}

	@Override
	public String getName()
	{
		//TODO: Create spring bean with name of interpreter to retrieve it from there. 
		//Move this to own bundle?
		return "Collada Model Interpreter";
	}
	
	@Override
	public void registerGeppettoService() {
		List<IModelFormat> modelFormatList = new ArrayList<IModelFormat>();
		modelFormatList.add(ModelFormat.COLLADA);
		ServicesRegistry.registerModelInterpreterService(this, modelFormatList);
	}

}
