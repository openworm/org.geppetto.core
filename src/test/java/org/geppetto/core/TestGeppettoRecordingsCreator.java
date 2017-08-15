
package org.geppetto.core;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import junit.framework.Assert;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.h5.H5File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.recordings.GeppettoRecordingCreator;
import org.geppetto.model.types.TypesPackage;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * Creates GeppettoRecordings Class
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class TestGeppettoRecordingsCreator
{

	private static Log _logger = LogFactory.getLog(TestGeppettoRecordingsCreator.class);

	@Test
	public void addInitialVariable() throws Exception
	{

		GeppettoRecordingCreator creator = new GeppettoRecordingCreator("sample.h5");
		creator.addValues("P.J", 2.0d, "ms", TypesPackage.Literals.STATE_VARIABLE_TYPE.getName(), false);
		creator.addValues("P.J", 8.0d, "ms", TypesPackage.Literals.STATE_VARIABLE_TYPE.getName(), false);
		creator.create();
		assertNotNull(creator.getRecordingsFile());

		H5File file = creator.getRecordingsFile();
		file.open();
		Dataset dataset = (Dataset) file.findObject(file, "/P/J");
		double[] value = (double[]) dataset.read();
		Assert.assertEquals(8.0d, value[0]);

		List metaData = dataset.getMetadata();
		Attribute unit = (Attribute) metaData.get(1);
		_logger.info("Attribute " + unit.getName() + " : " + ((String[]) unit.getValue())[0]);
		Assert.assertEquals("ms", ((String[]) unit.getValue())[0]);

		Attribute type = (Attribute) metaData.get(0);
		_logger.info("Attribute " + type.getName() + " : " + ((String[]) type.getValue())[0]);
		Assert.assertEquals(TypesPackage.Literals.STATE_VARIABLE_TYPE.getName(), ((String[]) type.getValue())[0]);

		file.close();

	}

	@Test
	public void addSibling() throws Exception
	{

		Double[] values = { 0.03d, 0.055d, 0.100d };
		GeppettoRecordingCreator creator = new GeppettoRecordingCreator("sample.h5");
		creator.addValues("P.neuron", values, "ms", TypesPackage.Literals.STATE_VARIABLE_TYPE.getName(), false);
		creator.create();
		assertNotNull(creator.getRecordingsFile());

		H5File file = creator.getRecordingsFile();
		file.open();
		Dataset dataset = (Dataset) file.findObject(file, "/P/neuron");
		double[] value = (double[]) dataset.read();
		Assert.assertEquals(0.1d, value[2]);

		List metaData = dataset.getMetadata();
		Attribute unit = (Attribute) metaData.get(1);
		_logger.info("Attribute " + unit.getName() + " : " + ((String[]) unit.getValue())[0]);
		Assert.assertEquals("ms", ((String[]) unit.getValue())[0]);

		Attribute type = (Attribute) metaData.get(0);
		_logger.info("Attribute " + type.getName() + " : " + ((String[]) type.getValue())[0]);
		Assert.assertEquals(TypesPackage.Literals.STATE_VARIABLE_TYPE.getName(), ((String[]) type.getValue())[0]);

		file.close();

	}

	@Test
	public void addSiblingSameName()
	{
		try
		{
			GeppettoRecordingCreator creator = new GeppettoRecordingCreator("sample.h5");
			creator.addValues("P.j", 3.0d, "ms", TypesPackage.Literals.STATE_VARIABLE_TYPE.getName(), false);
			creator.create();
			assertNotNull(creator.getRecordingsFile());

			H5File file = creator.getRecordingsFile();
			file.open();
			Dataset dataset = (Dataset) file.findObject(file, "/P/j");
			double[] value = (double[]) dataset.read();
			Assert.assertEquals(3.0d, value[0]);

			List metaData = dataset.getMetadata();
			Attribute unit = (Attribute) metaData.get(1);
			_logger.info("Attribute " + unit.getName() + " : " + ((String[]) unit.getValue())[0]);
			Assert.assertEquals("ms", ((String[]) unit.getValue())[0]);

			Attribute type = (Attribute) metaData.get(0);
			_logger.info("Attribute " + type.getName() + " : " + ((String[]) type.getValue())[0]);
			Assert.assertEquals(TypesPackage.Literals.STATE_VARIABLE_TYPE.getName(), ((String[]) type.getValue())[0]);

			file.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void addGrandChildren() throws Exception
	{

		GeppettoRecordingCreator creator = new GeppettoRecordingCreator("sample.h5");
		creator.addValues("P.b.aa", 3.0d, "ms", TypesPackage.Literals.STATE_VARIABLE_TYPE.getName(), false);
		creator.create();
		assertNotNull(creator.getRecordingsFile());

		H5File file = creator.getRecordingsFile();
		file.open();
		Dataset dataset = (Dataset) file.findObject(file, "/P/b/aa");
		double[] value = (double[]) dataset.read();
		Assert.assertEquals(3.0d, value[0]);

		List metaData = dataset.getMetadata();
		Attribute unit = (Attribute) metaData.get(1);
		_logger.info("Attribute " + unit.getName() + " : " + ((String[]) unit.getValue())[0]);
		Assert.assertEquals("ms", ((String[]) unit.getValue())[0]);

		Attribute type = (Attribute) metaData.get(0);
		_logger.info("Attribute " + type.getName() + " : " + ((String[]) type.getValue())[0]);
		Assert.assertEquals(TypesPackage.Literals.STATE_VARIABLE_TYPE.getName(), ((String[]) type.getValue())[0]);

		file.close();

	}

	@Test
	public void addSingleInteger() throws Exception
	{

		GeppettoRecordingCreator creator = new GeppettoRecordingCreator("sample.h5");
		creator.addValues("P.b.c", 3, "ms", TypesPackage.Literals.PARAMETER_TYPE.getName(), false);
		creator.addValues("P.b.c", 33, "ms", TypesPackage.Literals.PARAMETER_TYPE.getName(), false);

		creator.create();
		assertNotNull(creator.getRecordingsFile());

		H5File file = creator.getRecordingsFile();
		file.open();
		Dataset dataset = (Dataset) file.findObject(file, "/P/b/c");
		int[] value = (int[]) dataset.read();
		Assert.assertEquals(33, value[0]);
		List metaData = dataset.getMetadata();
		Attribute unit = (Attribute) metaData.get(1);
		_logger.info("Attribute " + unit.getName() + " : " + ((String[]) unit.getValue())[0]);
		Assert.assertEquals("ms", ((String[]) unit.getValue())[0]);

		Attribute type = (Attribute) metaData.get(0);
		_logger.info("Attribute " + type.getName() + " : " + ((String[]) type.getValue())[0]);
		Assert.assertEquals(TypesPackage.Literals.PARAMETER_TYPE.getName(), ((String[]) type.getValue())[0]);

		file.close();

	}

	@Test
	public void addSetInteger() throws Exception
	{

		int[] values = { 20, 30, 40 };
		GeppettoRecordingCreator creator = new GeppettoRecordingCreator("sample.h5");
		creator.addValues("P.b.a", values, "ms", TypesPackage.Literals.PARAMETER_TYPE.getName(), false);
		creator.create();
		assertNotNull(creator.getRecordingsFile());

		H5File file = creator.getRecordingsFile();
		file.open();
		Dataset dataset = (Dataset) file.findObject(file, "/P/b/a");
		int[] value = (int[]) dataset.read();
		Assert.assertEquals(30, value[1]);
		List metaData = dataset.getMetadata();
		Attribute unit = (Attribute) metaData.get(1);
		_logger.info("Attribute " + unit.getName() + " : " + ((String[]) unit.getValue())[0]);
		Assert.assertEquals("ms", ((String[]) unit.getValue())[0]);

		Attribute type = (Attribute) metaData.get(0);
		_logger.info("Attribute " + type.getName() + " : " + ((String[]) type.getValue())[0]);
		Assert.assertEquals(TypesPackage.Literals.PARAMETER_TYPE.getName(), ((String[]) type.getValue())[0]);

		file.close();

	}

	@AfterClass
	public static void teardown() throws Exception
	{
		File sampleFile = new File(System.getProperty("user.dir") + "/sample.h5");
		if(sampleFile.exists())
		{
			sampleFile.delete();
			_logger.info("Deleting sample h5");
		}
	}
}
