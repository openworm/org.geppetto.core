/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2011, 2013 OpenWorm.
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
package org.geppetto.core.model.state.visitors;

import org.geppetto.core.model.state.CompositeStateNode;
import org.geppetto.core.model.state.SimpleStateNode;

/**
 * @author matteocantarelli
 *
 */
public class DefaultStateVisitor implements IStateVisitor
{

	boolean _stopVisiting=false;
	
	/* (non-Javadoc)
	 * @see org.geppetto.core.model.state.visitors.IStateVisitor#visitEnter(org.geppetto.core.model.state.CompositeStateNode)
	 */
	@Override
	public boolean inCompositeStateNode(CompositeStateNode node)
	{
		return !_stopVisiting;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.model.state.visitors.IStateVisitor#visitLeave(org.geppetto.core.model.state.CompositeStateNode)
	 */
	@Override
	public boolean outCompositeStateNode(CompositeStateNode node)
	{
		return !_stopVisiting;
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.model.state.visitors.IStateVisitor#visit(org.geppetto.core.model.state.SimpleStateNode)
	 */
	@Override
	public boolean visitSimpleStateNode(SimpleStateNode node)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean stopVisiting()
	{
		return _stopVisiting;
	}
	
	@Override
	public void doStopVisiting()
	{
		_stopVisiting=true;
	}

}