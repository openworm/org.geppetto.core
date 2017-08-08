
package org.geppetto.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.utils.SetNatives;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.HDF5Reader;
import org.junit.Test;
import org.springframework.util.Assert;

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
		H5File file=HDF5Reader.readHDF5File(new File("./src/test/resources/H5DatasetCreate.h5").toURI().toURL(),-1l);
		Assert.notNull(file);
	}
	
	@Test
	public void testExample1() throws MalformedURLException, GeppettoExecutionException
	{
		this.setup();
		H5File file=HDF5Reader.readHDF5File(new File("./src/test/resources/example1.h5").toURI().toURL(),-1l);
		Assert.notNull(file);
	}
	
	@Test
	public void testExample2() throws MalformedURLException, GeppettoExecutionException
	{
		this.setup();
		H5File file=HDF5Reader.readHDF5File(new File("./src/test/resources/example2.h5").toURI().toURL(),-1l);
		Assert.notNull(file);
	}
	
	@Test
	public void testExample3() throws MalformedURLException, GeppettoExecutionException
	{
		this.setup();
		H5File file=HDF5Reader.readHDF5File(new File("./src/test/resources/recording_small.h5").toURI().toURL(),-1l);
		Assert.notNull(file);
	}
}
