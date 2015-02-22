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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.AVariable;
import org.geppetto.core.data.model.SimpleType;
import org.geppetto.core.data.model.SimpleType.Type;
import org.geppetto.core.data.model.VariableList;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.ValuesFactory;
import org.geppetto.core.simulation.ISimulatorCallbackListener;

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

	private String _timeStepUnit = "ms";

	protected int _currentRecordingIndex = 0;

	protected List<RecordingModel> _recordings = new ArrayList<RecordingModel>();

	private double _runtime;

	public ASimulator(){};
	
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
		_currentRecordingIndex = 0;
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
		if(_recordings != null && isWatching())
		{
			AspectSubTreeNode watchTree = (AspectSubTreeNode) aspect.getSubTree(AspectTreeType.WATCH_TREE);
			watchTree.setModified(true);
			aspect.setModified(true);
			aspect.getParentEntity().setModified(true);
			
			if(watchTree.getChildren().isEmpty() || watchListModified())
			{
				for(RecordingModel recording : _recordings)
				{
					watchListModified(false);
					H5File file = recording.getHDF5();
					try {
						file.open();
					} catch (Exception e1) {
						throw new GeppettoExecutionException(e1);
					}
					for(String watchedVariable : this.getWatchList())
					{
						String path = "/"+watchedVariable.replace(watchTree.getInstancePath()+".", "");
						path = path.replace(".", "/");
						Dataset v = (Dataset) FileFormat.findObject(file, path);

						path = path.replaceFirst("/", "");
						StringTokenizer tokenizer = new StringTokenizer(path, "/");
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
									Object dataRead;
									try
									{
										dataRead = v.read();
										Type type =null;
										if(dataRead instanceof double[]){
											type = Type.fromValue(SimpleType.Type.DOUBLE.toString());
										}else if(dataRead instanceof int[]){
											type = Type.fromValue(SimpleType.Type.INTEGER.toString());
										}else if(dataRead instanceof float[]){
											type = Type.fromValue(SimpleType.Type.FLOAT.toString());
										}

										PhysicalQuantity quantity = new PhysicalQuantity();
										AValue readValue = null;
										switch(type)
										{
										case DOUBLE:
											double[] dr = (double[])dataRead;
											readValue = ValuesFactory.getDoubleValue(dr[_currentRecordingIndex]);
											break;
										case FLOAT:
											float[] fr = (float[])dataRead;
											readValue = ValuesFactory.getFloatValue(fr[_currentRecordingIndex]);
											break;
										case INTEGER:
											int[] ir = (int[])dataRead;
											readValue = ValuesFactory.getIntValue(ir[_currentRecordingIndex]);
											break;
										default:
											break;
										}
										quantity.setValue(readValue);
										newNode.addPhysicalQuantity(quantity);
										node.addChild(newNode);
									}

									catch(Exception | OutOfMemoryError e)
									{
										throw new GeppettoExecutionException(e);
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
					UpdateRecordingStateTreeVisitor updateStateTreeVisitor = new UpdateRecordingStateTreeVisitor(recording, watchTree.getInstancePath(),_listener, _currentRecordingIndex++);
					watchTree.apply(updateStateTreeVisitor);
					if(updateStateTreeVisitor.getError() != null)
					{
						_listener.endOfSteps(null);
						this.stopWatch();
						throw new GeppettoExecutionException(updateStateTreeVisitor.getError());
					}
					else if(updateStateTreeVisitor.getRange()!=null){
						_listener.endOfSteps(null);
						this.stopWatch();
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
		//TODO : Update code below to use own bindings jar vs netcdf one. 
		//Method being called from RecordingSimulator. 22/2/2015
//		if(_recordings != null)
//		{
//			for(RecordingModel recording : _recordings)
//			{
//				H5File file = recording.getHDF5();
//				List<AVariable> listToCheck = getWatchableVariables().getVariables();
//
//				for(AVariable var  : listToCheck)
//				{
//					String fullPath = hdfVariable.getFullName();
//					StringTokenizer stok = new StringTokenizer(fullPath, "/");
//
//					while(stok.hasMoreTokens())
//					{
//						String s = stok.nextToken();
//						String searchVar = s;
//
//						if(ArrayUtils.isArray(s))
//						{
//							searchVar = ArrayUtils.getArrayName(s);
//						}
//
//						AVariable v = getVariable(searchVar, listToCheck);
//
//						if(v == null)
//						{
//							if(stok.hasMoreTokens())
//							{
//								StructuredType structuredType = new StructuredType();
//								structuredType.setName(searchVar + "T");
//
//								if(ArrayUtils.isArray(s))
//								{
//									v = DataModelFactory.getArrayVariable(searchVar, structuredType, ArrayUtils.getArrayIndex(s) + 1);
//								}
//								else
//								{
//									v = DataModelFactory.getSimpleVariable(searchVar, structuredType);
//								}
//								listToCheck.add(v);
//								listToCheck = structuredType.getVariables();
//							}
//							else
//							{
//								SimpleType type = DataModelFactory.getCachedSimpleType(Type.fromValue(hdfVariable.getDataType().toString()));
//								if(ArrayUtils.isArray(s))
//								{
//									v = DataModelFactory.getArrayVariable(searchVar, type, ArrayUtils.getArrayIndex(s) + 1);
//								}
//								else
//								{
//									v = DataModelFactory.getSimpleVariable(searchVar, type);
//								}
//								listToCheck.add(v);
//							}
//						}
//						else
//						{
//							if(stok.hasMoreTokens())
//							{
//								listToCheck = ((StructuredType) v.getType()).getVariables();
//								if(ArrayUtils.isArray(s))
//								{
//									if(ArrayUtils.getArrayIndex(s) + 1 > ((ArrayVariable) v).getSize())
//									{
//										((ArrayVariable) v).setSize(ArrayUtils.getArrayIndex(s) + 1);
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
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
	
	@Override
	public double getTime() {
		return _runtime;
	}
	
	@Override
	public String getTimeStepUnit() {
		return _timeStepUnit;
	}
}
