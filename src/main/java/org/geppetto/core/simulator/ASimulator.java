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

package org.geppetto.core.simulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.geppetto.core.common.ArrayUtils;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.AVariable;
import org.geppetto.core.data.model.ArrayVariable;
import org.geppetto.core.data.model.SimpleType;
import org.geppetto.core.data.model.SimpleType.Type;
import org.geppetto.core.data.model.StructuredType;
import org.geppetto.core.data.model.VariableList;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.data.DataModelFactory;
import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.ATimeSeriesNode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.ValuesFactory;
import org.geppetto.core.simulation.ISimulatorCallbackListener;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;

/**
 * @author matteocantarelli
 * 
 */
public abstract class ASimulator implements ISimulator
{

	protected List<IModel> _models;

	private ISimulatorCallbackListener _listener;

	private boolean _initialized = false;

	protected boolean _treesEmptied = false;

	private boolean _watching = false;

	private VariableList _forceableVariables = new VariableList();

	private VariableList _watchableVariables = new VariableList();

	private Set<String> _watchList = new HashSet<String>();

	private boolean _watchListModified = false;

	private double _runtime = 0;

	private String _timeStepUnit = "ms";

	protected int _currentRecordingIndex = 0;

	protected List<RecordingModel> _recordings = new ArrayList<RecordingModel>();

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
		if(!_watchableVariables.getVariables().isEmpty())
		{
			_treesEmptied = true;
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#addWatchVariables(java.util.List)
	 */
	@Override
	public void addWatchVariables(List<String> variableNames)
	{
		_watchList.addAll(variableNames);
		_watchListModified = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#startWatch()
	 */
	@Override
	public void startWatch()
	{
		_watching = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#stopWatch()
	 */
	@Override
	public void stopWatch()
	{
		_watching = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#clearWatchVariables()
	 */
	@Override
	public void clearWatchVariables()
	{
		_watchList.clear();
	}

	/**
	 * @return
	 */
	public boolean isWatching()
	{
		return _watching;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#getForceableVariables()
	 */
	@Override
	public VariableList getForceableVariables()
	{
		return _forceableVariables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#getWatchableVariables()
	 */
	@Override
	public VariableList getWatchableVariables()
	{
		return _watchableVariables;
	}

	/**
	 * @return
	 */
	protected Collection<String> getWatchList()
	{
		return _watchList;
	}

	/**
	 * @return
	 */
	protected boolean watchListModified()
	{
		return _watchListModified;
	}

	/**
	 * @param watchListModified
	 */
	protected void watchListModified(boolean watchListModified)
	{
		_watchListModified = watchListModified;

	}

	/**
	 * @return
	 */
	public String getTimeStepUnit()
	{
		return _timeStepUnit;
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
	 */
	protected void advanceTimeStep(double timestep)
	{
		_runtime += timestep;
		ACompositeNode timeStepsNode = new CompositeNode("time tree");
		{
			VariableNode time = new VariableNode("time");
			timeStepsNode.addChild(time);
			PhysicalQuantity t = new PhysicalQuantity();
			t.setUnit(_timeStepUnit);
		}
		ATimeSeriesNode leafNode = (ATimeSeriesNode) timeStepsNode.getChildren().get(0);
		PhysicalQuantity runTime = new PhysicalQuantity();
		runTime.setValue(ValuesFactory.getDoubleValue(_runtime));
		leafNode.addPhysicalQuantity(runTime);
	}

	protected void advanceRecordings(AspectNode aspect) throws GeppettoExecutionException
	{
		if(_recordings != null && isWatching())
		{
			AspectSubTreeNode watchTree = (AspectSubTreeNode) aspect.getSubTree(AspectTreeType.WATCH_TREE);

			if(watchTree.getChildren().isEmpty() || watchListModified())
			{
				for(RecordingModel recording : _recordings)
				{
					watchListModified(false);
					NetcdfFile file = recording.getHDF5();
					for(ucar.nc2.Variable hdfVariable : file.getVariables())
					{

						// for every state found in the recordings check if we are watching that variable
						String fullPath = watchTree.getInstancePath() + "." + hdfVariable.getFullName().replace("/", ".");
						if(getWatchList().contains(fullPath))
						{
							fullPath = fullPath.replace(watchTree.getInstancePath() + ".", "");

							StringTokenizer tokenizer = new StringTokenizer(fullPath, ".");
							ACompositeNode node = watchTree;
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
										int[] start = { _currentRecordingIndex };
										int[] lenght = { 1 };
										Array value;
										try
										{
											value = hdfVariable.read(start, lenght);
											Type type = Type.fromValue(hdfVariable.getDataType().toString());

											PhysicalQuantity quantity = new PhysicalQuantity();
											AValue readValue = null;
											switch(type)
											{
												case DOUBLE:
													readValue = ValuesFactory.getDoubleValue(value.getDouble(0));
													break;
												case FLOAT:
													readValue = ValuesFactory.getFloatValue(value.getFloat(0));
													break;
												case INTEGER:
													readValue = ValuesFactory.getIntValue(value.getInt(0));
													break;
												default:
													break;
											}
											quantity.setValue(readValue);
											newNode.addPhysicalQuantity(quantity);
											node.addChild(newNode);
										}
										catch(IOException | InvalidRangeException e)
										{
											throw new GeppettoExecutionException(e);
										}
									}
								}
							}
						}
					}
					_currentRecordingIndex++;
				}
			}
			else
			{
				for(RecordingModel recording : _recordings)
				{
					UpdateRecordingStateTreeVisitor updateStateTreeVisitor = new UpdateRecordingStateTreeVisitor(recording, watchTree.getInstancePath(), _currentRecordingIndex++);
					watchTree.apply(updateStateTreeVisitor);
					if(updateStateTreeVisitor.getError() != null)
					{
						throw new GeppettoExecutionException(updateStateTreeVisitor.getError());
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	protected void setWatchableVariablesFromRecordings()
	{
		if(_recordings != null)
		{
			for(RecordingModel recording : _recordings)
			{
				NetcdfFile file = recording.getHDF5();
				for(ucar.nc2.Variable hdfVariable : file.getVariables())
				{
					String fullPath = hdfVariable.getFullName();
					List<AVariable> listToCheck = getWatchableVariables().getVariables();
					StringTokenizer stok = new StringTokenizer(fullPath, "/");

					while(stok.hasMoreTokens())
					{
						String s = stok.nextToken();
						String searchVar = s;

						if(ArrayUtils.isArray(s))
						{
							searchVar = ArrayUtils.getArrayName(s);
						}

						AVariable v = getVariable(searchVar, listToCheck);

						if(v == null)
						{
							if(stok.hasMoreTokens())
							{
								StructuredType structuredType = new StructuredType();
								structuredType.setName(searchVar + "T");

								if(ArrayUtils.isArray(s))
								{
									v = DataModelFactory.getArrayVariable(searchVar, structuredType, ArrayUtils.getArrayIndex(s) + 1);
								}
								else
								{
									v = DataModelFactory.getSimpleVariable(searchVar, structuredType);
								}
								listToCheck.add(v);
								listToCheck = structuredType.getVariables();
							}
							else
							{
								SimpleType type = DataModelFactory.getCachedSimpleType(Type.fromValue(hdfVariable.getDataType().toString()));
								if(ArrayUtils.isArray(s))
								{
									v = DataModelFactory.getArrayVariable(searchVar, type, ArrayUtils.getArrayIndex(s) + 1);
								}
								else
								{
									v = DataModelFactory.getSimpleVariable(searchVar, type);
								}
								listToCheck.add(v);
							}
						}
						else
						{
							if(stok.hasMoreTokens())
							{
								listToCheck = ((StructuredType) v.getType()).getVariables();
								if(ArrayUtils.isArray(s))
								{
									if(ArrayUtils.getArrayIndex(s) + 1 > ((ArrayVariable) v).getSize())
									{
										((ArrayVariable) v).setSize(ArrayUtils.getArrayIndex(s) + 1);
									}
								}
							}
						}
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
	 * @param s
	 * @param list
	 * @return
	 */
	protected static AVariable getVariable(String s, List<AVariable> list)
	{
		String searchVar = s;
		for(AVariable v : list)
		{
			if(v.getName().equals(searchVar))
			{
				return v;
			}
		}
		return null;
	}

	/**
	 * @throws GeppettoExecutionException
	 */
	protected void notifyStateTreeUpdated() throws GeppettoExecutionException
	{
		getListener().stateTreeUpdated();
	}
}
