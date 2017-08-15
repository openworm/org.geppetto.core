

package org.geppetto.core.data.model;

public enum PersistedDataType
{
	RECORDING("RECORDING"), GEPPETTO_PROJECT("GEPPETTO_PROJECT"), MODEL("MODEL");

	private final String text;

	/**
	 * @param text
	 */
	private PersistedDataType(final String text)
	{
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return text;
	}

}
