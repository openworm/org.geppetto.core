
package org.geppetto.core.auth;

import org.geppetto.core.data.model.IUser;

/**
 * @author mattolson
 *
 */
public interface IAuthService
{

	public Boolean isAuthenticated(String sessionValue);

	public String authFailureRedirect();

	public String getSessionId();

	boolean isDefault();

	void setUser(IUser user);

	IUser getUser();

}
