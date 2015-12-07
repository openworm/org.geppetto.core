package org.geppetto.core;
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

import java.io.File;
import java.io.IOException;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.utils.SetNatives;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * Test functionality to create and read hdf5 file 
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class TestHDFBindings {

	private static Log _logger = LogFactory.getLog(TestHDFBindings.class);

	static String fname  = "H5DatasetRead.h5";

	@Test
	public void createHDF5File(){

		//User directory is path to store file
		String path = System.getProperty("user.dir");

		try {
			_logger.info("Setting natives at " + path);
			SetNatives.getInstance().setHDF5Native(path);
			_logger.info("Native at " + System.getProperty("ncsa.hdf.hdf5lib.H5.hdf5lib"));


			long[] dims2D = { 20, 10 };

			// retrieve an instance of H5File
			FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

			if (fileFormat == null) {
				_logger.error("Cannot find HDF5 FileFormat.");
				return;
			}

			// create a new file with a given file name.
			H5File testFile = (H5File) fileFormat.createFile(fname, FileFormat.FILE_CREATE_DELETE);

			if (testFile == null) {
				_logger.error("Failed to create file:" + fname);
				return;
			}

			// open the file and retrieve the root group
			testFile.open();
			Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) testFile.getRootNode()).getUserObject();

			// set the data values
			int[] dataIn = new int[20 * 10];
			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < 10; j++) {
					dataIn[i * 10 + j] = 1000 + i * 100 + j;
				}
			}

			// create 2D 32-bit (4 bytes) integer dataset of 20 by 10
			Datatype dtype = testFile.createDatatype(Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE);
			Dataset dataset = testFile
					.createScalarDS("2D 32-bit integer 20x10", root, dtype, dims2D, null, null, 0, dataIn);

			// close file resource
			testFile.close();

			org.junit.Assert.assertEquals(testFile.exists(), true);
			
			readHDF5File();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readHDF5File() throws Exception {
		// retrieve an instance of H5File
        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null) {
            _logger.error("Cannot find HDF5 FileFormat.");
            return;
        }

        // open the file with read and write access
        FileFormat testFile = fileFormat.createInstance(fname, FileFormat.WRITE);

        org.junit.Assert.assertNotNull(testFile);

        if (testFile == null) {
           _logger.error("Failed to open file: " + fname);
            return;
        }

        org.junit.Assert.assertEquals(testFile.exists(), true);

        // open the file and retrieve the file structure
        testFile.open();
        
        Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) testFile.getRootNode()).getUserObject();

        // retrieve the dataset "2D 32-bit integer 20x10"
        Dataset dataset = (Dataset) root.getMemberList().get(0);
        int[] dataRead = (int[]) dataset.read();

        org.junit.Assert.assertEquals(dataRead.length, 200);
        
        org.junit.Assert.assertEquals(dataRead[0], 1000);
        org.junit.Assert.assertEquals(dataRead[1], 1001);

        // change data value and write it to file.
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                dataRead[i * 10 + j]++;
            }
        }
        dataset.write(dataRead);

        // clean and reload the data value
        int[] dataModified = (int[]) dataset.read();

        org.junit.Assert.assertEquals(dataModified.length, 200);
        org.junit.Assert.assertEquals(dataModified[0], 1001);
        org.junit.Assert.assertEquals(dataModified[1], 1002);

        // close file resource
        testFile.close();
	}
	
	@AfterClass
    public static void teardown() throws Exception {
		File sampleFile = new File(System.getProperty("user.dir")+"/"+fname);
		if(sampleFile.exists()){
			sampleFile.delete();
			_logger.info("Deleting datasets h5");
		}
    } 
}
