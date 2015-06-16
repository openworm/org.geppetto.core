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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.quantities.Quantity;
import org.geppetto.core.model.runtime.SkeletonAnimationNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.state.visitors.DefaultStateVisitor;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.ValuesFactory;
import org.geppetto.core.utilities.StringSplitter;

/**
 * @author matteocantarelli
 * 
 */
public class UpdateRecordingStateTreeVisitor extends DefaultStateVisitor
{

	private RecordingModel _recording;
	private String _errorMessage = null;
	private String _endOfSteps = null;
	private int _currentIndex;

	/**
	 * @param recording
	 * @param instancePath
	 * @param currentIndex
	 */
	public UpdateRecordingStateTreeVisitor(RecordingModel recording, int currentIndex)
	{
		_recording = recording;
		_currentIndex = currentIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.state.visitors.DefaultStateVisitor#visitSimpleStateNode(org.geppetto.core.model.state.SimpleStateNode)
	 */
	@Override
	public boolean visitVariableNode(VariableNode node)
	{
		String variable = node.getInstancePath();
		H5File file = _recording.getHDF5();
		String variablePath = "/" + variable.replace(".", "/");
		Dataset v = (Dataset) FileFormat.findObject(file, variablePath);
		if(v != null)
		{
			Object dataRead;
			try
			{
				dataRead = v.read();
				Quantity quantity = new Quantity();
				AValue readValue = null;
				if(dataRead instanceof double[]){
					double[] dr = (double[])dataRead;
					readValue = ValuesFactory.getDoubleValue(dr[_currentIndex]);
				}else if(dataRead instanceof float[]){
					float[] fr = (float[])dataRead;
					readValue = ValuesFactory.getFloatValue(fr[_currentIndex]);
				}else if(dataRead instanceof int[]){
					int[] ir = (int[])dataRead;
					readValue = ValuesFactory.getIntValue(ir[_currentIndex]);
				}
				quantity.setValue(readValue);
				node.addQuantity(quantity);
			}
			catch (ArrayIndexOutOfBoundsException  e) {
				_endOfSteps = e.getMessage();
			}
			catch(Exception | OutOfMemoryError e)
			{
				_errorMessage = e.getMessage();
			}
		}
		return super.visitVariableNode(node);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.state.visitors.DefaultStateVisitor#visitSimpleStateNode(org.geppetto.core.model.state.SimpleStateNode)
	 */
	@Override
	public boolean visitSkeletonAnimationNode(SkeletonAnimationNode node)
	{
		String variable = node.getInstancePath();
		H5File file = _recording.getHDF5();
		String variablePath = "/" + variable.replace(".", "/");
		Dataset v = (Dataset) FileFormat.findObject(file, variablePath);
		if(v != null)
		{
			Object dataRead;
			try
			{
				dataRead = v.read();
				
				// get metadata from recording node
				List<Attribute> attributes = v.getMetadata();
				String meta = "";
				
				for(Attribute a : attributes){
					if(a.getName().equals("custom_metadata"))
						meta = ((String[])a.getValue())[0];
				}
				
				// split into key value pair
				Map<String, String> metaMap = StringSplitter.keyValueSplit(meta, ";");
				
				double[] flatMatrices = null;
				if(dataRead instanceof double[])
				{
					double[] dr = (double[])dataRead;
					
					// get items of interest based on matrix dimension and items per step
					int itemsPerStep = Integer.parseInt(metaMap.get("items_per_step"));
					int startIndex = _currentIndex*itemsPerStep;
					int endIndex = startIndex + (itemsPerStep);
					
					if(endIndex <= dr.length)
					{
						flatMatrices = Arrays.copyOfRange(dr, startIndex, endIndex);
					}
					else
					{
						throw new ArrayIndexOutOfBoundsException("ArrayIndexOutOfBounds");
					}
				}
				
				// data structures to hold matrices as they get built
				List<List<List<Double>>> transformations = new ArrayList<List<List<Double>>>();
				List<List<Double>> matrix = new ArrayList<List<Double>>();
				List<Double> row = new ArrayList<Double>();
				int dimension = Integer.parseInt(metaMap.get("dimension"));
				
				// build list of matrices
				for(int i=0; i<flatMatrices.length; i++)
				{
					// build rows and add to matrix when completed
					if(row.size() <dimension)
					{
						// add to row
						row.add((Double)flatMatrices[i]);
					}
					
					// check if row is completed
					if(row.size() == dimension)
					{
						// row completed - add to matrix
						matrix.add(row);
						// init new row
						row = new ArrayList<Double>();
					}
					
					// check if matrix is completed and add to list of matrices
					if(matrix.size() == dimension)
					{
						// matrix is complted - add to list of matrices
						transformations.add(matrix);
						// create new matrix
						matrix = new ArrayList<List<Double>>();
					}
					
				}
				
				// set matrices on skeleton animation node
				node.setSkeletonAnimationMatrices(transformations);
			}
			catch (ArrayIndexOutOfBoundsException  e) {
				_endOfSteps = e.getMessage();
			}
			catch(Exception | OutOfMemoryError e)
			{
				_errorMessage = e.getMessage();
			}
		}
		
		return super.visitSkeletonAnimationNode(node);
	}
	
	/**
	 * @return
	 */
	public String getError()
	{
		return _errorMessage;
	}

	/**
	 * @return
	 */
	public String getRange()
	{
		return _endOfSteps;
	}
}
