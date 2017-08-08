
package org.geppetto.core.datasources;

import org.geppetto.model.datasources.QueryResults;



/**
 * @author matteocantarelli
 *
 */
public interface IQueryListener
{

	void appendResults(QueryResults results);
	
	void done(QueryResults results);
	
}
