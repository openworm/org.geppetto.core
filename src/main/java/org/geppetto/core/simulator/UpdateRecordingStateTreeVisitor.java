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

import org.geppetto.core.data.model.SimpleType.Type;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.quantities.PhysicalQuantity;
import org.geppetto.core.model.runtime.ATimeSeriesNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.state.visitors.DefaultStateVisitor;
import org.geppetto.core.model.values.AValue;
import org.geppetto.core.model.values.ValuesFactory;
import org.geppetto.core.simulation.ISimulatorCallbackListener;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;

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
	private ISimulatorCallbackListener _listener;

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
		_listener = listener;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.model.state.visitors.DefaultStateVisitor#visitSimpleStateNode(org.geppetto.core.model.state.SimpleStateNode)
	 */
	@Override
	public boolean visitVariableNode(VariableNode node)
	{
		String variable = node.getInstancePath().replace(_instancePath + ".", "").replace(".", "/");
		Variable v = _recording.getHDF5().findVariable(variable);
		if(v == null)
		{
			_errorMessage = variable + " not found in recording";
		}
		else
		{
			int[] start = { _currentIndex};
			int[] lenght = {1};
			Array value;
			try
			{
				value = v.read(start, lenght);
				Type type = Type.fromValue(v.getDataType().toString());

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
				node.addPhysicalQuantity(quantity);
			}
			catch(IOException e)
			{
				_errorMessage = e.getMessage();
			} catch (InvalidRangeException e) {
				_endOfSteps = e.getMessage();
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
