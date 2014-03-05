package org.geppetto.core;

import java.io.File;
import java.net.MalformedURLException;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.HDF5Reader;
import org.junit.Test;
import org.springframework.util.Assert;

import ucar.nc2.Group;

public class HDF5ReaderTest
{

	@Test
	public void test() throws MalformedURLException, GeppettoExecutionException
	{
		Group group=HDF5Reader.readHDF5File(new File("./src/test/resources/H5DatasetCreate.h5").toURI().toURL());
		Assert.notNull(group);
	}

}
