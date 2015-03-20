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

public class LocalUser implements IUser
{
	private long id;

	private String login;

	private String name;
	
	private String token;

	private long spaceAllowance;

	private long simulationTimeAllowance;

	private List<LocalGeppettoProject> geppettoProjects;

	public LocalUser(long id, String login, String name, String token, List<LocalGeppettoProject> geppettoProjects, long spaceAllowance, long simulationTimeAllowance)
	{
		this.id = id;
		this.login = login;
		this.name = name;
		this.token = token;
		this.geppettoProjects = geppettoProjects;
		this.spaceAllowance = spaceAllowance;
		this.simulationTimeAllowance = simulationTimeAllowance;
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
	public String getName()
	{
		return name;
	}
	
	public String getToken() {
		return token;
	}

	@Override
	public List<LocalGeppettoProject> getGeppettoProjects()
	{
		return geppettoProjects;
	}

	@Override
	public long getSpaceAllowance()
	{
		return spaceAllowance;
	}

	@Override
	public long getSimulationTimeAllowance()
	{
		return simulationTimeAllowance;
	}
}