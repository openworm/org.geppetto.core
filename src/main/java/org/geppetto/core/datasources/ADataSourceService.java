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

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.AService;
import org.geppetto.model.DataSource;
import org.geppetto.model.Query;
import org.geppetto.model.types.Type;
import org.geppetto.model.util.ModelUtility;
import org.geppetto.model.variables.Variable;

/**
 * @author matteocantarelli
 *
 */
public abstract class ADataSourceService extends AService implements IDataSourceService
{

	private DataSource configuration;

	private String dataSourceTemplate;

	private GeppettoModelAccess geppettoModelAccess;

	public ADataSourceService(String dataSourceTemplate)
	{
		this.dataSourceTemplate = dataSourceTemplate;
	}

	@Override
	public void initialize(DataSource configuration, GeppettoModelAccess geppettoModelAccess)
	{
		this.configuration = configuration;
		this.geppettoModelAccess = geppettoModelAccess;

	}
	
	protected GeppettoModelAccess getGeppettoModelAccess()
	{
		return geppettoModelAccess;
	}

	protected String getTemplate()
	{
		return dataSourceTemplate;
	}

	/**
	 * @return the configuration for this DataSourceService
	 */
	protected DataSource getConfiguration()
	{
		return configuration;
	}

	/**
	 * @param url
	 * @param queryString
	 * @return
	 */
	protected String getQueryURL(String url, String queryString)
	{
		String queryURL = null;
		// TODO Do we need a template? Plain concatenation?
		return queryURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.model.QueryProvider#getAvailableQueries(org.geppetto.model.variables.Variable)
	 */
	@Override
	public List<Query> getAvailableQueries(Variable variable)
	{
		List<Query> availableQueries = new ArrayList<Query>();
		List<Type> types = ModelUtility.getAllTypesOf(variable);
		for(Query query : configuration.getQueries())
		{
			if(QueryChecker.check(query, types))
			{
				availableQueries.add(query);
			}
		}
		return availableQueries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.services.IService#registerGeppettoService()
	 */
	@Override
	public void registerGeppettoService() throws Exception
	{
		// Nothing to do here
	}

}
