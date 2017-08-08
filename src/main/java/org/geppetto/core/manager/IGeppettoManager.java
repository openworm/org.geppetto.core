
package org.geppetto.core.manager;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IUser;
import org.geppetto.core.simulation.IGeppettoManagerCallbackListener;

/**
 * @author matteocantarelli
 *
 */
public interface IGeppettoManager extends IProjectManager, IExperimentManager, IDropBoxManager, IRuntimeTreeManager, IDownloadManager, IDataSourceManager
{

	/**
	 * FIXME: Move to IAuthService?
	 * 
	 * @return
	 */
	IUser getUser();

	/**
	 * FIXME: Move to IAuthService?
	 * 
	 * @param user
	 * @throws GeppettoExecutionException
	 */
	void setUser(IUser user) throws GeppettoExecutionException;

	/**
	 * @return whether this geppetto manager has a connection or a run scope
	 */
	Scope getScope();

	void setSimulationListener(IGeppettoManagerCallbackListener listener);

}
