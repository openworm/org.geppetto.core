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
package org.geppetto.core.recordings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.utils.SetNatives;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoExecutionException;

/**
 * Creates HDF5 Recordings file. API can take different array datasets and single values for integers, doubles and floats.
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class GeppettoRecordingCreator
{

	private static Log _logger = LogFactory.getLog(GeppettoRecordingCreator.class);

	private String _fileName;
	private H5File recordingsH5File;
	private HashMap<String, RecordingObject> map = new HashMap<String, RecordingObject>();

	private String _timeStepUnit;
	private float _timeStep = 0;
	private float[] _timePoints;

	public enum MetaType
	{
		// FIXME This is not recording specific, move me elsewhere
		Variable_Node("VariableNode"), Parameter_Node("ParameterNode"), AspectSubtree_Node("AspectSubtreeNode"), Composite_Node("CompositeNode");

		private String type;

		private MetaType(String type)
		{
			this.type = type;
		}

		@Override
		public String toString()
		{
			return type;
		}
	};

	public GeppettoRecordingCreator(String name)
	{
		_fileName = name;
	}

	/*
	 * Begins declaration of public methods, meant to be used as part of the API this class offers.
	 */
	/**
	 * Returns H5File that was created for this recording
	 * 
	 * @return - HDF5 recordings file
	 */
	public H5File getRecordingsFile()
	{
		return this.recordingsH5File;
	}

	/**
	 * Returns name of recordings file
	 * 
	 * @return - Recordings file name
	 */
	public String getName()
	{
		return _fileName;
	}

	/**
	 * Create new HDF5File for recordings, or retrieve existing one with same name.
	 * 
	 * @throws Exception
	 */
	public void create() throws Exception
	{
		try
		{
			SetNatives.getInstance().setHDF5Native(System.getProperty("user.dir"));

			// retrieve an instance of H5File
			FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

			if(fileFormat == null)
			{
				_logger.error("Cannot find HDF5 FileFormat.");
				throw new GeppettoExecutionException("Cannot find HDF5 FileFormat.");
			}

			// Create instance pointing to file name
			recordingsH5File = (H5File) fileFormat.createInstance(_fileName, FileFormat.WRITE);

			// check if file already exists, don't create if it does
			if(!recordingsH5File.exists())
			{
				try
				{
					recordingsH5File = (H5File) fileFormat.createFile(_fileName, FileFormat.FILE_CREATE_DELETE);
				}
				catch(Exception e)
				{
					throw new GeppettoExecutionException(e);
				}
			}

			// open file, needs to do it to write or read from it
			recordingsH5File.open();

			// keep track of array with biggest length
			int biggestLength = 0;

			// Add recording variables in map to hdf5 file
			Set<String> mapSet = map.keySet();
			Iterator<String> iterator = mapSet.iterator();
			while(iterator.hasNext())
			{
				RecordingObject o = map.get(iterator.next());
				superAddValues(o);
				recordingsH5File.reloadTree(this.getRoot());
				// match biggest array length
				if(biggestLength < o.getValuesLength())
				{
					biggestLength = o.getValuesLength();
				}
			}

			float[] timeValues = new float[biggestLength];
			if(_timePoints == null && _timeStep > 0)
			{
				float time = 0;
				for(int i = 0; i < biggestLength; i++)
				{
					timeValues[i] = time;
					time = time + this._timeStep;
				}
			}
			else if(_timePoints == null && _timeStep <= 0)
			{
				for(int i = 0; i < biggestLength; i++)
				{
					timeValues[i] = i;
				}
			}
			else
			{
				timeValues = _timePoints;
			}

			HObject time = FileFormat.findObject(recordingsH5File, "/time");
			if(time == null)
			{
				createTime(biggestLength, timeValues);
			}
			recordingsH5File.close();
		}
		catch(IOException e)
		{
			throw new GeppettoExecutionException(e);
		}
	}

	public void addTimeStep(float timeStep, String timeStepUnit)
	{
		this._timeStep = timeStep;
		this._timeStepUnit = timeStepUnit;
	}

	public void addTimeStep(double timeStep, String timeStepUnit)
	{
		this._timeStep = (float) timeStep;
		this._timeStepUnit = timeStepUnit;
	}

	public void addTimePoints(float[] timePoints, String timeStepUnit)
	{
		this._timePoints = timePoints;
		this._timeStepUnit = timeStepUnit;
	}

	/**
	 * Calls overloaded method to add values to file. Passes it parameter that it was passed, plus a Datatype that will tell global method what kind of data it will be storing
	 * 
	 * @param variable
	 *            - Name of Variable to create
	 * @param values
	 *            - Values of data set to store
	 * @param unit
	 *            - Unit of measurement for data
	 * @param metaType
	 *            - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable, double value, String unit, MetaType metaType, boolean update)
	{
		// Convert single number into array, to store as dataset
		double[] values = new double[1];
		values[0] = value;
		addValues(variable, values, unit, metaType, update);
	}

	/**
	 * Calls overloaded method to add values to file. Passes it parameter that it was passed, plus a Datatype that will tell global method what kind of data it will be storing
	 * 
	 * @param variable
	 *            - Name of Variable to create
	 * @param values
	 *            - Values of data set to store
	 * @param unit
	 *            - Unit of measurement for data
	 * @param metaType
	 *            - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable, int value, String unit, MetaType metaType, boolean update)
	{
		// Convert single number into array, to store as dataset
		int[] values = new int[1];
		values[0] = value;
		addValues(variable, values, unit, metaType, update);
	}

	/**
	 * Calls overloaded method to add values to file. Passes it parameter that it was passed, plus a Datatype that will tell global method what kind of data it will be storing
	 * 
	 * @param variable
	 *            - Name of Variable to create
	 * @param values
	 *            - Values of data set to store
	 * @param unit
	 *            - Unit of measurement for data
	 * @param metaType
	 *            - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable, float value, String unit, MetaType metaType, boolean update) throws Exception
	{
		// Convert single number into array, to store as dataset
		float[] values = new float[1];
		values[0] = value;
		addValues(variable, values, unit, metaType, update);
	}

	/**
	 * Stores information in a Recording Object, which is then placed inside a map. Then variable will be created later when create() method is called.
	 * 
	 * @param variable
	 *            - Name of Variable to create
	 * @param values
	 *            - Values of data set to store
	 * @param unit
	 *            - Unit of measurement for data
	 * @param metaType
	 *            - Type of node, either variable or parameter node
	 * @throws Exception
	 * 
	 */
	public void addValues(String variable, double[] values, String unit, MetaType metaType, boolean update)
	{
		RecordingObject o = new RecordingObject();
		o.setMetaType(metaType);
		o.setVariable(variable);
		o.setUnit(unit);
		o.setValues(values);
		o.setDataType(Datatype.CLASS_FLOAT);
		o.setDataBytes(16);
		o.setValuesLenght(values.length);
		if(map.containsKey(variable) && update)
		{
			RecordingObject object = map.get(variable);
			double[] oldValues = (double[]) object.getValues();
			oldValues = ArrayUtils.addAll(oldValues, values);
			object.setValues(oldValues);
			object.setValuesLenght(oldValues.length);
			// map.put(variable, object);
		}
		else
		{
			map.put(variable, o);
		}
	}

	/**
	 * Stores information in a Recording Object, which is then placed inside a map. Then variable will be created later when create() method is called.
	 * 
	 * @param variable
	 *            - Name of Variable to create
	 * @param values
	 *            - Values of data set to store
	 * @param unit
	 *            - Unit of measurement for data
	 * @param metaType
	 *            - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable, int[] values, String unit, MetaType metaType, boolean update)
	{
		RecordingObject o = new RecordingObject();
		o.setMetaType(metaType);
		o.setVariable(variable);
		o.setUnit(unit);
		o.setValues(values);
		o.setDataType(Datatype.CLASS_INTEGER);
		o.setDataBytes(4);
		o.setValuesLenght(values.length);
		if(map.containsKey(variable) && update)
		{
			RecordingObject object = map.get(variable);
			int[] oldValues = (int[]) object.getValues();
			oldValues = ArrayUtils.addAll(oldValues, values);
			object.setValues(oldValues);
			object.setValuesLenght(oldValues.length);
		}
		else
		{
			map.put(variable, o);
		}
	}

	/**
	 * Stores information in a Recording Object, which is then placed inside a map. Then variable will be created later when create() method is called.
	 * 
	 * @param variable
	 *            - Name of Variable to create
	 * @param values
	 *            - Values of data set to store
	 * @param unit
	 *            - Unit of measurement for data
	 * @param metaType
	 *            - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable, float[] values, String unit, MetaType metaType, boolean update)
	{
		RecordingObject o = new RecordingObject();
		o.setMetaType(metaType);
		o.setVariable(variable);
		o.setUnit(unit);
		o.setValues(values);
		o.setDataType(Datatype.CLASS_FLOAT);
		o.setDataBytes(16);
		o.setValuesLenght(values.length);
		if(map.containsKey(variable) && update)
		{
			RecordingObject object = map.get(variable);
			float[] oldValues = (float[]) object.getValues();
			oldValues = ArrayUtils.addAll(oldValues, values);
			object.setValues(oldValues);
			object.setValuesLenght(oldValues.length);
		}
		else
		{
			map.put(variable, o);
		}
	}

	/*
	 * Begins declaration of internal private methods
	 */
	/**
	 * Get root of recordings file
	 * 
	 * @return
	 * @throws GeppettoExecutionException
	 */
	private Group getRoot() throws GeppettoExecutionException
	{
		return (Group) ((javax.swing.tree.DefaultMutableTreeNode) recordingsH5File.getRootNode()).getUserObject();
	}

	/**
	 * General method for adding datasets to an HDF5 File. The parameter dataType it's what makes the data stored in the dataset different from others; it could be an array or single number of
	 * integers, doubles or floats.
	 * 
	 * @param recordingObject
	 *            - Object containing the information of an object to be added to the HDF5 file.
	 * @throws Exception
	 */
	public void superAddValues(RecordingObject recordingObject) throws Exception
	{
		// Get group root of file, this should contain all groups
		Group root = getRoot();
		// Create path by replacing . in desired variable name by /
		String path = "/" + recordingObject.getVariable().replace(".", "/");
		// Try to find variable in file
		HObject v = FileFormat.findObject(recordingsH5File, path);
		// Variable not found, let's create it
		if(v == null)
		{

			_logger.warn("Creating variable " + recordingObject.getVariable());

			String[] splitByPeriod = recordingObject.getVariable().split("\\.");

			/**
			 * Split variable name by the . and use each string to create a group from it and attach it to parent group, which at start is the root. If group is found, retrieves it and keeps checking
			 * the rest of the path. Once all groups have been created for the path, create the dataset and attach it to last group created.
			 */
			Group current = root;
			String currentTag = recordingObject.getVariable();
			String currentPath = "";
			for(int s = 0; s < splitByPeriod.length - 1; s++)
			{
				currentTag = splitByPeriod[s];
				currentPath = currentPath.concat("/" + currentTag);
				current = createGroup(current, currentTag, currentPath);
			}
			// last part of path will be dataset
			// e.g. If variable given was P.J then P is group object, while
			// J is a dataset
			currentTag = splitByPeriod[splitByPeriod.length - 1];

			this.createDataSet(recordingObject, current, currentTag);
		}
		// else
		// {
		// if(!recordingObject.getVariable().equals("time"))
		// {
		// _logger.warn("File already contains variable " + recordingObject.getVariable());
		// // retrieve the dataset "2D 32-bit integer 20x10"
		// Dataset dataset = (Dataset) v;
		// Object dataRead = dataset.read();
		// // update dataset according to its type
		// if(dataRead instanceof double[])
		// {
		// double[] existingValues = (double[]) dataRead;
		// this.recordingsH5File.delete(dataset);
		// this.updateDataSet(dataset.getName(), existingValues, recordingObject);
		// }
		// else if(dataRead instanceof int[])
		// {
		// int[] existingValues = (int[]) dataRead;
		// this.recordingsH5File.delete(dataset);
		// this.updateDataSet(dataset.getName(), existingValues, recordingObject);
		// }
		// else if(dataRead instanceof float[])
		// {
		// float[] existingValues = (float[]) dataRead;
		// this.recordingsH5File.delete(dataset);
		// this.updateDataSet(dataset.getName(), existingValues, recordingObject);
		//
		// }
		// }
		// }
	}

	/**
	 * Creates time array
	 * 
	 * @param biggestLength
	 *            - Amount of time steps to include in time array
	 * @param timeValues
	 *            - Values of time
	 * @throws GeppettoExecutionException
	 * @throws Exception
	 */
	private void createTime(int biggestLength, float[] timeValues) throws GeppettoExecutionException, Exception
	{
		// dimension of dataset, length of array and 1 column
		long[] dims2D = { biggestLength, 1 };

		// we will be storing integers as our data type
		Datatype dataType = recordingsH5File.createDatatype(Datatype.CLASS_FLOAT, 16, Datatype.NATIVE, Datatype.NATIVE);
		// create 1D 32-bit float dataset
		Dataset dataset = recordingsH5File.createScalarDS("time", getRoot(), dataType, dims2D, null, null, 0, timeValues);
		// add attributes for unit and metatype
		createAttributes(null, this._timeStepUnit, dataset);
	}

	/**
	 * Adds attributes to dataset.
	 * 
	 * @param type
	 *            - Metatype of dataset (VariableNode or ParameterNode)
	 * @param unit
	 *            - Unit of measurement for datasets
	 * @param dataset
	 *            - Dataset that will be parent to attributes
	 * @throws HDF5Exception
	 */
	private void createAttributes(String type, String unit, Dataset dataset) throws HDF5Exception
	{
		// One piece of information per attribute, e.g. one for
		// unit and one for metatype
		long[] attrDims = { 1 };
		// create attribute holding metatype (parameternode or variablenode)
		if(type != null)
		{
			if(!type.equals(""))
			{
				String[] classValue = { type };
				Datatype attrType = new H5Datatype(Datatype.CLASS_STRING, classValue[0].length() + 1, -1, -1);
				Attribute attr = new Attribute("MetaType", attrType, attrDims);
				attr.setValue(classValue);
				recordingsH5File.writeAttribute(dataset, attr, false);
			}
		}

		// create attribute holding unit of measurement
		if(unit != null)
		{
			if(!unit.equals(""))
			{
				String[] classValue2 = { unit };
				Datatype attrType2 = new H5Datatype(Datatype.CLASS_STRING, classValue2[0].length() + 1, -1, -1);
				Attribute attr2 = new Attribute("Unit", attrType2, attrDims);
				attr2.setValue(classValue2);
				recordingsH5File.writeAttribute(dataset, attr2, false);
			}
		}
	}

	/**
	 * Stores data in a dataset object. This is where the recording values are stored
	 * 
	 * @param recordingObject
	 *            - Object holding parameters for creating the dataset
	 * @param parentObject
	 *            - Object that will have this dataset as child
	 * @param name
	 *            - Name of the dataset
	 * @return
	 * @throws Exception
	 */
	private Dataset createDataSet(RecordingObject recordingObject, Group parentObject, String name) throws Exception
	{
		// dimension of dataset, length of array and 1 column
		long[] dims2D = { recordingObject.getValuesLength(), 1 };

		// we will be storing integers as our data type
		Datatype dataType = recordingsH5File.createDatatype(recordingObject.getDataType(), recordingObject.getDataBytes(), Datatype.NATIVE, Datatype.NATIVE);
		// create 1D 32-bit float dataset
		Dataset dataset = recordingsH5File.createScalarDS(name, parentObject, dataType, dims2D, null, null, 0, recordingObject.getValues());

		// add attributes for unit and metatype
		createAttributes(recordingObject.getMetaType().toString(), recordingObject.getUnit(), dataset);

		return dataset;
	}

	/**
	 * Creates a part of the variable, a Group member object. e.g. For "a.b.c", it would create group "b" with tag "b", parent "a".
	 * 
	 * @param parent
	 *            - Will hold the new group
	 * @param tag
	 *            - Name of group
	 * @param path
	 *            - Path of variable
	 * @return
	 * @throws Exception
	 */
	private Group createGroup(Group parent, String tag, String path) throws Exception
	{
		// try to find if group already exists
		HObject findObject = FileFormat.findObject(recordingsH5File, path);
		if(findObject == null)
		{
			Group newGroup = recordingsH5File.createGroup(tag, parent);
			parent.addToMemberList(newGroup);
			parent = newGroup;
		}
		else
		{
			parent = (Group) findObject;
		}

		return parent;
	}

	/**
	 * Updates existing dataset for a variable by extracting old values and combining them with new values before writing to file.
	 * 
	 * @param name
	 *            - Name of dataset to create
	 * @param existingValues
	 *            -Values to store in dataset
	 * @param recordingObject
	 *            - Object holding parameter info for creation
	 * @param path
	 *            - The path of the object with "." replaced by "/"
	 * @throws Exception
	 */
	private void updateDataSet(String name, double[] existingValues, RecordingObject recordingObject) throws Exception
	{
		int setLength = existingValues.length;
		double[] newData = new double[existingValues.length + recordingObject.getValuesLength()];
		for(int i = 0; i < existingValues.length; i++)
		{
			newData[i] = existingValues[i];
		}

		double[] newValues = (double[]) recordingObject.getValues();
		for(int i = 0; i < newValues.length; i++)
		{
			newData[setLength] = newValues[i];
			setLength++;
		}
		String path = "/" + recordingObject.getVariable().replace(".", "/");
		String parent = path.replace("/" + name, "");
		Group datasetParent = (Group) FileFormat.findObject(recordingsH5File, parent);
		recordingObject.setDataBytes(16);
		recordingObject.setDataType(Datatype.CLASS_FLOAT);
		recordingObject.setVariable(name);
		recordingObject.setValues(newData);
		recordingObject.setValuesLenght(newData.length);
		this.createDataSet(recordingObject, datasetParent, name);
	}

	/**
	 * Updates existing dataset for a variable by extracting old values and combining them with new values before writing to file.
	 * 
	 * @param name
	 *            - Name of dataset to create
	 * @param existingValues
	 *            -Values to store in dataset
	 * @param recordingObject
	 *            - Object holding parameter info for creation
	 * @param path
	 *            - The path of the object with "." replaced by "/"
	 * @throws Exception
	 */
	private void updateDataSet(String name, int[] existingValues, RecordingObject recordingObject) throws Exception
	{
		int setLength = existingValues.length;
		int[] newData = new int[existingValues.length + recordingObject.getValuesLength()];
		for(int i = 0; i < existingValues.length; i++)
		{
			newData[i] = existingValues[i];
		}

		int[] newValues = (int[]) recordingObject.getValues();
		for(int i = 0; i < newValues.length; i++)
		{
			newData[setLength] = newValues[i];
			setLength++;
		}
		String path = "/" + recordingObject.getVariable().replace(".", "/");
		String parent = path.replace("/" + name, "");
		Group datasetParent = (Group) FileFormat.findObject(recordingsH5File, parent);
		recordingObject.setDataBytes(4);
		recordingObject.setDataType(Datatype.CLASS_INTEGER);
		recordingObject.setVariable(name);
		recordingObject.setValues(newData);
		recordingObject.setValuesLenght(newData.length);
		this.createDataSet(recordingObject, datasetParent, name);
	}

	/**
	 * Updates existing dataset for a variable by extracting old values and combining them with new values before writing to file.
	 * 
	 * @param name
	 *            - Name of dataset to create
	 * @param existingValues
	 *            -Values to store in dataset
	 * @param recordingObject
	 *            - Object holding parameter info for creation
	 * @param path
	 *            - The path of the object with "." replaced by "/"
	 * @throws Exception
	 */
	private void updateDataSet(String name, float[] existingValues, RecordingObject recordingObject) throws Exception
	{
		int setLength = existingValues.length;
		float[] newData = new float[existingValues.length + recordingObject.getValuesLength()];
		for(int i = 0; i < existingValues.length; i++)
		{
			newData[i] = existingValues[i];
		}

		float[] newValues = (float[]) recordingObject.getValues();
		for(int i = 0; i < newValues.length; i++)
		{
			newData[setLength] = newValues[i];
			setLength++;
		}
		String path = "/" + recordingObject.getVariable().replace(".", "/");
		int index = path.lastIndexOf("/");
		String parent = path.substring(0, index);
		Group datasetParent = (Group) FileFormat.findObject(recordingsH5File, parent);
		recordingObject.setDataBytes(16);
		recordingObject.setDataType(Datatype.CLASS_FLOAT);
		recordingObject.setVariable(name);
		recordingObject.setValues(newData);
		recordingObject.setValuesLenght(newData.length);
		this.createDataSet(recordingObject, datasetParent, name);
	}
}