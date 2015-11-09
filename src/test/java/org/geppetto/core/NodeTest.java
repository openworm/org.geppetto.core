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
package org.geppetto.core;

import junit.framework.Assert;

import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.ParameterSpecificationNode;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.runtime.VisualGroupNode;
import org.geppetto.core.model.typesystem.AspectNode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author matteocantarelli
 *
 */
public class NodeTest
{

	EntityNode entity1;
	EntityNode entity2;
	AspectNode aspect;
	ParameterSpecificationNode param;
	VariableNode var;
	VisualGroupNode vgroup;
	AspectSubTreeNode mt;
	AspectSubTreeNode vt;
	AspectSubTreeNode st;
	CompositeNode comp;
	
	public NodeTest()
	{
		super();
		entity1=new EntityNode("e1");
		entity2=new EntityNode("e2");
		entity2.setParent(entity1);
		aspect=new AspectNode("a1");
		aspect.setParent(entity2);
		mt=new AspectSubTreeNode(AspectTreeType.MODEL_TREE);
		vt=new AspectSubTreeNode(AspectTreeType.VISUALIZATION_TREE);
		st=new AspectSubTreeNode(AspectTreeType.SIMULATION_TREE);
		mt.setParent(aspect);
		st.setParent(aspect);
		vt.setParent(aspect);
		comp=new CompositeNode("comp");
		comp.setParent(mt);
		param=new ParameterSpecificationNode("p1");
		param.setParent(comp);
		var=new VariableNode("v1");
		var.setParent(st);
		vgroup = new VisualGroupNode("vg1");
		vgroup.setParent(vt);
	}

	@Test
	public void testGetInstancePath()
	{
		Assert.assertEquals("e1",entity1.getInstancePath());
		Assert.assertEquals("e1.e2",entity2.getInstancePath());
		Assert.assertEquals("e1.e2.a1",aspect.getInstancePath());
		Assert.assertEquals("e1.e2.a1.ModelTree.comp.p1",param.getInstancePath());
		Assert.assertEquals("e1.e2.a1.SimulationTree.v1",var.getInstancePath());
		Assert.assertEquals("e1.e2.a1.VisualizationTree.vg1",vgroup.getInstancePath());
	}
	
	@Test
	public void testGetLocalInstancePath()
	{
		Assert.assertEquals("",entity1.getLocalInstancePath());
		Assert.assertEquals("",entity2.getLocalInstancePath());
		Assert.assertEquals("",aspect.getLocalInstancePath());
		Assert.assertEquals("comp.p1",param.getLocalInstancePath());
		Assert.assertEquals("v1",var.getLocalInstancePath());
		Assert.assertEquals("vg1",vgroup.getLocalInstancePath());
	}
	
	@Test
	public void testGetEntityInstancePath()
	{
		Assert.assertEquals("e1",entity1.getEntityInstancePath());
		Assert.assertEquals("e1.e2",entity2.getEntityInstancePath());
		Assert.assertEquals("e1.e2",aspect.getEntityInstancePath());
		Assert.assertEquals("e1.e2",param.getEntityInstancePath());
		Assert.assertEquals("e1.e2",var.getEntityInstancePath());
		Assert.assertEquals("e1.e2",vgroup.getEntityInstancePath());
	}
	
	@Test
	public void testGetAspectInstancePath()
	{
		Assert.assertEquals("",entity1.getAspectInstancePath());
		Assert.assertEquals("",entity2.getAspectInstancePath());
		Assert.assertEquals("a1",aspect.getAspectInstancePath());
		Assert.assertEquals("a1.ModelTree",param.getAspectInstancePath());
		Assert.assertEquals("a1.ModelTree",mt.getAspectInstancePath());
		Assert.assertEquals("a1.SimulationTree",var.getAspectInstancePath());
		Assert.assertEquals("a1.SimulationTree",st.getAspectInstancePath());
		Assert.assertEquals("a1.VisualizationTree",vgroup.getAspectInstancePath());
		Assert.assertEquals("a1.VisualizationTree",vt.getAspectInstancePath());
	}
	

}
