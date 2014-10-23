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

import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.ColladaNode;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.ConnectionNode;
import org.geppetto.core.model.runtime.CylinderNode;
import org.geppetto.core.model.runtime.DynamicsSpecificationNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.FunctionNode;
import org.geppetto.core.model.runtime.OBJNode;
import org.geppetto.core.model.runtime.ParameterNode;
import org.geppetto.core.model.runtime.ParameterSpecificationNode;
import org.geppetto.core.model.runtime.ParticleNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.runtime.TextMetadataNode;
import org.geppetto.core.model.runtime.URLMetadataNode;
import org.geppetto.core.model.runtime.VisualObjectReferenceNode;

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
	public boolean inCompositeNode(CompositeNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean outCompositeNode(CompositeNode node) {
		return !_stopVisiting;
	}


	@Override
	public boolean visitVariableNode(VariableNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitDynamicsSpecificationNode(DynamicsSpecificationNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitParameterSpecificationNode(ParameterSpecificationNode node) {
		return !_stopVisiting;
	}
	
	@Override
	public boolean visitFunctionNode(FunctionNode node) {
		return !_stopVisiting;
	}
	
	@Override
	public boolean visitParameterNode(ParameterNode node) {
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
	public boolean visitObjNode(OBJNode sphereNode) {
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
	public boolean inAspectSubTreeNode(AspectSubTreeNode node) {
		return !_stopVisiting;
	}

	@Override
	public boolean outAspectSubTreeNode(AspectSubTreeNode node) {
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

	@Override
	public boolean inConnectionNode(ConnectionNode connectionNode) {
		return !_stopVisiting;
	}
	
	@Override
	public boolean outConnectionNode(ConnectionNode connectionNode) {
		return !_stopVisiting;
	}

	@Override
	public boolean visitVisualObjectReferenceNode(
			VisualObjectReferenceNode visualReferenceNode) {
		return !_stopVisiting;
	}
}