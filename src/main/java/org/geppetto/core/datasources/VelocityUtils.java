/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2011 - 2015 OpenWorm.
 * http://openworm.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *     	OpenWorm - http://openworm.org/people.html
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
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
