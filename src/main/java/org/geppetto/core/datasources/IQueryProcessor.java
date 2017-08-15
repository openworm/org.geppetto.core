
package org.geppetto.core.datasources;

import java.util.Map;

import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.IService;
import org.geppetto.model.datasources.DataSource;
import org.geppetto.model.datasources.ProcessQuery;
import org.geppetto.model.datasources.QueryResults;
import org.geppetto.model.variables.Variable;

/**
 * @author matteocantarelli
 *
 */
public interface IQueryProcessor extends IService
{
	
	/**
	 * This is an asynchronous method that will initiate processing of a query
	 * This method has the extra parameter "results" in case this query will need to output of a previously executed query in order to have all the necessary input data
	 * 
	 * @param query
	 * @param variable
	 * @param listener
	 *            the listener that will be notified as results become available
	 * @return a container for the results. The container has an id, there's no constraints for all the results to be inside IQueryResults as the DataSource might keep pushing results to it as the
	 *         query is execute
	 */
	QueryResults process(ProcessQuery query, DataSource dataSource, Variable variable, QueryResults results, GeppettoModelAccess geppettoModelAccess) throws GeppettoDataSourceException;


	/**
	 * @return
	 */
	Map<String, Object> getProcessingOutputMap();
}
