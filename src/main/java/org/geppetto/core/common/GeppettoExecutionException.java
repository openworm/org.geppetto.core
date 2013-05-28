/**
 * 
 */
package org.geppetto.core.common;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoExecutionException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3179500281577232606L;

	public GeppettoExecutionException(Throwable cause)
	{
		super(cause);
	}

	public GeppettoExecutionException(String string)
	{
		super(string);
	}

}
