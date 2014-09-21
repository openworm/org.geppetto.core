/**
 * 
 */
package org.geppetto.core.utilities;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author matteocantarelli
 * 
 */
public class URLReader
{
	
	public static String readStringFromURL(URL url) throws IOException
	{
		Scanner s = new Scanner(url.openStream(), "UTF-8");
		String content = s.useDelimiter("\\A").next();
		s.close();
		return content;
	}
}
