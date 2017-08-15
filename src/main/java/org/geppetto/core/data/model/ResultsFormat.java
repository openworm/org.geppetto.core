
package org.geppetto.core.data.model;

/**
 * @author matteocantarelli
 *
 */
public enum ResultsFormat
{
	GEPPETTO_RECORDING("GEPPETTO_RECORDING"), RAW("RAW");

	private final String text;

	/**
	 * @param text
	 */
	private ResultsFormat(final String text)
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
