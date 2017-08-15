

package org.geppetto.core.data.model.local;

import org.geppetto.core.data.model.IParameter;

public class LocalParameter implements IParameter
{
	private long id;

	private String variable;

	private String value;

	public LocalParameter(long id, String variable, String value)
	{
		this.id = id;
		this.variable = variable;
		this.value = value;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public String getVariable()
	{
		return variable;
	}

	@Override
	public void setValue(String value)
	{
		this.value = value;
	}

}