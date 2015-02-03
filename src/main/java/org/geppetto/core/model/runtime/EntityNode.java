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

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.model.state.visitors.IStateVisitor;
import org.geppetto.core.visualisation.model.Point;

/**
 * Node used to store an entity and then serialization.
 * 
 * @author Jesus R. Martinez (jesus@metacell.us)
 * 
 */
public class EntityNode extends ACompositeNode {

	private List<AspectNode> _aspects = new ArrayList<AspectNode>();
	protected List<ConnectionNode> _connections = new ArrayList<ConnectionNode>();
	private AMetadataNode _metadata;
	private Point _position;
	private boolean _modified = true;

	public EntityNode(String id) {
		super(id);
	}

	public void setAspects(List<AspectNode> aspects) {
		this._aspects = aspects;
	}

	public List<AspectNode> getAspects() {
		return _aspects;
	}
	
	public void setConnections(List<ConnectionNode> connections){
		this._connections = connections;
	}
	
	public List<ConnectionNode> getConnections(){
		return this._connections;
	}

	public AMetadataNode getMetadata() {
		return this._metadata;
	}

	public void setMetadata(AMetadataNode textMetadataNode) {

	}

	public void setPosition(Point position) {
		this._position = position;
	}

	public Point getPosition() {
		return this._position;
	}

	public boolean isModified() {
		return this._modified;
	}

	public void setModified(boolean mode) {
		this._modified = mode;
	}

	public void updateParentEntitiesFlags(boolean mode) {
		this._modified = mode;
		EntityNode parentEntity = null;
		if (this.getParent().getMetaType().equals("EntityNode")) {
			parentEntity = (EntityNode) this.getParent();
		}
		while (parentEntity != null) {
			parentEntity.setModified(mode);
			if (parentEntity.getParent().getMetaType().equals("EntityNode")) {
				parentEntity = (EntityNode) parentEntity.getParent();
			} else {
				parentEntity = null;
			}
		}
	}

	@Override
	public synchronized boolean apply(IStateVisitor visitor) {
		if (visitor.inEntityNode(this)) // enter this node?
		{
			for (ANode stateNode : this.getChildren()) {
				stateNode.apply(visitor);
				if (visitor.stopVisiting()) {
					break;
				}
			}
			for (AspectNode stateNode : this.getAspects()) {
				stateNode.apply(visitor);
				if (visitor.stopVisiting()) {
					break;
				}
			}
			for (ConnectionNode stateNode : this.getConnections()) {
				stateNode.apply(visitor);
				if (visitor.stopVisiting()) {
					break;
				}
			}
		}
		return visitor.outEntityNode(this);
	}
}
