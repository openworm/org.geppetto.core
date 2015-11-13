package org.geppetto.core.model.runtime;

import org.geppetto.core.model.typesystem.values.AValue;
import org.geppetto.core.model.typesystem.values.PhysicalQuantityValue;
import org.geppetto.core.model.typesystem.visitor.IAnalysis;

/**
 * Element of Visual Group used to store individual node properties not shared with rest of the nodes in the same group.
 * 
 * @author Jesus R. Martinez (jesus@metacell.us)
 */
public class VisualGroupElementNode extends AValue
{

	private String _defaultColor;
	private PhysicalQuantityValue _parameter;

	public VisualGroupElementNode(String id)
	{
		super(id);
	}

	public String getDefaultColor()
	{
		return this._defaultColor;
	}

	public void setDefaultColor(String defaultColor)
	{
		this._defaultColor = defaultColor;
	}

	public PhysicalQuantityValue getParameter()
	{
		return this._parameter;
	}

	public void setParameter(PhysicalQuantityValue parameter)
	{
		this._parameter = parameter;
	}

	@Override
	public boolean apply(IAnalysis visitor)
	{
		return visitor.visitVisualGroupElementNode(this);
	}
}
