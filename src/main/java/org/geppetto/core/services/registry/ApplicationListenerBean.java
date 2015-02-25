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

import java.util.Map;

import org.geppetto.core.conversion.IConversion;
import org.geppetto.core.model.IModelInterpreter;
import org.geppetto.core.simulator.ISimulator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationListenerBean implements ApplicationListener<ContextRefreshedEvent>
{

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event)
	{
		ApplicationContext applicationContext = event.getApplicationContext();
		Map<String, IModelInterpreter> modelBeans = applicationContext.getBeansOfType(IModelInterpreter.class, false, false);
		//Map<String, IModelInterpreter> modelBeans = applicationContext.getBeansOfType(IModelInterpreter.class);
		
		for(Map.Entry<String, IModelInterpreter> modelBean : modelBeans.entrySet())
		{
			System.out.println(modelBean.getKey() + "/" + modelBean.getValue());
			modelBean.getValue().registerGeppettoService();
		}

		Map<String, IConversion> conversionBeans = applicationContext.getBeansOfType(IConversion.class, false, false);
		for(Map.Entry<String, IConversion> conversionBean : conversionBeans.entrySet())
		{
			System.out.println(conversionBean.getKey() + "/" + conversionBean.getValue());
			conversionBean.getValue().registerGeppettoService();
		}

		Map<String, ISimulator> simulatorBeans = applicationContext.getBeansOfType(ISimulator.class, false, false);
		for(Map.Entry<String, ISimulator> simulatorBean : simulatorBeans.entrySet())
		{
			System.out.println(simulatorBean.getKey() + "/" + simulatorBean.getValue());
			simulatorBean.getValue().registerGeppettoService();
		}
	}
}