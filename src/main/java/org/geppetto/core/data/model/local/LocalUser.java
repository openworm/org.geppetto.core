/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011 - 2015 OpenWorm.
 * http://openworm.org
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *     	OpenWorm - http://openworm.org/people.html
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/

package org.geppetto.core.data.model.local;

import java.util.List;

import org.geppetto.core.data.model.IUser;
import org.geppetto.core.data.model.IUserGroup;

public class LocalUser implements IUser
{
	private String lastLoginDate = null;

	private long id;

	private String login;

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
	public String getLastLogin() {
		return this.lastLoginDate.toString();
	}

	@Override
	public void setLastLoginDate(String date) {	
		this.lastLoginDate = date;
	}

}
