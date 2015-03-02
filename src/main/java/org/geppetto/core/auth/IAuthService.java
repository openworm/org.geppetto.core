package org.geppetto.core.auth;


public interface IAuthService {

    public Boolean isAuthenticated();

    public String authFailureRedirect();
    
    boolean isDefault();

}
