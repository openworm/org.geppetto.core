package org.geppetto.core.model;

public abstract class AModel implements IModel
{

	protected final String _id;

	public AModel(String id)
	{
		super();
		_id = id;
	}

	public String getId()
	{
		return _id;
	}

}