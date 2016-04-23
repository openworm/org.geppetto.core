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
package org.geppetto.core.services.registry;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.conversion.IConversion;
import org.geppetto.core.model.IModelInterpreter;
import org.geppetto.core.simulator.ISimulator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationListenerBean implements ApplicationListener<ContextRefreshedEvent>
{
	private static Log _logger = LogFactory.getLog(ApplicationListenerBean.class);

	private static Map<String, ApplicationContext> applicationContexts = new HashMap<String, ApplicationContext>();

	/**
	 * @return
	 */
	public static ApplicationContext getApplicationContext(String service)
	{
		return applicationContexts.get(service);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event)
	{
		ApplicationContext applicationContext = event.getApplicationContext();
		Map<String, IModelInterpreter> modelBeans = applicationContext.getBeansOfType(IModelInterpreter.class, false, false);

		for(Map.Entry<String, IModelInterpreter> modelBean : modelBeans.entrySet())
		{
			_logger.info("Registering Model Interpreter Services: " + modelBean.getKey());
			try
			{
				applicationContexts.put(modelBean.getKey(), applicationContext);
				modelBean.getValue().registerGeppettoService();
			}
			catch(Exception e)
			{
				_logger.error("Error registering model interpreter service: " + modelBean.getKey() + " Error:" + e.getMessage());
			}
		}

		Map<String, IConversion> conversionBeans = applicationContext.getBeansOfType(IConversion.class, false, false);
		for(Map.Entry<String, IConversion> conversionBean : conversionBeans.entrySet())
		{
			_logger.info("Registering Conversion Services: " + conversionBean.getKey());
			try
			{
				applicationContexts.put(conversionBean.getKey(), applicationContext);
				conversionBean.getValue().registerGeppettoService();
			}
			catch(Exception e)
			{
				_logger.error("Error registering conversion service: " + conversionBean.getKey() + " Error:" + e.getMessage());
			}
		}

		Map<String, ISimulator> simulatorBeans = applicationContext.getBeansOfType(ISimulator.class, false, false);
		for(Map.Entry<String, ISimulator> simulatorBean : simulatorBeans.entrySet())
		{
			_logger.info("Registering Simulator Services: " + simulatorBean.getKey());
			try
			{
				applicationContexts.put(simulatorBean.getKey(), applicationContext);
				simulatorBean.getValue().registerGeppettoService();
			}
			catch(Exception e)
			{
				_logger.error("Error registering simulator service: " + simulatorBean.getKey() + " Error:" + e.getMessage());
			}
		}
	}
}