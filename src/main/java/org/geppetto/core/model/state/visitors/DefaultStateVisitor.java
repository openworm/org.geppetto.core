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

import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectTreeNode;
import org.geppetto.core.model.runtime.ColladaNode;
import org.geppetto.core.model.runtime.CompositeVariableNode;
import org.geppetto.core.model.runtime.CylinderNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.ParameterNode;
import org.geppetto.core.model.runtime.ParticleNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.StateVariableNode;
import org.geppetto.core.model.runtime.TextMetadataNode;
import org.geppetto.core.model.runtime.URLMetadataNode;
import org.geppetto.core.model.runtime.VisualModelNode;

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
	public boolean inCompositeStateNode(CompositeVariableNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean outCompositeStateNode(CompositeVariableNode node) {
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
	public boolean visitTimeNode(StateVariableNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitTextMetadataNode(TextMetadataNode textMetadataNode) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitURLMetadataNode(URLMetadataNode urlMetadataNode) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitSphereNode(SphereNode sphereNode) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitColladaNode(ColladaNode sphereNode) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitCylinderNode(CylinderNode sphereNode) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitParticleNode(ParticleNode sphereNode) {
		return !_stopVisiting;
	}

	@Override
	public boolean inAspectNode(AspectNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean outAspectNode(AspectNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean inEntityNode(EntityNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean outEntityNode(EntityNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean inColladaNode(ColladaNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean outColladaNode(ColladaNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean inAspectTreeNode(AspectTreeNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean outAspectTreeNode(AspectTreeNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean inVisualModelNode(VisualModelNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean outVisualModelNode(VisualModelNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean inRuntimeTreeRoot(RuntimeTreeRoot runtimeTreeRoot) {
		return !_stopVisiting;
	}

	@Override
	public boolean outRuntimeTreeRoot(RuntimeTreeRoot runtimeTreeRoot) {
		return !_stopVisiting;
	}

}