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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.quantities.Quantity;
import org.geppetto.core.model.quantities.Unit;
import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.ValuesFactory;

/**
 * @author matteocantarelli
 *
 */
public class RecordingReader
{
	private int currentRecordingIndex = 0;

	private RecordingModel recording;

	private boolean recordingOpened = false;

	public RecordingReader(RecordingModel recording)
	{
		super();
		this.recording = recording;
	}

	/**
	 * @param variables
	 * @param simulationTree
	 * @param readAll
	 * @throws GeppettoExecutionException
	 */
	public void readRecording(List<String> variables, AspectSubTreeNode simulationTree, boolean readAll) throws GeppettoExecutionException
	{
		openRecording();

		for(String watchedVariable : variables)
		{
			String path = "/" + watchedVariable.replace(simulationTree.getInstancePath() + ".", "");
			path = path.replace(".", "/");

			this.readVariable(path, recording.getHDF5(), simulationTree, readAll);
		}

		this.readVariable("/time", recording.getHDF5(), simulationTree, readAll);

		currentRecordingIndex++;
	}

	/**
	 * @return
	 */
	public int getAndIncrementCurrentIndex()
	{
		return currentRecordingIndex++;
	}

	/**
	 * @param path
	 * @param h5File
	 * @param parent
	 * @param readAll
	 * @throws GeppettoExecutionException
	 */
	public void readVariable(String path, H5File h5File, ACompositeNode parent, boolean readAll) throws GeppettoExecutionException
	{
		Dataset v = (Dataset) FileFormat.findObject(h5File, path);

		String unit = "";
		try
		{
			List metaData = v.getMetadata();
			Attribute unitAttr = (Attribute) metaData.get(1);
			unit = ((String[]) unitAttr.getValue())[0];
		}
		catch(Exception e1)
		{

		}

		path = path.replaceFirst("/", "");
		StringTokenizer tokenizer = new StringTokenizer(path, "/");
		VariableNode newVariableNode = null;
		while(tokenizer.hasMoreElements())
		{
			String current = tokenizer.nextToken();
			boolean found = false;
			for(ANode child : parent.getChildren())
			{
				if(child.getId().equals(current))
				{
					if(child instanceof ACompositeNode)
					{
						parent = (ACompositeNode) child;
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
					parent.addChild(newNode);
					parent = newNode;
				}
				else
				{
					// it's a leaf node
					VariableNode newNode = new VariableNode(current);
					Object dataRead;
					try
					{
						dataRead = v.read();
						Quantity quantity = new Quantity();
						AValue readValue = null;
						if(dataRead instanceof double[])
						{
							double[] dr = (double[]) dataRead;
							readValue = ValuesFactory.getDoubleValue(dr[currentRecordingIndex]);
						}
						else if(dataRead instanceof float[])
						{
							float[] fr = (float[]) dataRead;
							readValue = ValuesFactory.getFloatValue(fr[currentRecordingIndex]);
						}
						else if(dataRead instanceof int[])
						{
							int[] ir = (int[]) dataRead;
							readValue = ValuesFactory.getIntValue(ir[currentRecordingIndex]);
						}

						quantity.setValue(readValue);
						newNode.addQuantity(quantity);
						newNode.setUnit(new Unit(unit));
						newVariableNode = newNode;
						parent.addChild(newNode);
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
						Quantity quantity = new Quantity();
						readValue = ValuesFactory.getDoubleValue(dr[i]);
						quantity.setValue(readValue);
						newVariableNode.addQuantity(quantity);
					}
				}
				else if(dataRead instanceof float[])
				{
					float[] fr = (float[]) dataRead;
					for(int i = 0; i < fr.length; i++)
					{
						Quantity quantity = new Quantity();
						readValue = ValuesFactory.getDoubleValue(fr[i]);
						quantity.setValue(readValue);
						newVariableNode.addQuantity(quantity);
					}
				}
				else if(dataRead instanceof int[])
				{
					int[] ir = (int[]) dataRead;
					for(int i = 0; i < ir.length; i++)
					{
						Quantity quantity = new Quantity();
						readValue = ValuesFactory.getDoubleValue(ir[i]);
						quantity.setValue(readValue);
						newVariableNode.addQuantity(quantity);
					}
				}
				newVariableNode.setUnit(new Unit(unit));
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

	/**
	 * @return
	 */
	public int getRecordingIndex()
	{
		return currentRecordingIndex;
	}

	/**
	 * 
	 */
	public void resetRecordingIndex()
	{
		currentRecordingIndex = 0;
	}

	/**
	 * @param aspect
	 * @throws GeppettoExecutionException
	 */
	public void advanceRecordings(AspectNode aspect) throws GeppettoExecutionException
	{
		if(recording == null)
		{
			// no recordings = nothing to advance
			return;
		}

		// traverse scene root to get all simulation trees for all *CHILDREN* aspects
		ANode parentEntity = aspect.getParent();

		// find all children aspects
		GetAspectsVisitor mappingVisitor = new GetAspectsVisitor();
		parentEntity.apply(mappingVisitor);
		HashMap<String, AspectNode> aspects = mappingVisitor.getAspects();

		// iterate through children aspects
		Iterator it = aspects.entrySet().iterator();
		boolean updated = false;
		boolean endOfStepsReached = false;
		while(it.hasNext())
		{
			Map.Entry o = (Map.Entry) it.next();
			AspectNode a = (AspectNode) o.getValue();

			// get trees
			AspectSubTreeNode simulationTree = (AspectSubTreeNode) a.getSubTree(AspectTreeType.SIMULATION_TREE);
			AspectSubTreeNode visTree = (AspectSubTreeNode) a.getSubTree(AspectTreeType.VISUALIZATION_TREE);

			// set modified
			simulationTree.setModified(true);
			visTree.setModified(true);
			a.setModified(true);
			a.getParentEntity().setModified(true);

			if(!this.recordingOpened)
			{
				this.openRecording();
			}

			UpdateRecordingStateTreeVisitor updateStateTreeVisitor = new UpdateRecordingStateTreeVisitor(recording, getRecordingIndex());
			UpdateRecordingStateTreeVisitor updateVisTreeVisitor = new UpdateRecordingStateTreeVisitor(recording, getRecordingIndex());

			// apply visitors
			// TODO: improvement --> only visit is there's nodes populated in the tree otherwise we are traversing for nothing
			simulationTree.apply(updateStateTreeVisitor);
			visTree.apply(updateVisTreeVisitor);

			if(updateStateTreeVisitor.getError() != null || updateVisTreeVisitor.getError() != null)
			{
				// something went wrong - notify recording is stopped and close recording files
				// TODO Review this
				// listener.endOfSteps(null,null);
				closeRecording();

				// bubble up exception with errors
				String errorMsg = String.format("Simulation tree: %s | Visualization tree: %s", updateStateTreeVisitor.getError(), updateVisTreeVisitor.getError());
				throw new GeppettoExecutionException(errorMsg);
			}
			else if(updateStateTreeVisitor.getRange() != null || updateVisTreeVisitor.getRange() != null)
			{
				// recording reached the end - notify listener
				// TODO Review this
				//listener.endOfSteps(null,null);

				// set end of steps flag reached
				// NOTE: cannot break the loop here because more than one aspect might be reading the last step
				endOfStepsReached = true;
			}
			else
			{
				// if none of the above trees have been updated
				updated = true;
			}
		}

		// NOTE: we cannot increase counter inside the loop above otherwise it would increase per each aspect
		// NOTE: recordings steps would be skipped if the same recording is applied from parent to children aspects
		if(updated)
		{
			getAndIncrementCurrentIndex();
		}

		if(endOfStepsReached)
		{
			resetAndCloseRecordings();
		}
	}

	private void openRecording() throws GeppettoExecutionException
	{
		// loop through recordings and open them for reading

		try
		{
			H5File h5file = recording.getHDF5();
			h5file.open();
		}
		catch(Exception e1)
		{
			throw new GeppettoExecutionException(e1);
		}

		this.recordingOpened = true;
	}

	private void closeRecording() throws GeppettoExecutionException
	{

		if(this.recordingOpened)
		{
			// loop through recordings and open them for reading

			try
			{
				H5File h5file = recording.getHDF5();
				h5file.close();
			}
			catch(Exception e1)
			{
				throw new GeppettoExecutionException(e1);
			}

			this.recordingOpened = false;
		}
	}

	private void resetAndCloseRecordings() throws GeppettoExecutionException
	{
		closeRecording();
		currentRecordingIndex = 0;
	}
}
