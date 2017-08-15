

package org.geppetto.core.manager;

import java.util.List;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.datasources.GeppettoDataSourceException;
import org.geppetto.model.GeppettoModel;
import org.geppetto.model.datasources.QueryResults;
import org.geppetto.model.datasources.RunnableQuery;
import org.geppetto.model.util.GeppettoModelException;

/**
 * @author matteocantarelli
 * @author Rob Court
 * @author Giovanni Idili
 * 
 */
public interface IDataSourceManager
{

	GeppettoModel fetchVariable(String dataSourceId, String variableId, IGeppettoProject project) throws GeppettoDataSourceException, GeppettoModelException, GeppettoExecutionException;

	QueryResults runQuery(List<RunnableQuery> queries, IGeppettoProject project) throws GeppettoDataSourceException, GeppettoModelException, GeppettoExecutionException;

	int runQueryCount(List<RunnableQuery> queries, IGeppettoProject project) throws GeppettoDataSourceException, GeppettoModelException, GeppettoExecutionException;

}
