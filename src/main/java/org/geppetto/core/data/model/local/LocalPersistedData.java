

package org.geppetto.core.data.model.local;

import org.geppetto.core.data.model.IPersistedData;
import org.geppetto.core.data.model.PersistedDataType;

public class LocalPersistedData implements IPersistedData
{
	private long id;

	private String url;

	private PersistedDataType type;

	public LocalPersistedData(long id, String url, PersistedDataType type)
	{
		this.id = id;
		this.url = url;
		this.type = type;
	}

	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public String getUrl()
	{
		return url;
	}

	@Override
	public PersistedDataType getType()
	{
		return type;
	}

	@Override
	public void setId(long id)
	{
		this.id = id;
	}

	@Override
	public void setURL(String url)
	{
		this.url = url;
	}

}