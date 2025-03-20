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

			 // Extract large ARRAY_ID_RESULTS if present
			String largeArrayResults = null;
			if (properties.containsKey("ARRAY_ID_RESULTS")) {
				Object value = properties.get("ARRAY_ID_RESULTS");
				String valueStr = value.toString();
				System.out.println("ARRAY_ID_RESULTS found in properties, length: " + valueStr.length());
				
				// Save large lists for direct string replacement
				if (valueStr.length() > 10000) {
					largeArrayResults = valueStr;
					System.out.println("Large ARRAY_ID_RESULTS detected (" + valueStr.length() + " chars), will handle manually");
				}
			}

			// First load the raw template to examine it
			String templateContent = null;
			try {
				Template template = ve.getTemplate(templatePath);
				StringWriter rawWriter = new StringWriter();
				template.merge(new VelocityContext(), rawWriter);
				templateContent = rawWriter.toString();
				System.out.println("Template loaded, length: " + templateContent.length());
			} catch (Exception e) {
				System.out.println("Couldn't pre-read template: " + e.getMessage());
			}

			// Create a standard velocity context
			VelocityContext context = new VelocityContext();
			if (properties != null) {
				for (Map.Entry<String, Object> property : properties.entrySet()) {
					Object value = property.getValue() == null ? "" : property.getValue();
					context.put(property.getKey(), value);
				}
			}
			
			// Special handling for JSON templates with ARRAY_ID_RESULTS
			if (templateContent != null && largeArrayResults != null && 
				(templateContent.contains("\"fq\"") || templateContent.contains("'fq'"))) {
				
				// This looks like a JSON template with a filter query parameter
				// Process it using direct string replacement first
				templateContent = templateContent.replace("$ARRAY_ID_RESULTS", largeArrayResults);
				
				// Now process with Velocity engine to handle any other variables
				StringWriter writer = new StringWriter();
				ve.evaluate(context, writer, "jsonTemplate", templateContent);
				return writer.toString();
			} else {
				// Standard template processing for other cases
				StringWriter writer = new StringWriter();
				Template t = ve.getTemplate(templatePath);
				t.merge(context, writer);
				
				String result = writer.toString();
				
				// Final check for any remaining ARRAY_ID_RESULTS if we have it
				if (largeArrayResults != null && result.contains("$ARRAY_ID_RESULTS")) {
					result = result.replace("$ARRAY_ID_RESULTS", largeArrayResults);
					System.out.println("Manually replaced $ARRAY_ID_RESULTS in result");
				}
				
				return result;
			}
		}
		catch(IOException e)
		{
			throw new GeppettoDataSourceException(e);
		}

	}
}
