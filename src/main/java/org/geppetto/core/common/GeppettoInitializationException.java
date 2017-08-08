

package org.geppetto.core.common;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoInitializationException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6071408236013277603L;

	public GeppettoInitializationException(Throwable arg0)
	{
		super(arg0);
	}

	public GeppettoInitializationException(String message)
	{
		super(message);
	}

	public GeppettoInitializationException(String message, Throwable arg0)
	{
		super(message, arg0);
	}

}
