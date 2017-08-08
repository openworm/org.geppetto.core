

package org.geppetto.core.data.model.local;

import java.io.Serializable;
import java.util.List;

import org.geppetto.core.data.model.IUserGroup;
import org.geppetto.core.data.model.UserPrivileges;

public class LocalUserGroup implements Serializable, IUserGroup {

	private static final long serialVersionUID = 1L;

	private long id;
	
	private String name;
	
	private List<UserPrivileges> privileges;

	private long spaceAllowance;

	private long simulationTimeAllowance;
	
	public LocalUserGroup(String name, List<UserPrivileges> privileges, long spaceAllowance, long timeAllowance)
	{
		super();
		this.name = name;
		this.privileges = privileges;
		this.spaceAllowance = spaceAllowance;
		this.simulationTimeAllowance = timeAllowance;
	}
	
	@Override
	public long getId() {
		return this.id;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public long getSpaceAllowance()
	{
		return spaceAllowance;
	}

	public void setSpaceAllowance(long spaceAllowance)
	{
		this.spaceAllowance = spaceAllowance;
	}

	@Override
	public long getSimulationTimeAllowance()
	{
		return simulationTimeAllowance;
	}

	public void setSimulationTimeAllowance(long simulationTimeAllowance)
	{
		this.simulationTimeAllowance = simulationTimeAllowance;
	}

	public List<UserPrivileges> getPrivileges() {
		return this.privileges;
	}
	
	public void setPrivileges(List<UserPrivileges> privileges) {
		this.privileges = privileges;
	}
}

