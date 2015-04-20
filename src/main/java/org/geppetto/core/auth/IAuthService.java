package org.geppetto.core.auth;

/**
 * @author mattolson
 *
 */
public interface IAuthService
{

	public Boolean isAuthenticated();

	public String authFailureRedirect();

}
