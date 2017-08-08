

package org.geppetto.core.data.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IUserGroup {
	
	long getId();
	
	String getName();
	
	List<UserPrivileges> getPrivileges();
	
	@JsonIgnore
	long getSpaceAllowance();
	
	@JsonIgnore
	long getSimulationTimeAllowance();
}
