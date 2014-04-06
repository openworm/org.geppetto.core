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
