
package org.geppetto.core.common;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.geppetto.core.datasources.GeppettoDataSourceException;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoHTTPClient
{

	public static String doJSONPost(String url, String postContent) throws GeppettoDataSourceException
	{
		String postResult = null;
		try
		{
			// execute query and handle any error responses.
			HttpPost postRequest = new HttpPost(url);
			StringEntity postEntity = new StringEntity(postContent);
			postEntity.setContentType("application/json");
			postRequest.setEntity(postEntity);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse response = httpClient.execute(postRequest);
			InputStream inputStream = response.getEntity().getContent();
			postResult = GeppettoCommonUtils.readString(inputStream);
		}
		catch(IOException e)
		{
			throw new GeppettoDataSourceException(e);
		}

		return postResult;
	}

	public static String doGET(String url, String getParameters) throws GeppettoDataSourceException
	{
		String getResult = null;
		try
		{
			// execute query and handle any error responses.
			String parameters = "";
			if(getParameters != null && !getParameters.isEmpty())
			{
				parameters = "?" + getParameters;
			}
			HttpGet getRequest = new HttpGet(url + parameters);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse response = httpClient.execute(getRequest);
			InputStream inputStream = response.getEntity().getContent();
			getResult = GeppettoCommonUtils.readString(inputStream);
		}
		catch(IOException e)
		{
			throw new GeppettoDataSourceException(e);
		}

		return getResult;
	}
}
