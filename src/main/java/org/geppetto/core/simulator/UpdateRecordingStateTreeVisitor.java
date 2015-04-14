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

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

import org.geppetto.core.data.model.SimpleType;
import org.geppetto.core.data.model.SimpleType.Type;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.state.visitors.DefaultStateVisitor;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.ValuesFactory;
import org.geppetto.core.simulation.ISimulatorCallbackListener;

/**
 * @author matteocantarelli
 * 
 */
public class UpdateRecordingStateTreeVisitor extends DefaultStateVisitor
{

	private RecordingModel _recording;
	private String _instancePath;
	private String _errorMessage = null;
	private String _endOfSteps = null;
	private int _currentIndex;

	/**
	 * @param recording
	 * @param instancePath
	 * @param _listener
	 * @param currentIndex
	 */
	public UpdateRecordingStateTreeVisitor(RecordingModel recording, String instancePath, ISimulatorCallbackListener listener, int currentIndex)
	{
		_recording = recording;
		_instancePath = instancePath;
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
		String variable = node.getInstancePath().replace(_instancePath + ".", "").replace(".", "/");
		H5File file = _recording.getHDF5();
		String variablePath = "/" + variable.replace(".", "/");
		Dataset v = (Dataset) FileFormat.findObject(file, variablePath);
		if(v == null)
		{
			_errorMessage = variable + " not found in recording";
		}
		else
		{
			Object dataRead;
			try
			{
				dataRead = v.read();
				Type type = null;
				if(dataRead instanceof double[])
				{
					type = Type.fromValue(SimpleType.Type.DOUBLE.toString());
				}
				else if(dataRead instanceof int[])
				{
					type = Type.fromValue(SimpleType.Type.INTEGER.toString());
				}
				else if(dataRead instanceof float[])
				{
					type = Type.fromValue(SimpleType.Type.FLOAT.toString());
				}

				PhysicalQuantity quantity = new PhysicalQuantity();
				AValue readValue = null;
				switch(type)
				{
					case DOUBLE:
						double[] dr = (double[]) dataRead;
						readValue = ValuesFactory.getDoubleValue(dr[_currentIndex]);
						break;
					case FLOAT:
						float[] fr = (float[]) dataRead;
						readValue = ValuesFactory.getFloatValue(fr[_currentIndex]);
						break;
					case INTEGER:
						int[] ir = (int[]) dataRead;
						readValue = ValuesFactory.getIntValue(ir[_currentIndex]);
						break;
					default:
						break;
				}
				quantity.setValue(readValue);
				node.addPhysicalQuantity(quantity);
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				_endOfSteps = e.getMessage();
			}
			catch(Exception | OutOfMemoryError e)
			{
				_errorMessage = e.getMessage();
			}
		}
		return super.visitVariableNode(node);
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
