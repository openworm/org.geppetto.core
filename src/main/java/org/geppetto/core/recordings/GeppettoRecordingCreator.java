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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoExecutionException;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;

/**
 * Creates HDF5 Recordings file. API can take different 
 * array datasets and single values for integers, doubles and 
 * floats.
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class GeppettoRecordingCreator {

	private static Log _logger = LogFactory.getLog(GeppettoRecordingCreator.class);

	private String _fileName;
	private H5File recordingsH5File;
	public enum MetaType{
		Variable_Node("VariableNode"), Parameter_Node("ParameterNode");

		private String type; 
		private MetaType(String type) { 
			this.type = type; 
		} 

		@Override 
		public String toString() {
			return type;
		}
	};

	public GeppettoRecordingCreator(String name) throws Exception{
		_fileName=name;
		createHDF5File();
	}

	/**
	 * Returns H5File that was created for this recording
	 * @return - HDF5 recordings file
	 */
	public H5File getRecordingsFile(){
		return this.recordingsH5File;
	}

	/**
	 * Returns name of recordings file
	 * @return - Recordings file name
	 */
	public String getName() {
		return _fileName;
	}

	/**
	 * Get root of recordings file
	 * @return
	 * @throws GeppettoExecutionException
	 */
	private Group getRoot() throws GeppettoExecutionException{
		return 
				(Group) ((javax.swing.tree.DefaultMutableTreeNode) 
						recordingsH5File.getRootNode()).getUserObject();
	}

	/**
	 * Create new HDF5File for recordings, or retrieve existing one with same
	 * name.
	 * @throws Exception
	 */
	private void createHDF5File() throws Exception {
		// retrieve an instance of H5File
		FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

		if (fileFormat == null) {
			_logger.error("Cannot find HDF5 FileFormat.");
			throw new GeppettoExecutionException("Cannot find HDF5 FileFormat.");
		}

		//Create instance pointing to file name
		recordingsH5File = 
				(H5File) fileFormat.createInstance(_fileName, FileFormat.WRITE);

		//check if file already exists, don't create if it does
		if(!recordingsH5File.exists()){
			try {
				recordingsH5File = 
						(H5File) fileFormat.createFile(_fileName,
								FileFormat.FILE_CREATE_DELETE);
			} catch (Exception e) {
				throw new GeppettoExecutionException(e);
			}
		}
	}

	/**
	 * Adds attributes to dataset.
	 * @param type - Metatype of dataset (VariableNode or ParameterNode)
	 * @param unit - Unit of measurement for datasets
	 * @param dataset - Dataset that will be parent to attributes
	 * @throws HDF5Exception
	 */
	private void createaAttributes(String type, String unit, Dataset dataset) throws HDF5Exception{
		//One piece of information per attribute, e.g. one for 
		//unit and one for metatype
		long[] attrDims = {1};
		//create attribute holding metatype (parameternode or variablenode)
		if(!(unit.equals(""))|| unit!=null){
			String[] classValue = {type};
			Datatype attrType =new H5Datatype(Datatype.CLASS_STRING,classValue[0].length()+1, -1, -1);
			Attribute attr = new Attribute("MetaType", attrType,attrDims);
			attr.setValue(classValue);
			recordingsH5File.writeAttribute(dataset, attr, false);
		}

		//create attribute holding unit of measurement
		if(!(unit.equals(""))|| unit!=null){
			String[] classValue2 = {unit};
			Datatype attrType2 =new H5Datatype(Datatype.CLASS_STRING,classValue2[0].length()+1, -1, -1);
			Attribute attr2 = new Attribute("Unit", attrType2,attrDims);
			attr2.setValue(classValue2);
			recordingsH5File.writeAttribute(dataset, attr2, false);
		}
	}

	/**
	 * Calls overloaded method to add values to file. Passes it parameter that it was
	 * passed, plus a Datatype that will tell global method what kind of data it
	 * will be storing
	 * 
	 * @param variable - Name of Variable to create
	 * @param values - Values of data set to store
	 * @param unit - Unit of measurement for data
	 * @param metaType - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable, double value, String unit, MetaType metaType) throws Exception{
		//Convert single number into array, to store as dataset
		double[] values = new double[1];
		values[0] = value;
		addValues(variable, values, unit,metaType);
	}

	/**
	 * Calls overloaded method to add values to file. Passes it parameter that it was
	 * passed, plus a Datatype that will tell global method what kind of data it
	 * will be storing
	 * 
	 * @param variable - Name of Variable to create
	 * @param values - Values of data set to store
	 * @param unit - Unit of measurement for data
	 * @param metaType - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable, int value, String unit, MetaType metaType) throws Exception{
		//Convert single number into array, to store as dataset
		int[] values = new int[1];
		values[0] = value;
		addValues(variable, values, unit,metaType);
	}

	/**
	 * Calls overloaded method to add values to file. Passes it parameter that it was
	 * passed, plus a Datatype that will tell global method what kind of data it
	 * will be storing
	 * 
	 * @param variable - Name of Variable to create
	 * @param values - Values of data set to store
	 * @param unit - Unit of measurement for data
	 * @param metaType - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable, float value, String unit, MetaType metaType) throws Exception{
		//Convert single number into array, to store as dataset
		float[] values = new float[1];
		values[0] = value;
		addValues(variable, values, unit,metaType);
	}

	/**
	 * Calls super method to add values to file. Passes it parameter that it was
	 * passed, plus a Datatype that will tell global method what kind of data it
	 * will be storing
	 * 
	 * @param variable - Name of Variable to create
	 * @param values - Values of data set to store
	 * @param unit - Unit of measurement for data
	 * @param metaType - Type of node, either variable or parameter node
	 * @throws Exception
	 * 
	 */
	public void addValues(String variable,double[] values, String unit,MetaType metaType) throws Exception{
		//we will be storing floats as our data type
		Datatype dataType = recordingsH5File.createDatatype(Datatype.CLASS_FLOAT, 8, 
				Datatype.NATIVE, Datatype.NATIVE);
		superAddValues(variable, values, values.length, unit, dataType,metaType);
	}

	/**
	 * Calls super method to add values to file. Passes it parameter that it was
	 * passed, plus a Datatype that will tell global method what kind of data it
	 * will be storing
	 * 
	 * @param variable - Name of Variable to create
	 * @param values - Values of data set to store
	 * @param unit - Unit of measurement for data
	 * @param metaType - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable, int[] values, String unit, MetaType metaType) throws Exception{
		//we will be storing integers as our data type
		Datatype dataType = recordingsH5File.createDatatype(Datatype.CLASS_INTEGER, 4, 
				Datatype.NATIVE, Datatype.NATIVE);
		superAddValues(variable, values, values.length, unit, dataType,metaType);
	}

	/**
	 * Calls super method to add values to file. Passes it parameter that it was
	 * passed, plus a Datatype that will tell global method what kind of data it
	 * will be storing
	 * 
	 * @param variable - Name of Variable to create
	 * @param values - Values of data set to store
	 * @param unit - Unit of measurement for data
	 * @param metaType - Type of node, either variable or parameter node
	 * @throws Exception
	 */
	public void addValues(String variable,float[] values, String unit, MetaType metaType) throws Exception{
		//we will be storing floats as our data type
		Datatype dataType = recordingsH5File.createDatatype(Datatype.CLASS_FLOAT, 8, 
				Datatype.NATIVE, Datatype.NATIVE);
		superAddValues(variable, values, values.length, unit, dataType,metaType);
	}

	/**
	 * General method for adding datasets to an HDF5 File. The parameter dataType
	 * it's what makes the data stored in the dataset different from others; it could
	 * be an array or single number of integers, doubles or floats.
	 * 
	 * @param variable - Name of variabe to create
	 * @param values - Values in data set to store
	 * @param valuesLength - Length of data set a.k.a length of array
	 * @param unit - Unit of measurement for data in array
	 * @param dataType - Datatype to differentiate data store in hdf5file 
	 * @param metaType - Attribute that stores whether this is a 
	 * 					 variable or parameter node
	 * @throws Exception
	 */
	public void superAddValues(String variable, Object values, int valuesLength, 
			String unit,Datatype dataType, MetaType metaType) throws Exception{
		//open file, needs to do it to write or read from it
		recordingsH5File.open();
		//Get group root of file, this should contain all groups
		Group root = getRoot();
		//Create path by replacing . in desired variable name by /
		String path = "/"+variable.replace(".", "/");
		//Try to find variable in file
		HObject v = FileFormat.findObject(recordingsH5File, path);
		//Variable not found, let's create it
		if(v == null){
			String[] splitByPeriod = variable.split("\\.");

			/**
			 * Split variable name by the . and use each string 
			 * to create a group from it and attach it to parent group, 
			 * which at start is the root. If group is found, retrieves it
			 * and keeps checking the rest of the path.
			 * Once all groups have been created for the path, create the dataset
			 * and attach it to last group created. 
			 */
			Group current = root;
			String currentTag = variable;
			String currentPath = ""; 
			for(int s =0; s<splitByPeriod.length-1; s++){
				currentTag =splitByPeriod[s];
				currentPath = currentPath.concat("/"+currentTag);
				//try to find if group already exists
				HObject findObject = FileFormat.findObject(recordingsH5File, currentPath);
				if(findObject==null){
					Group newGroup = recordingsH5File.createGroup(currentTag, current);
					current.addToMemberList(newGroup);
					current = newGroup;
				}else{
					current = (Group) findObject;
				}
			}
			//last part of path will be dataset
			//e.g. If variable given was P.J then P is group object, while
			//J is a dataset
			currentTag = splitByPeriod[splitByPeriod.length-1];

			//dimension of dataset, length of array and 1 column 
			long[] dims2D = { valuesLength,1 };

			// create 1D 32-bit float dataset
			Dataset dataset = recordingsH5File.createScalarDS(currentTag, current, dataType, 
					dims2D, null, null, 0, values);

			//add attributes for unit and metatype
			createaAttributes(metaType.toString(), unit, dataset);
		}
		else{
			_logger.warn("File already contains variable " + variable);
		}

		recordingsH5File.close();
	}
}