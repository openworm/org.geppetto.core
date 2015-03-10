/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011, 2013 OpenWorm.
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
package org.geppetto.core;


import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.geppetto.core.conversion.IConversion;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.services.IModelFormat;
import org.geppetto.core.services.registry.ServicesRegistry;
import org.geppetto.core.services.registry.ServicesRegistry.ConversionServiceKey;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 */
public class ServiceRegistryTest
{
	@BeforeClass
	public static void setup(){
		TestModelInterpreter testModelInterpreter = new TestModelInterpreter();
		testModelInterpreter.registerGeppettoService();
		TestModelInterpreter2 testModelInterpreter2 = new TestModelInterpreter2();
		testModelInterpreter2.registerGeppettoService();
		
		TestSimulator testSimulator = new TestSimulator();
		testSimulator.registerGeppettoService();
		TestSimulator2 testSimulator2 = new TestSimulator2();
		testSimulator2.registerGeppettoService();
		
		TestConversion testConversion = new TestConversion();
		testConversion.registerGeppettoService();
		TestConversion2 testConversion2 = new TestConversion2();
		testConversion2.registerGeppettoService();
		
	}
	/**
	 * "" Test method for {@link org.geppetto.core.services.registry.ServicesRegistry#registerModelInterpreterService(org.geppetto.core.model.IModelInterpreter)}.
	 * 
	 */
	@Test
	public void testRegisterModelInterpreterService(){
		List<IModelFormat> modelFormats = null;
		List<IModelFormat> modelFormats2 = null;
		
		modelFormats = ServicesRegistry.getModelInterpreterServiceFormats(new TestModelInterpreter());
		modelFormats2 = ServicesRegistry.getModelInterpreterServiceFormats(new TestModelInterpreter2());
		assertNotNull(modelFormats);
		Assert.assertEquals(modelFormats.get(0), ModelFormat.TEST);
		assertNotNull(modelFormats2);
		Assert.assertEquals(modelFormats2.get(0), ModelFormat.TEST2);
	}
	
	/**
	 * "" Test method for {@link org.geppetto.core.services.registry.ServicesRegistry#registerModelInterpreterService(org.geppetto.core.model.IModelInterpreter)}.
	 * 
	 */
	@Test
	public void testRegisterSimulatorService(){
		List<IModelFormat> modelFormats = null;
		List<IModelFormat> modelFormats2 = null;
		
		modelFormats = ServicesRegistry.getSimulatorServiceFormats(new TestSimulator());
		modelFormats2 = ServicesRegistry.getSimulatorServiceFormats(new TestSimulator2());
		assertNotNull(modelFormats);
		Assert.assertEquals(modelFormats.get(0), ModelFormat.TEST);
		assertNotNull(modelFormats2);
		Assert.assertEquals(modelFormats2.get(0), ModelFormat.TEST2);
	}
	
	/**
	 * "" Test method for {@link org.geppetto.core.services.registry.ServicesRegistry#registerModelInterpreterService(org.geppetto.core.model.IModelInterpreter)}.
	 * 
	 */
	@Test
	public void testRegisterConversionService(){
		List<IModelFormat> modelFormats = new ArrayList<IModelFormat>();
		modelFormats.add(ModelFormat.TEST);
		List<IModelFormat> modelFormats2 = new ArrayList<IModelFormat>();
		modelFormats2.add(ModelFormat.TEST2);
		ConversionServiceKey conversionServiceKey = new ConversionServiceKey(ModelFormat.TEST, ModelFormat.TEST2);
		ConversionServiceKey conversionServiceKey2 = new ConversionServiceKey(ModelFormat.TEST2, ModelFormat.TEST);
		Map<ConversionServiceKey, List<IConversion>> conversionServiceMap = null;
		List<IConversion> conversionServiceList = null;
		
		conversionServiceMap = ServicesRegistry.getConversionService(modelFormats, modelFormats2);
		assertNotNull(conversionServiceMap);
		Assert.assertEquals(conversionServiceMap.size(), 1);
		Assert.assertTrue(conversionServiceMap.containsKey(conversionServiceKey));
		assertNotNull(conversionServiceMap.get(conversionServiceKey));
		Assert.assertEquals(conversionServiceMap.get(conversionServiceKey).get(0).getClass(), new TestConversion().getClass());
		
		conversionServiceList = ServicesRegistry.getConversionService(ModelFormat.TEST, ModelFormat.TEST2);
		assertNotNull(conversionServiceList);
		Assert.assertEquals(conversionServiceList.size(), 1);
		Assert.assertEquals(conversionServiceMap.get(conversionServiceKey).get(0).getClass(), new TestConversion().getClass());
		
		conversionServiceMap = ServicesRegistry.getConversionService(modelFormats2, modelFormats);
		assertNotNull(conversionServiceMap);
		Assert.assertEquals(conversionServiceMap.size(), 1);
		Assert.assertTrue(conversionServiceMap.containsKey(conversionServiceKey2));
		assertNotNull(conversionServiceMap.get(conversionServiceKey2));
		Assert.assertEquals(conversionServiceMap.get(conversionServiceKey2).get(0).getClass(), new TestConversion2().getClass());
		
		conversionServiceList = ServicesRegistry.getConversionService(ModelFormat.TEST2, ModelFormat.TEST);
		assertNotNull(conversionServiceList);
		Assert.assertEquals(conversionServiceList.size(), 1);
		Assert.assertEquals(conversionServiceMap.get(conversionServiceKey2).get(0).getClass(), new TestConversion2().getClass());
		
	}
	
	/**
	 * "" Test method for {@link org.geppetto.core.services.registry.ServicesRegistry#registerModelInterpreterService(org.geppetto.core.model.IModelInterpreter)}.
	 * 
	 */
	@Test
	public void testSupportedOutputs(){
		Map<IModelFormat, List<IConversion>> outputsMap = null;
		List<IModelFormat> modelFormats = new ArrayList<IModelFormat>();
		modelFormats.add(ModelFormat.TEST);
		List<IModelFormat> modelFormats2 = new ArrayList<IModelFormat>();
		modelFormats2.add(ModelFormat.TEST2);
		
		outputsMap = ServicesRegistry.getSupportedOutputs(new ModelWrapper(""), modelFormats);
		assertNotNull(outputsMap);
		Assert.assertEquals(outputsMap.size(), 1);
		Assert.assertTrue(outputsMap.containsKey(ModelFormat.TEST2));
		assertNotNull(outputsMap.get(ModelFormat.TEST2));
		Assert.assertEquals(outputsMap.get(ModelFormat.TEST2).get(0).getClass(), new TestConversion().getClass());
		
	}

}
