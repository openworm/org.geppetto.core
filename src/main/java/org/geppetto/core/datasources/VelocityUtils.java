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

			System.out.println("Processing template with following properties:");
			if(properties != null)
			{
				for(Map.Entry<String, Object> property : properties.entrySet())
				{
					Object value = property.getValue() == null ? "" : property.getValue();
					context.put(property.getKey(), value);
					System.out.println("  Key: '" + property.getKey() + "', Value: '" + value + "'");
				}
			}

			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			writer.flush();
			String result = writer.toString();
			String previousResult = "";
			int iterations = 0;
			
			// In this loop we keep using velocity until all the replacements are done
			while(result.contains("$") && !result.equals(previousResult))
			{
				iterations++;
				previousResult = result;
				writer = new StringWriter();
				ve.evaluate(context, writer, "doItAgain", result);
				writer.flush();
				result = writer.toString();
				
				// Find remaining $ variables
				if(result.contains("$")) {
					System.out.println("After iteration " + iterations + ", these $ variables remain:");
					int index = result.indexOf("$");
					while(index != -1) {
						// Extract variable name or surrounding context
						int endIndex = Math.min(index + 30, result.length());
						String snippet = result.substring(index, endIndex);
						System.out.println("  Remaining $: " + snippet + "...");
						
						// Find next $ after current position
						index = result.indexOf("$", index + 1);
					}
				}
				
				writer.close();
			}

			if (result.contains("$")) {
				System.out.println("WARNING: $ still present in '" + result + "' after " + iterations + " iterations");
				System.out.println("Available context keys: " + context.getKeys());
			}

			return result;
		}
		catch(IOException e)
		{
			throw new GeppettoDataSourceException(e);
		}

	}
}
