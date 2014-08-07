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
package org.geppetto.core;

import java.io.File;
import java.net.MalformedURLException;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.HDF5Reader;
import org.junit.Test;
import org.springframework.util.Assert;

import ucar.nc2.NetcdfFile;

public class HDF5ReaderTest
{

	@Test
	public void test() throws MalformedURLException, GeppettoExecutionException
	{
		NetcdfFile file=HDF5Reader.readHDF5File(new File("./src/test/resources/H5DatasetCreate.h5").toURI().toURL());
		Assert.notNull(file);
	}
	
	@Test
	public void testExample1() throws MalformedURLException, GeppettoExecutionException
	{
		NetcdfFile file=HDF5Reader.readHDF5File(new File("./src/test/resources/example1.h5").toURI().toURL());
		Assert.notNull(file);
	}
	
	@Test
	public void testExample2() throws MalformedURLException, GeppettoExecutionException
	{
		NetcdfFile file=HDF5Reader.readHDF5File(new File("./src/test/resources/example2.h5").toURI().toURL());
		Assert.notNull(file);
	}

}
