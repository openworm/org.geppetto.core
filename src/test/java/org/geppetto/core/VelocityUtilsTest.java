
package org.geppetto.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.geppetto.core.common.GeppettoCommonUtils;
import org.geppetto.core.datasources.GeppettoDataSourceException;
import org.geppetto.core.datasources.VelocityUtils;
import org.geppetto.model.datasources.DatasourcesFactory;
import org.geppetto.model.datasources.SimpleQuery;
import org.junit.Before;
import org.junit.Test;

/**
 * @author matteocantarelli
 *
 */
public class VelocityUtilsTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * Test method for {@link org.geppetto.core.datasources.VelocityUtils#processTemplate(java.lang.String, java.util.Map)}.
	 * 
	 * @throws GeppettoDataSourceException
	 * @throws IOException
	 */
	@Test
	public void testProcessTemplate() throws GeppettoDataSourceException, IOException
	{
		SimpleQuery query = DatasourcesFactory.eINSTANCE.createSimpleQuery();
		query.setQuery("MATCH (n:Class)<-[:SUBCLASSOF*]-(:Class)<-[:INSTANCEOF]-(i) WHERE n.short_form='$ID' RETURN n AS columnName limit 5;");
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("ID", "FBbt_00100219");
		properties.put("QUERY", query.getQuery());
		String queryString = VelocityUtils.processTemplate("/velocityTemplate/testTemplate.vm", properties);
		InputStream in = VelocityUtilsTest.class.getClassLoader().getResourceAsStream("expectedQueryTemplateResultTest.json");
		String expected = GeppettoCommonUtils.readString(in);
		Assert.assertEquals(expected, queryString);
	}

}
