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

import java.util.List;

import org.geppetto.model.datasources.Query;
import org.geppetto.model.datasources.QueryResults;
import org.geppetto.model.variables.Variable;

/**
 * @author matteocantarelli
 *
 */
public interface IQueryProvider
{

	List<Query> getAvailableQueries(Variable variable) throws GeppettoDataSourceException;

	int getNumberOfResults(Query query, Variable variable) throws GeppettoDataSourceException;

	int getNumberOfResults(Query query, Variable variable, QueryResults results) throws GeppettoDataSourceException;

	/**
	 * This is an asynchronous method that will initiate the execution of a query
	 * 
	 * @param query
	 * @param variable
	 * @param listener
	 *            the listener that will be notified as results become available
	 * @return a container for the results. The container has an id, there's no constraints for all the results to be inside IQueryResults as the DataSource might keep pushing results to it as the
	 *         query is execute
	 */
	QueryResults execute(Query query, Variable variable, IQueryListener listener) throws GeppettoDataSourceException;

	/**
	 * This method has the extra parameter "results" in case this query will need to output of a previously executed query in order to have all the necessary input data
	 * 
	 * @param query
	 * @param variable
	 * @param results
	 * @param listener
	 * @return
	 */
	QueryResults execute(Query query, Variable variable, QueryResults results, IQueryListener listener) throws GeppettoDataSourceException;

}
