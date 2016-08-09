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
package org.geppetto.core.manager;

import java.lang.reflect.Type;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.IUser;
import org.geppetto.core.simulation.IGeppettoManagerCallbackListener;

/**
 * @author matteocantarelli
 *
 */
public interface IGeppettoManager extends IProjectManager, IExperimentManager, IDropBoxManager, IRuntimeTreeManager, IDownloadManager, IDataSourceManager
{

	/**
	 * FIXME: Move to IAuthService?
	 * 
	 * @return
	 */
	IUser getUser();

	/**
	 * FIXME: Move to IAuthService?
	 * 
	 * @param user
	 * @throws GeppettoExecutionException
	 */
	void setUser(IUser user) throws GeppettoExecutionException;

	/**
	 * @return whether this geppetto manager has a connection or a run scope
	 */
	Scope getScope();

	void setSimulationListener(IGeppettoManagerCallbackListener listener);

	void setLogin(boolean b);
	
	boolean isLogin();
}
