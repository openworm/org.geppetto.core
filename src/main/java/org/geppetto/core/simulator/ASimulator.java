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

import java.util.ArrayList;
import java.util.List;

import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.features.IVariableWatchFeature;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.services.AService;
import org.geppetto.core.services.GeppettoFeature;
import org.geppetto.core.simulation.ISimulatorCallbackListener;

/**
 * @author matteocantarelli
 * 
 */
public abstract class ASimulator extends AService implements ISimulator
{

	protected List<IModel> _models;

	private ISimulatorCallbackListener _listener;

	private boolean _initialized = false;

	protected boolean _treesEmptied = false;

	private String _timeStepUnit = "ms";

	protected List<RecordingModel> _recordings = new ArrayList<RecordingModel>();

	private double _runtime;

	private RecordingReader recordingReader;

	public ASimulator()
	{
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#initialize(org.geppetto.core.model.IModel, org.geppetto.core.simulation.ISimulatorCallbackListener)
	 */
	@Override
	public void initialize(List<IModel> models, ISimulatorCallbackListener listener) throws GeppettoInitializationException, GeppettoExecutionException
	{
		setListener(listener);
		_models = models;
		recordingReader = new RecordingReader();

		// initialize recordings
		for(IModel model : models)
		{
			// for each IModel passed to this simulator which is a RecordingModel
			// we add it to a list
			if(model instanceof ModelWrapper)
			{
				for(Object wrappedModel : ((ModelWrapper) model).getModels())
				{
					if(wrappedModel instanceof RecordingModel)
					{
						_recordings.add((RecordingModel) wrappedModel);
					}
				}
			}
			else if(model instanceof RecordingModel)
			{
				_recordings.add((RecordingModel) model);
			}
		}

		_runtime = 0;
		_initialized = true;
	}

	/**
	 * @return
	 */
	public ISimulatorCallbackListener getListener()
	{
		return _listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#isInitialized()
	 */
	public boolean isInitialized()
	{
		return _initialized;
	}

	@Override
	public void setInitialized(boolean initialized)
	{
		_initialized = initialized;
	}

	/**
	 * @param _listener
	 */
	public void setListener(ISimulatorCallbackListener listener)
	{
		this._listener = listener;
	}

	/**
	 * @param timeStepUnit
	 */
	public void setTimeStepUnit(String timeStepUnit)
	{
		this._timeStepUnit = timeStepUnit;
	}

	/**
	 * @param timestep
	 * @param aspect
	 */
	public void advanceTimeStep(double timestep, AspectNode aspect)
	{
		_runtime += timestep;
	}
	
	/**
	 * @param h5File
	 * @param variables
	 * @param simulationTree
	 * @param readAll
	 * @throws GeppettoExecutionException
	 */
	public void readRecording(H5File h5File,List<String> variables, AspectSubTreeNode simulationTree, boolean readAll) throws GeppettoExecutionException
	{
		recordingReader.readRecording(h5File, variables, simulationTree, readAll);
	}

	protected void advanceRecordings(AspectNode aspect) throws GeppettoExecutionException
	{
		IVariableWatchFeature watchFeature = ((IVariableWatchFeature) this.getFeature(GeppettoFeature.VARIABLE_WATCH_FEATURE));
		if(_recordings != null)
		{
			AspectSubTreeNode watchTree = (AspectSubTreeNode) aspect.getSubTree(AspectTreeType.SIMULATION_TREE);
			watchTree.setModified(true);
			aspect.setModified(true);
			aspect.getParentEntity().setModified(true);

			if(watchTree.getChildren().isEmpty())
			{
				for(RecordingModel recording : _recordings)
				{
					recordingReader.readRecording(recording.getHDF5(), watchFeature.getWatchedVariables(), watchTree, false);

				}
			}
			else
			{
				for(RecordingModel recording : _recordings)
				{
					UpdateRecordingStateTreeVisitor updateStateTreeVisitor = new UpdateRecordingStateTreeVisitor(recording, watchTree.getInstancePath(), _listener, recordingReader.getAndIncrementCurrentIndex());
					watchTree.apply(updateStateTreeVisitor);
					if(updateStateTreeVisitor.getError() != null)
					{
						_listener.endOfSteps(null, null);
						throw new GeppettoExecutionException(updateStateTreeVisitor.getError());
					}
					else if(updateStateTreeVisitor.getRange() != null)
					{
						_listener.endOfSteps(null, null);
					}
				}
			}
		}
	}

	/**
	 * @return
	 */
	protected boolean treesEmptied()
	{
		return _treesEmptied;
	}

	/**
	 * @param b
	 */
	protected void treesEmptied(boolean b)
	{
		_treesEmptied = b;
	}

	/**
	 * @throws GeppettoExecutionException
	 */
	protected void notifySimulatorHasStepped(AspectNode aspect) throws GeppettoExecutionException
	{
		getListener().stepped(aspect);
	}

	@Override
	public double getTime()
	{
		return _runtime;
	}

	@Override
	public String getTimeStepUnit()
	{
		return _timeStepUnit;
	}

}
