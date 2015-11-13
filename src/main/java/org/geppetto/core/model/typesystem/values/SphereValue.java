package org.geppetto.core.model.typesystem.values;

import org.geppetto.core.model.runtime.AVisualObjectNode;
import org.geppetto.core.model.typesystem.visitor.IAnalysis;

/**
 * Node use to define a sphere for visualization and serialization
 * 
 * @author Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class SphereValue extends AVisualObjectNode
{

	private Double _radius;

	public SphereValue(String id)
	{
		super(id);
	}

	public Double getRadius()
	{
		return _radius;
	}

	public void setRadius(Double radius)
	{
		this._radius = radius;
	}

	@Override
	public boolean apply(IAnalysis visitor)
	{
		return visitor.visitSphereNode(this);
	}

}
