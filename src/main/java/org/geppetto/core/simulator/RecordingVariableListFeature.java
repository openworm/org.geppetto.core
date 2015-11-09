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
package org.geppetto.core.simulator;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.features.IWatchableVariableListFeature;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.typesystem.AspectNode;
import org.geppetto.core.services.GeppettoFeature;
import org.geppetto.core.utilities.RecordingReader;

/**
 * Feature class for listing simulation variables from a recording file
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class RecordingVariableListFeature implements IWatchableVariableListFeature{

	private GeppettoFeature type = GeppettoFeature.WATCHABLE_VARIABLE_LIST_FEATURE;
	private RuntimeTreeRoot root;
	private static Log _logger = LogFactory.getLog(RecordingVariableListFeature.class);

	@Override
	public GeppettoFeature getType()
	{
		return type;
	}

	@Override
	public boolean listWatchableVariables(AspectNode aspectNode, IAspectConfiguration aspectConfiguration) throws ModelInterpreterException
	{
		boolean modified = true;

		ModelWrapper model = (ModelWrapper) aspectNode.getModel();
		Collection<Object> models = model.getModels();
		Iterator i = models.iterator();
		while(i.hasNext()){
			Object m = i.next();
			if(m instanceof RecordingModel)
			{
				//Get scene root
				ANode n = aspectNode.getParent();
				while(n.getParent()!=null){
					n  = n.getParent();
				}
				
				this.root = (RuntimeTreeRoot) n;
				//traverse scene root to get all simulation trees for all aspects
				GetAspectsVisitor mappingVisitor = new GetAspectsVisitor();
				this.root.apply(mappingVisitor);
			
				try {
					//we send the recording hdf5 location, plus map of aspects to populate them with recordings extracts
					RecordingReader recReader = new RecordingReader();
					recReader.populateSimulationVariables(((RecordingModel) m).getHDF5().getAbsolutePath(), mappingVisitor.getAspects());
				} catch (GeppettoExecutionException e) {
					throw new ModelInterpreterException(e);
				}
			}
		}
		
		return modified;
	}
}


