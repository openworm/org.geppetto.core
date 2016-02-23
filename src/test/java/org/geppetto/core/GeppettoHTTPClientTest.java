package org.geppetto.core;

import org.geppetto.core.common.GeppettoHTTPClient;
import org.geppetto.core.common.JSONUtility;
import org.geppetto.core.datasources.GeppettoDataSourceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeppettoHTTPClientTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testDoJSONPost() throws GeppettoDataSourceException
	{
		String response = GeppettoHTTPClient.doJSONPost("http://httpbin.org/post", "{name:'Test'}");
		Assert.assertEquals("{name:'Test'}", JSONUtility.getAsMap(response).get("data"));
	}
}
