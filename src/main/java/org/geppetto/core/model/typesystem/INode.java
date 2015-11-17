package org.geppetto.core.model.typesystem;

import org.geppetto.core.model.typesystem.visitor.IVisitable;

public interface INode extends IVisitable
{

	String getName();
	
	public abstract INode getParent();

	public abstract void setParent(ANode parent);
	
	String getPath();
	

}