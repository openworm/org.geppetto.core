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
package org.geppetto.core.model.runtime;

import org.geppetto.core.model.state.visitors.IStateVisitor;
import org.geppetto.core.visualisation.model.Point;

/**
 * Node use to define a cylinder for visualization and serialization
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class CylinderNode extends AVisualObjectNode{

	private Double _radiusTop;
    private Double _radiusBottom;
    private Double _height;
    private Point _distal;
    
    public CylinderNode(String id)
	{
    	super(id);
	}

	public Double getRadiusTop() {
		return _radiusTop;
	}

	public void setRadiusTop(Double radiusTop) {
		this._radiusTop = radiusTop;
	}

	public Double getRadiusBottom() {
		return _radiusBottom;
	}

	public void setRadiusBottom(Double radiusBottom) {
		this._radiusBottom = radiusBottom;
	}

	public Double getHeight() {
		return _height;
	}

	public void setHeight(Double height) {
		this._height = height;
	}

	public Point getDistal() {
		return _distal;
	}

	public void setDistal(Point distal) {
		this._distal = distal;
	}
    
	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitCylinderNode(this);
	}
}
