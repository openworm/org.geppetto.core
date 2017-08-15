
package org.geppetto.core;

import junit.framework.Assert;

import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.model.GeppettoModelReader;
import org.geppetto.model.GeppettoModel;
import org.junit.Test;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoModelReaderTest
{

	@Test
	public void testReader() throws GeppettoInitializationException
	{
		GeppettoModel model = GeppettoModelReader.readGeppettoModel(GeppettoModelReaderTest.class.getClassLoader().getResource("GeppettoModelTestDifferentSchema.xmi"));
		Assert.assertEquals("acnet2", model.getVariables().get(0).getName());
	}

}
