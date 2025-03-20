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

			// Print info about ARRAY_ID_RESULTS if it exists
			if (properties.containsKey("ARRAY_ID_RESULTS")) {
				Object value = properties.get("ARRAY_ID_RESULTS");
				String valueStr = value.toString();
				System.out.println("ARRAY_ID_RESULTS found in properties, length: " + valueStr.length());
			}

			 // Get the raw template content to perform pre-processing for large arrays
			String templateContent = null;
			try {
				StringWriter rawWriter = new StringWriter();
				ve.getTemplate(templatePath).merge(new VelocityContext(), rawWriter);
				templateContent = rawWriter.toString();
				System.out.println("Template loaded, length: " + templateContent.length());
				if(templateContent.contains("$ARRAY_ID_RESULTS")) {
					System.out.println("Template contains $ARRAY_ID_RESULTS placeholder");
				}
			} catch (Exception e) {
				System.out.println("Couldn't pre-read template: " + e.getMessage());
			}
			
			// Handle large ARRAY_ID_RESULTS - but don't remove from properties
			String largeArrayResults = null;
			if (properties.containsKey("ARRAY_ID_RESULTS")) {
				Object value = properties.get("ARRAY_ID_RESULTS");
				String valueStr = value.toString();
				if (valueStr.length() > 10000) {
					largeArrayResults = valueStr;
					// Keep it in the properties map as requested
					System.out.println("Large ARRAY_ID_RESULTS detected (" + valueStr.length() + " chars), will handle specially");
					
					// Pre-process the template if we were able to read it
					if (templateContent != null && templateContent.contains("$ARRAY_ID_RESULTS")) {
						templateContent = templateContent.replace("$ARRAY_ID_RESULTS", largeArrayResults);
						System.out.println("Pre-replaced $ARRAY_ID_RESULTS in template");
					}
				}
			}

			 // Create velocity context with all properties
			VelocityContext context = new VelocityContext();
			if(properties != null)
			{
				for(Map.Entry<String, Object> property : properties.entrySet())
				{
					Object value = property.getValue() == null ? "" : property.getValue();
					context.put(property.getKey(), value);
					if (property.getKey().equals("ARRAY_ID_RESULTS")) {
						System.out.println("Added ARRAY_ID_RESULTS to context, value length: " + value.toString().length());
					}
				}
			}

			// Process the template
			StringWriter writer = new StringWriter();
			String result;
			
			// If we have a pre-processed template with large array results, use that
			if (templateContent != null && largeArrayResults != null) {
				System.out.println("Using pre-processed template with direct replacement");
				ve.evaluate(context, writer, "preprocessedTemplate", templateContent);
				writer.flush();
				result = writer.toString();
			} else {
				// Otherwise use standard template processing
				Template t = ve.getTemplate(templatePath);
				t.merge(context, writer);
				writer.flush();
				result = writer.toString();
			}

			// Check if substitution was successful
			if (result.contains("$ARRAY_ID_RESULTS")) {
				System.out.println("WARNING: $ARRAY_ID_RESULTS still present in result after initial processing");
				
				// Try direct replacement as fallback
				if (largeArrayResults != null) {
					result = result.replace("$ARRAY_ID_RESULTS", largeArrayResults);
					System.out.println("Manually replaced $ARRAY_ID_RESULTS");
				}
			}

			String previousResult = "";
			// In this loop we keep using velocity until all the replacements are done
			int iterations = 0;
			while(result.contains("$") && !result.equals(previousResult) && iterations < 5)
			{
				iterations++;
				previousResult = result;
				writer = new StringWriter();
				ve.evaluate(context, writer, "doItAgain", result);
				writer.flush();
				result = writer.toString();
				writer.close();
				
				// Check if ARRAY_ID_RESULTS still needs replacement
				if (result.contains("$ARRAY_ID_RESULTS") && largeArrayResults != null) {
					System.out.println("$ARRAY_ID_RESULTS still present after iteration " + iterations);
					result = result.replace("$ARRAY_ID_RESULTS", largeArrayResults);
					System.out.println("Manually replaced $ARRAY_ID_RESULTS in iteration " + iterations);
				}
			}

			// Final check
			if (result.contains("$ARRAY_ID_RESULTS")) {
				System.out.println("WARNING: $ARRAY_ID_RESULTS still present in final result after " + iterations + " iterations");
			} else {
				System.out.println("Successfully processed template, no $ARRAY_ID_RESULTS placeholders remain");
			}

			return result;
		}
		catch(IOException e)
		{
			throw new GeppettoDataSourceException(e);
		}

	}
}
