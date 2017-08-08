
package org.geppetto.core.auth;

import org.geppetto.core.data.model.IUser;

public class DefaultAuthService implements IAuthService
{

	@Override
	public String authFailureRedirect()
	{
		return "";
	}

	@Override
	public boolean isDefault()
	{
		return true;
	}

	@Override
	public Boolean isAuthenticated(String sessionValue)
	{
		return true;
	}

	@Override
	public String getSessionId()
	{
		return null;
	}

	@Override
	public void setUser(IUser user)
	{
	}

	@Override
	public IUser getUser()
	{
		return null;
	}

}
