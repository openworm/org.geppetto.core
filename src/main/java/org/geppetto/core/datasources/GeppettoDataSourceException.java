
package org.geppetto.core.datasources;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoDataSourceException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GeppettoDataSourceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public GeppettoDataSourceException(String message)
	{
		super(message);
	}

	public GeppettoDataSourceException(Throwable cause)
	{
		super(cause);
	}

}
