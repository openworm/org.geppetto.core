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

import org.geppetto.core.model.state.AspectNode;
import org.geppetto.core.model.state.AspectTreeNode;
import org.geppetto.core.model.state.ColladaNode;
import org.geppetto.core.model.state.CompositeVariableNode;
import org.geppetto.core.model.state.CylinderNode;
import org.geppetto.core.model.state.EntityNode;
import org.geppetto.core.model.state.ParameterNode;
import org.geppetto.core.model.state.ParticleNode;
import org.geppetto.core.model.state.RuntimeTreeRoot;
import org.geppetto.core.model.state.SphereNode;
import org.geppetto.core.model.state.StateVariableNode;
import org.geppetto.core.model.state.TextMetadataNode;
import org.geppetto.core.model.state.URLMetadataNode;
import org.geppetto.core.model.state.VisualModelNode;

/**
 * @author matteocantarelli
 *
 */
public interface IStateVisitor
{
	boolean inCompositeStateNode( CompositeVariableNode node ); // going into a branch
	
	boolean outCompositeStateNode( CompositeVariableNode node ); // coming out
	
	boolean inAspectNode( AspectNode node ); // going into a branch
	
	boolean outAspectNode( AspectNode node ); // coming out

	boolean inEntityNode( EntityNode node ); // going into a branch
	
	boolean outEntityNode( EntityNode node ); // coming out
	
	boolean inColladaNode( ColladaNode node ); // going into a branch
	
	boolean outColladaNode( ColladaNode node ); // coming out
	
	boolean inAspectTreeNode( AspectTreeNode node ); // going into a branch
	
	boolean outAspectTreeNode( AspectTreeNode node ); // coming out
	
	boolean inVisualModelNode( VisualModelNode node ); // going into a branch
	
	boolean outVisualModelNode( VisualModelNode node ); // coming out
	
	boolean inRuntimeTreeRoot(RuntimeTreeRoot runtimeTreeRoot);

	boolean outRuntimeTreeRoot(RuntimeTreeRoot runtimeTreeRoot);
	
	boolean visitStateVariableNode( StateVariableNode node );
	
	boolean visitParameterNode( ParameterNode node );
	
	boolean visitTimeNode( StateVariableNode node );
	
	boolean visitTextMetadataNode(TextMetadataNode textMetadataNode);

	boolean visitURLMetadataNode(URLMetadataNode urlMetadataNode);
	
	boolean visitSphereNode(SphereNode sphereNode);
	
	boolean visitColladaNode(ColladaNode sphereNode);

	boolean visitCylinderNode(CylinderNode sphereNode);

	boolean visitParticleNode(ParticleNode sphereNode);

	void doStopVisiting();

	boolean stopVisiting();

}