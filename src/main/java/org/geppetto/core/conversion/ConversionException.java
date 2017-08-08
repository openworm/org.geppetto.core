

package org.geppetto.core.conversion;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 *
 */
public class ConversionException extends Exception
{

	public ConversionException(String message)
	{
		super(message);
	}

	public ConversionException(Throwable e)
	{
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8712503151350991595L;

}
