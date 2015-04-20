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
import java.util.StringTokenizer;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.features.IVariableWatchFeature;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.state.visitors.SerializeUpdateSimulationTreeVisitor;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.ValuesFactory;
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

	protected int _currentRecordingIndex = 0;

	protected List<RecordingModel> _recordings = new ArrayList<RecordingModel>();

	private double _runtime;

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

	protected void advanceRecordings(AspectNode aspect) throws GeppettoExecutionException
	{
		IVariableWatchFeature watchFeature = ((IVariableWatchFeature) this.getFeature(GeppettoFeature.VARIABLE_WATCH_FEATURE));
		if(_recordings != null && watchFeature.isWatching())
		{
			AspectSubTreeNode watchTree = (AspectSubTreeNode) aspect.getSubTree(AspectTreeType.SIMULATION_TREE);
			watchTree.setModified(true);
			aspect.setModified(true);
			aspect.getParentEntity().setModified(true);

			if(watchTree.getChildren().isEmpty())
			{
				for(RecordingModel recording : _recordings)
				{
					this.readRecording(recording.getHDF5(), watchTree, false);
					_currentRecordingIndex++;
				}
			}
			else
			{
				for(RecordingModel recording : _recordings)
				{
					UpdateRecordingStateTreeVisitor updateStateTreeVisitor = new UpdateRecordingStateTreeVisitor(recording, watchTree.getInstancePath(), _listener, _currentRecordingIndex++);
					watchTree.apply(updateStateTreeVisitor);
					if(updateStateTreeVisitor.getError() != null)
					{
						_listener.endOfSteps(null);
						throw new GeppettoExecutionException(updateStateTreeVisitor.getError());
					}
					else if(updateStateTreeVisitor.getRange() != null)
					{
						_listener.endOfSteps(null);
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
	protected void notifyStateTreeUpdated() throws GeppettoExecutionException
	{
		getListener().stateTreeUpdated();
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

	public void readRecording(H5File h5File, AspectSubTreeNode simulationTree, boolean readAll) throws GeppettoExecutionException
	{
		try
		{
			h5File.open();
		}
		catch(Exception e1)
		{
			throw new GeppettoExecutionException(e1);
		}
		SerializeUpdateSimulationTreeVisitor readWatchableVariableListVisitor = new SerializeUpdateSimulationTreeVisitor();
		simulationTree.apply(readWatchableVariableListVisitor);
		for(String watchedVariable : readWatchableVariableListVisitor.getWatchableVariableList())
		{
			// String name = watchedVariable.getName();
			String path = "/" + watchedVariable.replace(simulationTree.getInstancePath() + ".", "");
			path = path.replace(".", "/");
			Dataset v = (Dataset) FileFormat.findObject(h5File, path);

			path = path.replaceFirst("/", "");
			StringTokenizer tokenizer = new StringTokenizer(path, "/");
			ACompositeNode node = simulationTree;
			VariableNode newVariableNode = null;
			while(tokenizer.hasMoreElements())
			{
				String current = tokenizer.nextToken();
				boolean found = false;
				for(ANode child : node.getChildren())
				{
					if(child.getId().equals(current))
					{
						if(child instanceof ACompositeNode)
						{
							node = (ACompositeNode) child;
						}
						if(child instanceof VariableNode)
						{
							newVariableNode = (VariableNode) child;
						}
						found = true;
						break;
					}
				}
				if(found)
				{
					continue;
				}
				else
				{
					if(tokenizer.hasMoreElements())
					{
						// not a leaf, create a composite state node
						ACompositeNode newNode = new CompositeNode(current);
						node.addChild(newNode);
						node = newNode;
					}
					else
					{
						// it's a leaf node
						VariableNode newNode = new VariableNode(current);
						Object dataRead;
						try
						{
							dataRead = v.read();
							PhysicalQuantity quantity = new PhysicalQuantity();
							AValue readValue = null;
							if(dataRead instanceof double[])
							{
								double[] dr = (double[]) dataRead;
								readValue = ValuesFactory.getDoubleValue(dr[_currentRecordingIndex]);
							}
							else if(dataRead instanceof float[])
							{
								float[] fr = (float[]) dataRead;
								readValue = ValuesFactory.getFloatValue(fr[_currentRecordingIndex]);
							}
							else if(dataRead instanceof int[])
							{
								int[] ir = (int[]) dataRead;
								readValue = ValuesFactory.getIntValue(ir[_currentRecordingIndex]);
							}

							quantity.setValue(readValue);
							newNode.addPhysicalQuantity(quantity);
							newVariableNode = newNode;
							node.addChild(newNode);
						}

						catch(Exception | OutOfMemoryError e)
						{
							throw new GeppettoExecutionException(e);
						}
					}
				}
			}
			if(readAll)
			{
				Object dataRead;
				try
				{
					dataRead = v.read();

					AValue readValue = null;

					if(dataRead instanceof double[])
					{
						double[] dr = (double[]) dataRead;
						for(int i = 0; i < dr.length; i++)
						{
							PhysicalQuantity quantity = new PhysicalQuantity();
							readValue = ValuesFactory.getDoubleValue(dr[i]);
							quantity.setValue(readValue);
							newVariableNode.addPhysicalQuantity(quantity);
						}
					}
					else if(dataRead instanceof float[])
					{
						float[] fr = (float[]) dataRead;
						for(int i = 0; i < fr.length; i++)
						{
							PhysicalQuantity quantity = new PhysicalQuantity();
							readValue = ValuesFactory.getDoubleValue(fr[i]);
							quantity.setValue(readValue);
							newVariableNode.addPhysicalQuantity(quantity);
						}
					}
					else if(dataRead instanceof int[])
					{
						int[] ir = (int[]) dataRead;
						for(int i = 0; i < ir.length; i++)
						{
							PhysicalQuantity quantity = new PhysicalQuantity();
							readValue = ValuesFactory.getDoubleValue(ir[i]);
							quantity.setValue(readValue);
							newVariableNode.addPhysicalQuantity(quantity);
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					throw new GeppettoExecutionException(e);
				}
				catch(Exception | OutOfMemoryError e)
				{
					throw new GeppettoExecutionException(e);
				}
			}
		}
	}
}
