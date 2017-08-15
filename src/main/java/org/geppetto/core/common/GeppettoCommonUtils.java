
package org.geppetto.core.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoCommonUtils
{

	public static String readString(InputStream input) throws IOException
	{
		StringWriter writer = new StringWriter();
		IOUtils.copy(input, writer, StandardCharsets.UTF_8.name());
		return writer.toString();
	}
}
