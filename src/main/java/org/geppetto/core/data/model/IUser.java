

package org.geppetto.core.data.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IUser
{

	long getId();

	String getLogin();
	
	List<Date> getLoginTimeStamps();
	
	void addLoginTimeStamp(Date date);

	@JsonIgnore
	String getPassword();

	String getName();
	
	IUserGroup getUserGroup();

	List<? extends IGeppettoProject> getGeppettoProjects();

	String getDropboxToken();

	void setDropboxToken(String token);

}
