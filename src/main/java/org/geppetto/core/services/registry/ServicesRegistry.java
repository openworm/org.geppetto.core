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

package org.geppetto.core.services.registry;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.geppetto.core.conversion.ConversionException;
import org.geppetto.core.conversion.IConversion;
import org.geppetto.core.model.IModel;
import org.geppetto.core.model.IModelInterpreter;
import org.geppetto.core.services.IModelFormat;
import org.geppetto.core.simulator.ISimulator;
import org.springframework.aop.TargetClassAware;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 */
public class ServicesRegistry {

	static Map<Class<? extends IModelInterpreter>, List<IModelFormat>> registeredModelInterpreterServices = new HashMap<Class<? extends IModelInterpreter>, List<IModelFormat>>();
	static Map<ConversionServiceKey, List<IConversion>> registeredConversionServices = new HashMap<ConversionServiceKey, List<IConversion>>();
	static Map<Class<? extends ISimulator>, List<IModelFormat>> registeredSimulatorServices = new HashMap<Class<? extends ISimulator>, List<IModelFormat>>();
	
	
	public static void registerModelInterpreterService(IModelInterpreter interpreterService, List<IModelFormat> outputModelFormats)
	{
		registeredModelInterpreterServices.put(interpreterService.getClass(), outputModelFormats);
	}
	
	@SuppressWarnings("rawtypes")
	public static List<IModelFormat> getModelInterpreterServiceFormats(IModelInterpreter interpreterService){
		Class interpreterServiceClass;
		if (interpreterService instanceof TargetClassAware){
			interpreterServiceClass = ((TargetClassAware)interpreterService).getTargetClass();
		}
		else{
			interpreterServiceClass = interpreterService.getClass();
		}
		return registeredModelInterpreterServices.get(interpreterServiceClass);
	}
	
	public static void registerSimulatorService(ISimulator simulatorService, List<IModelFormat> inputModelFormats)
	{
		registeredSimulatorServices.put(simulatorService.getClass(), inputModelFormats);
	}
	
	@SuppressWarnings("rawtypes")
	public static List<IModelFormat> getSimulatorServiceFormats(ISimulator simulatorService){
		Class simulatorServiceClass;
		if (simulatorService instanceof TargetClassAware){
			simulatorServiceClass = ((TargetClassAware)simulatorService).getTargetClass();
		}
		else{
			simulatorServiceClass = simulatorService.getClass();
		}
		
		return registeredSimulatorServices.get(simulatorServiceClass);
	}

	public static void registerConversionService(IConversion conversionService, List<IModelFormat> inputModelFormats, List<IModelFormat> outputModelFormats)
	{
		for (IModelFormat inputModelFormat : inputModelFormats){
			for (IModelFormat outputModelFormat : outputModelFormats){
				ServicesRegistry.ConversionServiceKey registeredConversionServiceKey = new ConversionServiceKey(inputModelFormat, outputModelFormat);
				List<IConversion> conversionList = registeredConversionServices.get(registeredConversionServiceKey);
				if (conversionList != null){
					conversionList.add(conversionService);
					registeredConversionServices.put(registeredConversionServiceKey, conversionList);
				}
				else{
					conversionList = new ArrayList<IConversion>();
					conversionList.add(conversionService);
					registeredConversionServices.put(registeredConversionServiceKey, conversionList);
				}
			}
		}
	}
	
	public static Map<IModelFormat, List<IConversion>> getSupportedOutputs(IModel model, List<IModelFormat> inputFormats){
		Map<IModelFormat, List<IConversion>> supportedOutputs = new HashMap<IModelFormat, List<IConversion>>();
		List<IConversion> checkedConversions = new ArrayList<IConversion>();
		for (Map.Entry<ConversionServiceKey, List<IConversion>> entry : registeredConversionServices.entrySet()) {
			ConversionServiceKey conversionServiceKey = entry.getKey();
			if (inputFormats.contains(conversionServiceKey.getInputModelFormat())){
				for (IConversion conversion : entry.getValue()){
					try
					{
						if (!checkedConversions.contains(conversion)){
							List<IModelFormat> outputs = conversion.getSupportedOutputs(model, conversionServiceKey.getInputModelFormat());
							for (IModelFormat output : outputs){
								List<IConversion> supportedConversions = supportedOutputs.get(output);
								if (supportedConversions != null){
									supportedConversions.add(conversion);
								}
								else{
									supportedConversions = new ArrayList<IConversion>();
									supportedConversions.add(conversion);
									supportedOutputs.put(output, supportedConversions);
								}
							}
						}
						
						checkedConversions.add(conversion);
					}
					catch(ConversionException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		return supportedOutputs;		
	}
	
	public static Map<ConversionServiceKey, List<IConversion>> getConversionServices(List<IModelFormat> inputModelFormats, List<IModelFormat> outputModelFormats)
	{
		Map<ConversionServiceKey, List<IConversion>> conversionServices = new HashMap<ConversionServiceKey, List<IConversion>>();
		for (IModelFormat inputModelFormat : inputModelFormats){
			for (IModelFormat outputModelFormat : outputModelFormats){
				ServicesRegistry.ConversionServiceKey registeredConversionServiceKey = new ConversionServiceKey(inputModelFormat, outputModelFormat);
				if (registeredConversionServices.containsKey(registeredConversionServiceKey)){
					conversionServices.put(registeredConversionServiceKey, registeredConversionServices.get(registeredConversionServiceKey));
				}
				
			}
		}
		
		return conversionServices;
	}
	
	public static List<IConversion> getConversionService(IModelFormat inputModelFormat, IModelFormat outputModelFormat)
	{
		return registeredConversionServices.get(new ConversionServiceKey(inputModelFormat, outputModelFormat));
	}
	
	public static class ConversionServiceKey {

		IModelFormat inputModelFormat;
		IModelFormat outputModelFormat;

	    public ConversionServiceKey(IModelFormat inputModelFormat, IModelFormat outputModelFormat) {
			super();
			this.inputModelFormat = inputModelFormat;
			this.outputModelFormat = outputModelFormat;
		}

	    public IModelFormat getInputModelFormat()
	    {
	    	return inputModelFormat;
	    }
	    
	    public IModelFormat getOutputModelFormat()
	    {
	    	return outputModelFormat;
	    }

	    @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((inputModelFormat == null) ? 0 : inputModelFormat
							.hashCode());
			result = prime
					* result
					+ ((outputModelFormat == null) ? 0 : outputModelFormat
							.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof ConversionServiceKey)) return false;
			ConversionServiceKey other = (ConversionServiceKey) o;
			return inputModelFormat.toString().equals(other.inputModelFormat.toString()) && outputModelFormat.toString().equals(other.outputModelFormat.toString());
		}

	}
}
