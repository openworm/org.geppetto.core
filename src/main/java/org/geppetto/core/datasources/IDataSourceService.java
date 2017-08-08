

package org.geppetto.core.datasources;

import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.IService;
import org.geppetto.model.datasources.DataSource;


/**
 * @author matteocantarelli
 * @author Rob Court
 * @author Giovanni Idili
 * 
 */
public interface IDataSourceService extends IService, IQueryProvider
{

	void fetchVariable(String variableId) throws GeppettoDataSourceException;

	void initialize(DataSource configuration, GeppettoModelAccess geppettoModelAccess);
	

}
