
package org.geppetto.core.datasources;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * @author matteocantarelli
 *
 */
public class VelocityUtils
{

	/**
	 * @param templatePath
	 * @param properties
	 * @return
	 * @throws GeppettoDataSourceException
	 */
	public static String processTemplate(String templatePath, Map<String, Object> properties) throws GeppettoDataSourceException
	{
		try
		{
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","velocity");
			ve.init();

			Template t = ve.getTemplate(templatePath);
			VelocityContext context = new VelocityContext();

			if(properties != null)
			{
				for(Map.Entry<String, Object> property : properties.entrySet())
				{
					Object value = property.getValue() == null ? "" : property.getValue();
					context.put(property.getKey(), value);
				}
			}

			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			writer.flush();
			String result = writer.toString();
			String previousResult = "";
			// In this loop we keep using velocity until all the replacements are done
			while(result.contains("$") && !result.equals(previousResult))
			{
				previousResult = result;
				writer = new StringWriter();
				ve.evaluate(context, writer, "doItAgain", result);
				writer.flush();
				result = writer.toString();
				writer.close();
			}

			return result;
		}
		catch(IOException e)
		{
			throw new GeppettoDataSourceException(e);
		}

	}
}
