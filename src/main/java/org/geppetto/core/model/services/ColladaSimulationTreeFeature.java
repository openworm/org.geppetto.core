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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.features.IVariableWatchFeature;
import org.geppetto.core.features.IWatchableVariableListFeature;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.services.GeppettoFeature;

/**
 * This feature allows the users to populate the variables which can be watched during a lems simulation
 * 
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 */
public class ColladaSimulationTreeFeature implements IWatchableVariableListFeature
{
	private AspectSubTreeNode simulationTree;

	private GeppettoFeature type = GeppettoFeature.WATCHABLE_VARIABLE_LIST_FEATURE;

	private List<URL> _recordings = new ArrayList<URL>();

	private IVariableWatchFeature watchFeature;

	private static Log _logger = LogFactory.getLog(ColladaSimulationTreeFeature.class);

	public ColladaSimulationTreeFeature(List<URL> recordings) {
	}

	@Override
	public GeppettoFeature getType()
	{
		return type;
	}

	@Override
	public boolean listWatchableVariables(AspectNode aspectNode) throws ModelInterpreterException
	{
		long start = System.currentTimeMillis();

		boolean modified = true;

		simulationTree = (AspectSubTreeNode) aspectNode.getSubTree(AspectTreeType.SIMULATION_TREE);
		simulationTree.setId(AspectTreeType.SIMULATION_TREE.toString());
		simulationTree.setModified(modified);

		_logger.info("Populate simulation tree completed, took " + (System.currentTimeMillis() - start) + "ms");

//		IModel model = aspectNode.getModel();
//		if(model instanceof RecordingModel)
//		{
//			readRecording(((RecordingModel) model).getHDF5(),watchFeature.getWatchedVariables(), simulationTree, false);
//		}
		return modified;
	}
}
