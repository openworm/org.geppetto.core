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
