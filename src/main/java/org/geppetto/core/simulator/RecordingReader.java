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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

import org.apache.commons.lang.ArrayUtils;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IInstancePath;
import org.geppetto.core.data.model.ResultsFormat;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.utilities.StringSplitter;
import org.geppetto.model.GeppettoFactory;
import org.geppetto.model.ExperimentState;
import org.geppetto.model.VariableValue;
import org.geppetto.model.values.Pointer;
import org.geppetto.model.values.Quantity;
import org.geppetto.model.values.SkeletonAnimation;
import org.geppetto.model.values.SkeletonTransformation;
import org.geppetto.model.values.TimeSeries;
import org.geppetto.model.values.Unit;
import org.geppetto.model.values.Value;
import org.geppetto.model.values.ValuesFactory;

/**
 * @author matteocantarelli
 * @author giovanniidili
 */
public class RecordingReader
{
	private int currentRecordingIndex = 0;

	private RecordingModel recording;

	private ResultsFormat recordingFormat;

	private boolean recordingOpened = false;

	public RecordingReader(RecordingModel recording)
	{
		this(recording, ResultsFormat.GEPPETTO_RECORDING);
	}

	public RecordingReader(RecordingModel recording, ResultsFormat format)
	{
		super();
		this.recording = recording;
		this.setRecordingFormat(format);
	}

	/**
	 * @param variables
	 * @param tree
	 * @param readAll
	 * @throws GeppettoExecutionException
	 */
	public void readRecording(IInstancePath variable, ExperimentState modelState, boolean readAll) throws GeppettoExecutionException
	{
		openRecording();

		String recordingVariablePath = "/" + variable.getInstancePath();

		recordingVariablePath = recordingVariablePath.replace(".", "/");

		this.readVariable(recordingVariablePath, recording.getHDF5(), modelState, readAll);

		this.readVariable("/time", recording.getHDF5(), modelState, readAll);

		if(!readAll)
		{
			currentRecordingIndex++;
		}
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
	public void readVariable(String path, H5File h5File, ExperimentState modelState, boolean readAll) throws GeppettoExecutionException
	{
		try
		{
			Dataset v = (Dataset) FileFormat.findObject(h5File, path);

			VariableValue variableValue = GeppettoFactory.eINSTANCE.createVariableValue();
			modelState.getRecordedVariables().add(variableValue);

			Value value = null;

			Unit unit = ValuesFactory.eINSTANCE.createUnit();

			String metaType = "";
			Map<String, String> custom = null;

			// get metadata from recording node
			List<Attribute> attributes = v.getMetadata();

			for(Attribute a : attributes)
			{
				if(a.getName().toLowerCase().equals("unit"))
				{
					unit.setUnit(((String[]) a.getValue())[0]);
				}
				else if(a.getName().toLowerCase().equals("meta_type") || a.getName().toLowerCase().equals("metatype"))
				{
					// FIXME Why the two of them? Why hardcoded strings? Consolidate and remove one of the two
					metaType = ((String[]) a.getValue())[0];
				}
				else if(a.getName().toLowerCase().equals("custom_metadata"))
				{
					String customStr = ((String[]) a.getValue())[0];
					custom = StringSplitter.keyValueSplit(customStr, ";");
				}

			}

			Object readData = v.read();

			if(metaType.contains("VariableNode") || metaType.contains("STATE_VARIABLE"))
			{

				value = ValuesFactory.eINSTANCE.createTimeSeries();
				((TimeSeries) value).setUnit(unit);
				double[] dr = (double[]) readData;

				if(readData instanceof double[])
				{

					if(!readAll)
					{
						Quantity quantity = ValuesFactory.eINSTANCE.createQuantity();
						quantity.setValue(dr[currentRecordingIndex]);
						((TimeSeries) value).getQuantities().add(quantity);
					}
					else
					{
						for(int i = 0; i < dr.length; i++)
						{
							Quantity quantity = ValuesFactory.eINSTANCE.createQuantity();
							quantity.setValue(dr[i]);
							((TimeSeries) value).getQuantities().add(quantity);
						}

					}

				}
				else if(readData instanceof float[])
				{
					throw new GeppettoExecutionException("Only double values supported");
				}
				else if(readData instanceof int[])
				{
					throw new GeppettoExecutionException("Only double values supported");
				}

			}
			else if(metaType.contains("VISUAL_TRANSFORMATION"))
			{
				value = ValuesFactory.eINSTANCE.createSkeletonAnimation();
				double[] dr = (double[]) readData;
				if(!readAll)
				{
					double[] flatMatrices = null;
					if(readData instanceof double[])
					{

						// get items of interest based on matrix dimension and items per step
						int itemsPerStep = Integer.parseInt(custom.get("items_per_step"));
						int startIndex = currentRecordingIndex * itemsPerStep;
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

					// set matrices on skeleton animation node
					SkeletonTransformation skeletonTransformation = ValuesFactory.eINSTANCE.createSkeletonTransformation();
					skeletonTransformation.getSkeletonTransformation().addAll(Arrays.asList(ArrayUtils.toObject(flatMatrices)));
					((SkeletonAnimation) value).getSkeletonTransformationSeries().add(skeletonTransformation);
				}
				else
				{
					if(readData instanceof double[])
					{
						// get items of interest based on matrix dimension and items per step
						int itemsPerStep = Integer.parseInt(custom.get("items_per_step"));
						int totalSteps = dr.length / itemsPerStep;

						for(int i = 0; i < totalSteps; i++)
						{
							double[] flatMatrices = null;

							int startIndex = i * itemsPerStep;
							int endIndex = startIndex + (itemsPerStep);

							if(endIndex <= dr.length)
							{
								flatMatrices = Arrays.copyOfRange(dr, startIndex, endIndex);
							}
							else
							{
								throw new ArrayIndexOutOfBoundsException("ArrayIndexOutOfBounds");
							}

							// set matrices on skeleton animation node
							SkeletonTransformation skeletonTransformation = ValuesFactory.eINSTANCE.createSkeletonTransformation();
							skeletonTransformation.getSkeletonTransformation().addAll(Arrays.asList(ArrayUtils.toObject(flatMatrices)));
							((SkeletonAnimation) value).getSkeletonTransformationSeries().add(skeletonTransformation);
						}
					}
				}
			}
			variableValue.setValue(value);
			variableValue.setPointer(createPointer(path));

		}
		catch(Exception e)
		{
			throw new GeppettoExecutionException("Error reading a variable from the recording", e);
		}

	}

	private Pointer createPointer(String path)
	{
		Pointer pointer = ValuesFactory.eINSTANCE.createPointer();
		// IT FIXME
		return pointer;
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



	private void openRecording() throws GeppettoExecutionException
	{
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



	public ResultsFormat getRecordingFormat()
	{
		return recordingFormat;
	}

	public void setRecordingFormat(ResultsFormat recordingFormat)
	{
		this.recordingFormat = recordingFormat;
	}
}
