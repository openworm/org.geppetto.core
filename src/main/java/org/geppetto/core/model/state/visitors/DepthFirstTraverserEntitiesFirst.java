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
package org.geppetto.core.model.state.visitors;

import org.geppetto.core.model.simulation.Aspect;
import org.geppetto.core.model.simulation.Entity;
import org.geppetto.core.model.simulation.GeppettoModel;
import org.geppetto.core.model.simulation.Model;
import org.geppetto.core.model.simulation.visitor.DepthFirstTraverserImpl;
import org.geppetto.core.model.simulation.visitor.Visitor;

/**
 * @author matteocantarelli
 *
 */
public class DepthFirstTraverserEntitiesFirst extends DepthFirstTraverserImpl
{
	protected boolean _stopVisiting=false;
	
	@Override
	public void traverse(Entity aBean, Visitor aVisitor)
	{
        for (Entity bean: aBean.getEntities()) {
            bean.accept(aVisitor);
            if(_stopVisiting)
            {
            	return;
            }
        }
        for (Aspect bean: aBean.getAspects()) {
            bean.accept(aVisitor);
            if(_stopVisiting)
            {
            	return;
            }
        }
        if (aBean.getPosition()!= null) {
            aBean.getPosition().accept(aVisitor);
            if(_stopVisiting)
            {
            	return;
            }
        }
	}


	public void traverse(Aspect aBean, Visitor aVisitor) {
        if (aBean.getModel()!= null) {
            aBean.getModel().accept(aVisitor);
            if(_stopVisiting)
            {
            	return;
            }
        }
    	// SIM TODO
//        if (aBean.getSimulator()!= null) {
//            aBean.getSimulator().accept(aVisitor);
//            if(_stopVisiting)
//            {
//            	return;
//            }
//        }
    }


    public void traverse(Model aBean, Visitor aVisitor) {

    }


    public void traverse(GeppettoModel aBean, Visitor aVisitor) {
        for (Entity bean: aBean.getEntities()) {
            bean.accept(aVisitor);
            if(_stopVisiting)
            {
            	return;
            }
        }
    }

	// SIM TODO
//    public void traverse(Simulator aBean, Visitor aVisitor) {
//        if (aBean.getTimeStep()!= null) {
//            aBean.getTimeStep().accept(aVisitor);
//        }
//    }

}
