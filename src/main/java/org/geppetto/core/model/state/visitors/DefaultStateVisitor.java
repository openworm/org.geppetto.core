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

import org.geppetto.core.model.state.ACompositeStateNode;
import org.geppetto.core.model.state.AVisualNode;
import org.geppetto.core.model.state.AspectNode;
import org.geppetto.core.model.state.AspectTreeNode;
import org.geppetto.core.model.state.CompositeVariableNode;
import org.geppetto.core.model.state.EntityNode;
import org.geppetto.core.model.state.ParameterNode;
import org.geppetto.core.model.state.SceneNode;
import org.geppetto.core.model.state.StateVariableNode;
import org.geppetto.core.model.state.TimeNode;
import org.geppetto.core.visualisation.model.Collada;
import org.geppetto.core.visualisation.model.Cylinder;
import org.geppetto.core.visualisation.model.Particle;
import org.geppetto.core.visualisation.model.Sphere;

/**
 * @author matteocantarelli
 *
 */
public class DefaultStateVisitor implements IStateVisitor
{

	boolean _stopVisiting=false;
	
	
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

	@Override
	public boolean inCompositeStateNode(ACompositeStateNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean outCompositeStateNode(ACompositeStateNode node) {
		return !_stopVisiting;
	}


	@Override
	public boolean visitStateVariableNode(StateVariableNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitParameterNode(ParameterNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitTimeNode(TimeNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitVisualObjectNode(AVisualNode aVisualObjectNode) {
		// TODO Auto-generated method stub
		return false;
	}

}