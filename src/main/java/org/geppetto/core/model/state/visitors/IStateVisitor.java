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
import org.geppetto.core.model.runtime.CompositeVariableNode;
import org.geppetto.core.model.runtime.CylinderNode;
import org.geppetto.core.model.runtime.DynamicsSpecificationNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.FunctionNode;
import org.geppetto.core.model.runtime.ParameterNode;
import org.geppetto.core.model.runtime.ParameterSpecificationNode;
import org.geppetto.core.model.runtime.ParticleNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.SphereNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.runtime.TextMetadataNode;
import org.geppetto.core.model.runtime.URLMetadataNode;

/**
 * @author matteocantarelli
 *
 */
public interface IStateVisitor
{
	boolean inCompositeVariableNode( CompositeVariableNode node ); // going into a branch
	
	boolean outCompositeVariableNode( CompositeVariableNode node ); // coming out
	
	boolean inAspectNode( AspectNode node ); // going into a branch
	
	boolean outAspectNode( AspectNode node ); // coming out

	boolean inEntityNode( EntityNode node ); // going into a branch
	
	boolean outEntityNode( EntityNode node ); // coming out
	
	boolean inColladaNode( ColladaNode node ); // going into a branch
	
	boolean outColladaNode( ColladaNode node ); // coming out
	
	boolean inAspectSubTreeNode( AspectSubTreeNode node ); // going into a branch
	
	boolean outAspectSubTreeNode( AspectSubTreeNode node ); // coming out
	
	boolean inRuntimeTreeRoot(RuntimeTreeRoot runtimeTreeRoot);

	boolean outRuntimeTreeRoot(RuntimeTreeRoot runtimeTreeRoot);
	
	boolean visitVariableNode( VariableNode node );
	
	boolean visitParameterNode( ParameterNode node );
	
	boolean visitDynamicsSpecificationNode(DynamicsSpecificationNode dynamicsSpecificationNode);

	boolean visitParameterSpecificationNode(ParameterSpecificationNode parameterSpecificationNode);

	boolean visitFunctionNode(FunctionNode functionNode);
	
	boolean visitTextMetadataNode(TextMetadataNode textMetadataNode);

	boolean visitURLMetadataNode(URLMetadataNode urlMetadataNode);
	
	boolean visitSphereNode(SphereNode sphereNode);
	
	boolean visitColladaNode(ColladaNode sphereNode);

	boolean visitCylinderNode(CylinderNode sphereNode);

	boolean visitParticleNode(ParticleNode sphereNode);

	void doStopVisiting();

	boolean stopVisiting();

}