
package org.geppetto.core.datasources;

import java.util.List;

import org.geppetto.model.datasources.Query;
import org.geppetto.model.datasources.QueryResults;
import org.geppetto.model.datasources.RunnableQuery;
import org.geppetto.model.variables.Variable;

/**
 * @author matteocantarelli
 *
 */
public interface IQueryProvider
{

	/**
	 * @param variable
	 * @return
	 * @throws GeppettoDataSourceException
	 */
	List<Query> getAvailableQueries(Variable variable) throws GeppettoDataSourceException;
	
	/**
	 * @param queries
	 * @return
	 * @throws GeppettoDataSourceException 
	 */
	int getNumberOfResults(List<RunnableQuery> queries) throws GeppettoDataSourceException;
	
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
	QueryResults execute(List<RunnableQuery> queries) throws GeppettoDataSourceException;
	



}
