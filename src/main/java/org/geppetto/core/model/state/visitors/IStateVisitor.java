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
import org.geppetto.core.model.runtime.VisualGroupElementNode;
import org.geppetto.core.model.runtime.VisualGroupNode;
import org.geppetto.core.model.runtime.VisualObjectReferenceNode;

/**
 * @author matteocantarelli
 *
 */
public interface IStateVisitor
{
	boolean inCompositeNode( CompositeNode node ); // going into a branch
	
	boolean outCompositeNode( CompositeNode node ); // coming out
	
	boolean inAspectNode( AspectNode node ); // going into a branch
	
	boolean outAspectNode( AspectNode node ); // coming out

	boolean inEntityNode( EntityNode node ); // going into a branch
	
	boolean outEntityNode( EntityNode node ); // coming out
	
	boolean inAspectSubTreeNode( AspectSubTreeNode node ); // going into a branch
	
	boolean outAspectSubTreeNode( AspectSubTreeNode node ); // coming out
	
	boolean inRuntimeTreeRoot(RuntimeTreeRoot runtimeTreeRoot);

	boolean outRuntimeTreeRoot(RuntimeTreeRoot runtimeTreeRoot);
	
	boolean inVisualGroupNode(VisualGroupNode visualGroupNode);

	boolean outVisualGroupNode(VisualGroupNode visualGroupNode);
	
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
	
	boolean visitObjNode(OBJNode objNode);

	void doStopVisiting();

	boolean stopVisiting();

	boolean inConnectionNode(ConnectionNode connectionNode);
	
	boolean outConnectionNode(ConnectionNode connectionNode);
	
	boolean visitVisualObjectReferenceNode(VisualObjectReferenceNode visualReferenceNode);

	boolean visitVisualGroupElementNode(VisualGroupElementNode visualGroupElementNode);
}
