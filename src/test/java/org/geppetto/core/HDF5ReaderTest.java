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
package org.geppetto.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.callbacks.H5L_iterate_cb;
import ncsa.hdf.hdf5lib.callbacks.H5L_iterate_t;
import ncsa.hdf.hdf5lib.callbacks.H5O_iterate_cb;
import ncsa.hdf.hdf5lib.callbacks.H5O_iterate_t;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.H5L_info_t;
import ncsa.hdf.hdf5lib.structs.H5O_info_t;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.utils.SetNatives;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.HDF5Reader;
import org.junit.Test;
import org.springframework.util.Assert;


class opdata implements H5L_iterate_t {
	int recurs;
	opdata prev;
	long addr;
}
public class HDF5ReaderTest
{
	public void setup(){
		try {
			SetNatives.getInstance().setHDF5Native(System.getProperty("user.dir"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() throws MalformedURLException, GeppettoExecutionException
	{
		this.setup();
		H5File file=HDF5Reader.readHDF5File(new File("./src/test/resources/H5DatasetCreate.h5").toURI().toURL());
		Assert.notNull(file);		
	}
	
	@Test
	public void testExample1() throws MalformedURLException, GeppettoExecutionException
	{
		this.setup();
		 int file_id = -1;

	        try {
	            //Open file
	            file_id = H5.H5Fopen("./src/test/resources/recording_small.h5", HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);

	            //Begin iteration using H5Ovisit
	            System.out.println("Objects in the file:");
	            H5O_iterate_t iter_data = new H5O_iter_data();
	            H5O_iterate_cb iter_cb = new H5O_iter_callback();
	            H5.H5Ovisit(file_id, HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_NATIVE, iter_cb, iter_data);
	            System.out.println();
	            //Repeat the same process using H5Lvisit
	            H5L_iterate_t iter_data2 = new H5L_iter_data();
	            H5L_iterate_cb iter_cb2 = new H5L_iter_callback();
	            System.out.println ("Links in the file:");
	            H5.H5Lvisit(file_id, HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_NATIVE, iter_cb2, iter_data2);

	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        finally {
	            //Close and release resources.
	            if(file_id >= 0)
					try {
						H5.H5Fclose (file_id);
					} catch (HDF5LibraryException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        }
	}
	
//	@Test
//	public void testExample2() throws MalformedURLException, GeppettoExecutionException
//	{
//		this.setup();
//		H5File file=HDF5Reader.readHDF5File(new File("./src/test/resources/example2.h5").toURI().toURL());
//		Assert.notNull(file);
//	}
//	
//	@Test
//	public void testExample3() throws MalformedURLException, GeppettoExecutionException
//	{
//		this.setup();
//		H5File file=HDF5Reader.readHDF5File(new File("./src/test/resources/recording_small.h5").toURI().toURL());
//		Assert.notNull(file);
//	}
}



/************************************************************
 * Operator function for H5Lvisit.  This function simply
 * retrieves the info for the object the current link points
 * to, and calls the operator function for H5Ovisit.
 ************************************************************/

class idata {
    public String link_name = null;
    public int link_type = -1;
    idata(String name, int type) {
        this.link_name = name;
        this.link_type = type;
    }
}

class H5L_iter_data implements H5L_iterate_t {
    public ArrayList<idata> iterdata = new ArrayList<idata>();
}

class H5L_iter_callback implements H5L_iterate_cb {
    public int callback(int group, String name, H5L_info_t info, H5L_iterate_t op_data) {

        idata id = new idata(name, info.type);
        ((H5L_iter_data)op_data).iterdata.add(id);

        H5O_info_t infobuf;
        int ret = 0;
        try {
            //Get type of the object and display its name and type. The name of the object is passed to this function by the Library.
            infobuf = H5.H5Oget_info_by_name (group, name, HDF5Constants.H5P_DEFAULT);
            H5O_iterate_cb iter_cbO = new H5O_iter_callback();
            H5O_iterate_t iter_dataO = new H5O_iter_data();
            ret=iter_cbO.callback(group, name, infobuf, iter_dataO);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}

class H5O_iter_data implements H5O_iterate_t {
    public ArrayList<idata> iterdata = new ArrayList<idata>();
}


class H5O_iter_callback implements H5O_iterate_cb {
    public int callback(int group, String name, H5O_info_t info, H5O_iterate_t op_data) {
        idata id = new idata(name, info.type);
        ((H5O_iter_data)op_data).iterdata.add(id);

        System.out.print("/"); /* Print root group in object path */

        //Check if the current object is the root group, and if not print the full path name and type.

        if (name.charAt(0) == '.')         /* Root group, do not print '.' */
            System.out.println("  (Group)");
        else if(info.type == HDF5Constants.H5O_TYPE_GROUP ) 
            System.out.println(name + "  (Group)" );
        else if(info.type == HDF5Constants.H5O_TYPE_DATASET)
            System.out.println(name + "  (Dataset)");
        else if (info.type == HDF5Constants.H5O_TYPE_NAMED_DATATYPE )
            System.out.println(name + "  (Datatype)");
        else
            System.out.println(name + "  (Unknown)");

        return 0;
    }
}


