

package org.geppetto.core.data.model.local;

import java.util.Date;
import java.util.List;

import org.geppetto.core.data.model.IUser;
import org.geppetto.core.data.model.IUserGroup;

public class LocalUser implements IUser
{
	private String lastLoginDate = null;

	private long id;

	private String login;
	
	private int loginCount;

	private String name;

	private String password;

	private String token;

	private List<LocalGeppettoProject> geppettoProjects;
	
	private IUserGroup group;

	private String dropboxToken;

	public LocalUser(long id, String login, String password, String name, String token, List<LocalGeppettoProject> geppettoProjects, IUserGroup group)
	{
		this.id = id;
		this.login = login;
		this.password = password;
		this.name = name;
		this.token = token;
		this.geppettoProjects = geppettoProjects;
		this.group = group;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public String getLogin()
	{
		return login;
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public String getToken()
	{
		return token;
	}

	@Override
	public List<LocalGeppettoProject> getGeppettoProjects()
	{
		return geppettoProjects;
	}

	@Override
	public String getDropboxToken()
	{
		return this.dropboxToken;
	}

	@Override
	public void setDropboxToken(String token)
	{
		this.dropboxToken = token;
	}

	@Override
	public IUserGroup getUserGroup() {
		return this.group;
	}
	
	public void setUserGroup(IUserGroup group){
		this.group = group;
	}

	@Override
	public List<Date> getLoginTimeStamps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLoginTimeStamp(Date date) {
		// TODO Auto-generated method stub
		
	}

}
