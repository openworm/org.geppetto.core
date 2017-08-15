

package org.geppetto.core.common;

/**
 * @author giovanni idili
 *
 */
public class GeppettoAccessException extends Exception
{

	private static final long serialVersionUID = -3179500281577232696L;

	public GeppettoAccessException(Throwable cause)
	{
		super(cause);
	}

	public GeppettoAccessException(String string)
	{
		super(string);
	}

	public GeppettoAccessException(String string, Throwable e1)
	{
		super(string,e1);
	}

}

