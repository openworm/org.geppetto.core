package org.geppetto.core.model.runtime;

import org.geppetto.core.model.typesystem.values.AValue;
import org.geppetto.core.model.typesystem.visitor.IAnalysis;

/**
 * Used inside connections to reference 3D objects
 * 
 * @author Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class VisualObjectReferenceNode extends AValue
{

	private String _aspectInstancePath;
	private String _visualObjectId;

	public void setAspectInstancePath(String path)
	{
		this._aspectInstancePath = path;
	}

	public String getAspectInstancePath()
	{
		return this._aspectInstancePath;
	}

	public void setVisualObjectId(String id)
	{
		this._visualObjectId = id;
	}

	public String getVisualObjectId()
	{
		return this._visualObjectId;
	}

	public VisualObjectReferenceNode(String id)
	{
		super(id);
	}

	@Override
	public boolean apply(IAnalysis visitor)
	{
		return visitor.visitVisualObjectReferenceNode(this);
	}
}
