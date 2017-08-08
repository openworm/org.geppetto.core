

package org.geppetto.core.data.model;

public interface IPersistedData extends IDataEntity
{

	String getUrl();

	PersistedDataType getType();

	void setURL(String string);

}