
package org.geppetto.core.beans;

import java.util.List;

/**
 * @author Jesus Martinez (jesus@metacell.us)
 *
 */
public class LocalUserConfig
{

	private List<String> guestUserPermissions;

	public List<String> getGuestUserPermissions()
	{
		return this.guestUserPermissions;
	}

	public void setGuestUserPermissions(List<String> guestUserPermissions)
	{
		this.guestUserPermissions = guestUserPermissions;
	}
}
