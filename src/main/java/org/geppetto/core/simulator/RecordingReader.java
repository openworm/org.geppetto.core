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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IInstancePath;
import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.quantities.Unit;
import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
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
	
	protected int currentRecordingIndex = 0;
	
	public void readRecording(H5File h5File,List<String> variables, AspectSubTreeNode simulationTree, boolean readAll) throws GeppettoExecutionException
	{
		try
		{
			h5File.open();
		} catch (Exception e1) {
			throw new GeppettoExecutionException(e1);
		}
		
		for(String watchedVariable : variables)
		{
			String path = "/" + watchedVariable.replace(simulationTree.getInstancePath() + ".", "");
			path = path.replace(".", "/");
			
			this.readVariable(path, h5File, simulationTree, readAll);
		}
		
		this.readVariable("/time", h5File, simulationTree, readAll);
		
		currentRecordingIndex++;
	}
	
	
	public int getAndIncrementCurrentIndex()
	{
		return currentRecordingIndex++;
	}

	public void readVariable(String path, H5File h5File, ACompositeNode parent, boolean readAll) throws GeppettoExecutionException{
		Dataset v = (Dataset) FileFormat.findObject(h5File, path);

		String unit = "";
		try {
			List metaData = v.getMetadata();
			Attribute unitAttr = (Attribute)metaData.get(1);
			unit = ((String[])unitAttr.getValue())[0];
		} catch (Exception e1) {
			
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
						PhysicalQuantity quantity = new PhysicalQuantity();
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
						quantity.setUnit(unit);
						newNode.addPhysicalQuantity(quantity);
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
						PhysicalQuantity quantity = new PhysicalQuantity();
						readValue = ValuesFactory.getDoubleValue(dr[i]);
						quantity.setValue(readValue);
						quantity.setUnit(unit);
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
						quantity.setUnit(unit);
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
						quantity.setUnit(unit);
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
