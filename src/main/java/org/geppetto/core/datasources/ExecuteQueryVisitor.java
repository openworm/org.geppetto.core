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
package org.geppetto.core.datasources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.geppetto.core.common.GeppettoHTTPClient;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.common.JSONUtility;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.ServiceCreator;
import org.geppetto.model.DataSource;
import org.geppetto.model.GeppettoFactory;
import org.geppetto.model.ProcessQuery;
import org.geppetto.model.Query;
import org.geppetto.model.QueryResult;
import org.geppetto.model.QueryResults;
import org.geppetto.model.SimpleQuery;
import org.geppetto.model.util.GeppettoSwitch;
import org.geppetto.model.util.GeppettoVisitingException;
import org.geppetto.model.variables.Variable;

/**
 * @author matteocantarelli
 *
 */
public class ExecuteQueryVisitor extends GeppettoSwitch<Object>
{

	private DataSource dataSource = null;

	private boolean count; // true if we want to execute a count

	private QueryResults results = null;

	private Variable variable;

	private String dataSourceTemplate;

	private GeppettoModelAccess geppettoModelAccess;

	/**
	 * 
	 */
	public ExecuteQueryVisitor(DataSource dataSource, String dataSourceTemplate, Variable variable, GeppettoModelAccess geppettoModelAccess)
	{
		this(dataSource, dataSourceTemplate, variable, geppettoModelAccess, false);
	}

	/**
	 * @param variable
	 * @param count
	 */
	public ExecuteQueryVisitor(DataSource dataSource, String dataSourceTemplate, Variable variable, GeppettoModelAccess geppettoModelAccess, boolean count)
	{
		super();
		this.dataSource = dataSource;
		this.geppettoModelAccess = geppettoModelAccess;
		this.dataSourceTemplate = dataSourceTemplate;
		this.variable = variable;
		this.count = count;
		results = GeppettoFactory.eINSTANCE.createQueryResults();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.model.util.GeppettoSwitch#caseProcessQuery(org.geppetto.model.ProcessQuery)
	 */
	@Override
	public Object caseProcessQuery(ProcessQuery query)
	{
		try
		{
			IQueryProcessor queryProcessor = (IQueryProcessor) ServiceCreator.getNewServiceInstance(query.getQueryProcessorId());
			this.results = queryProcessor.process(query, dataSource, getVariable(), getResults(), geppettoModelAccess);
		}
		catch(GeppettoInitializationException e)
		{
			return new GeppettoVisitingException(e);
		}
		catch(GeppettoDataSourceException e)
		{
			return new GeppettoVisitingException(e);
		}
		return super.caseProcessQuery(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.model.util.GeppettoSwitch#caseSimpleQuery(org.geppetto.model.SimpleQuery)
	 */
	@Override
	public Object caseSimpleQuery(SimpleQuery query)
	{
		try
		{
			if(QueryChecker.check(query, getVariable()))
			{
				String url = getDataSource(query).getUrl();

				String queryString = count ? query.getCountQuery() : query.getQuery();

				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("ID", getVariable().getId());
				properties.put("QUERY", queryString);

				String processedQueryString = VelocityUtils.processTemplate(dataSourceTemplate, properties);

				String response = GeppettoHTTPClient.doJSONPost(url, processedQueryString);
				System.out.println(response);
				processResponse(response);
			}
		}
		catch(GeppettoDataSourceException e)
		{
			return new GeppettoVisitingException(e);
		}
		return super.caseSimpleQuery(query);
	}

	/**
	 * @param response
	 */
	private void processResponse(String response)
	{
		// TODO Maybe split in two different visitors?
		if(count)
		{
			// TODO update the count
		}
		else
		{
			//TODO This is neo4j specific, factor it out

			Map<String, Object> responseMap = JSONUtility.getAsMap(response);

			List<String> headers = (List<String>) ((List) ((Map<String, Object>) ((List) responseMap.get("results")).get(0)).get("columns"));

			results.getHeader().clear();
			results.getHeader().addAll(headers);

			results.getResults().clear();
			List<Map<String, Object>> data = (List<Map<String, Object>>) ((List) ((Map<String, Object>) ((List) responseMap.get("results")).get(0)).get("data"));
			for(Map<String, Object> rowObject : data)
			{
				QueryResult resultRow = GeppettoFactory.eINSTANCE.createQueryResult();
				List<Object> row = (List<Object>) rowObject.get("row");
				for(Object value : row)
				{
					resultRow.getValues().add(value);
				}
				results.getResults().add(resultRow);
			}
		}

	}

	/**
	 * @param query
	 * @return
	 */
	private DataSource getDataSource(Query query)
	{
		EObject parent = query.eContainer();
		while(!(parent instanceof DataSource))
		{
			parent = parent.eContainer();
		}
		return (DataSource) parent;
	}

	/**
	 * @return
	 */
	private Variable getVariable()
	{
		return variable;
	}

	/**
	 * @return
	 */
	public QueryResults getResults()
	{
		return results;
	}

	public int getCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
