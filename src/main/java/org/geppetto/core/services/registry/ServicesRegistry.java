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
import org.geppetto.core.services.ModelFormat;
import org.geppetto.core.simulator.ISimulator;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 */
public class ServicesRegistry {

	static Map<IModelInterpreter, List<ModelFormat>> registeredModelInterpreterServices = new HashMap<IModelInterpreter, List<ModelFormat>>();
	static Map<ConversionServiceKey, List<IConversion>> registeredConversionServices = new HashMap<ConversionServiceKey, List<IConversion>>();
	static Map<ISimulator, List<ModelFormat>> registerSimulatorServices = new HashMap<ISimulator, List<ModelFormat>>();
	
	
	public static void registerModelInterpreterService(IModelInterpreter interpreterService, List<ModelFormat> outputModelFormats)
	{
		registeredModelInterpreterServices.put(interpreterService, outputModelFormats);
	}
	
	public static List<ModelFormat> getModelInterpreterServiceFormats(IModelInterpreter interpreterService){
		for (Map.Entry<IModelInterpreter, List<ModelFormat>> entry : registeredModelInterpreterServices.entrySet()) {
			IModelInterpreter key = entry.getKey();
			if (key.getName().equals(interpreterService.getName())){
				return entry.getValue();
			}
		}
		return null;
		
		//return registeredModelInterpreterServices.get(interpreterService);
	}
	
	public static void registerSimulatorService(ISimulator simulatorService, List<ModelFormat> inputModelFormats)
	{
		registerSimulatorServices.put(simulatorService, inputModelFormats);
	}
	
	public static List<ModelFormat> getSimulatorServiceFormats(ISimulator simulatorService){
		for (Map.Entry<ISimulator, List<ModelFormat>> entry : registerSimulatorServices.entrySet()) {
			ISimulator key = entry.getKey();
			if (key.getId().equals(simulatorService.getId())){
				return entry.getValue();
			}
		}
		return null;
		//return registerSimulatorService.get(simulatorService);
	}

	public static void registerConversionService(IConversion conversionService, List<ModelFormat> inputModelFormats, List<ModelFormat> outputModelFormats)
	{
		for (ModelFormat inputModelFormat : inputModelFormats){
			for (ModelFormat outputModelFormat : outputModelFormats){
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
	
	public static Map<ModelFormat, List<IConversion>> getSupportedOutputs(IModel model, List<ModelFormat> inputFormats){
		Map<ModelFormat, List<IConversion>> supportedOutputs = new HashMap<ModelFormat, List<IConversion>>();
		List<IConversion> checkedConversions = new ArrayList<IConversion>();
		for (Map.Entry<ConversionServiceKey, List<IConversion>> entry : registeredConversionServices.entrySet()) {
			ConversionServiceKey conversionServiceKey = entry.getKey();
			if (inputFormats.contains(conversionServiceKey.getInputModelFormat())){
				for (IConversion conversion : entry.getValue()){
					try
					{
						if (!checkedConversions.contains(conversion)){
							List<ModelFormat> outputs = conversion.getSupportedOutputs(model, conversionServiceKey.getInputModelFormat());
							for (ModelFormat output : outputs){
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
	
	public static Map<ConversionServiceKey, List<IConversion>> getConversionServices(List<ModelFormat> inputModelFormats, List<ModelFormat> outputModelFormats)
	{
		Map<ConversionServiceKey, List<IConversion>> conversionServices = new HashMap<ConversionServiceKey, List<IConversion>>();
		for (ModelFormat inputModelFormat : inputModelFormats){
			for (ModelFormat outputModelFormat : outputModelFormats){
				ServicesRegistry.ConversionServiceKey registeredConversionServiceKey = new ConversionServiceKey(inputModelFormat, outputModelFormat);
				if (registeredConversionServices.containsKey(registeredConversionServiceKey)){
					conversionServices.put(registeredConversionServiceKey, registeredConversionServices.get(registeredConversionServiceKey));
				}
				
			}
		}
		
		return conversionServices;
	}
	
	public static List<IConversion> getConversionService(ModelFormat inputModelFormat, ModelFormat outputModelFormat)
	{
		return registeredConversionServices.get(new ConversionServiceKey(inputModelFormat, outputModelFormat));
	}
	
	public static class ConversionServiceKey {

		ModelFormat inputModelFormat;
		ModelFormat outputModelFormat;

	    public ConversionServiceKey(ModelFormat inputModelFormat, ModelFormat outputModelFormat) {
			super();
			this.inputModelFormat = inputModelFormat;
			this.outputModelFormat = outputModelFormat;
		}

	    public ModelFormat getInputModelFormat()
	    {
	    	return inputModelFormat;
	    }
	    
	    public ModelFormat getOutputModelFormat()
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
			return inputModelFormat.equals(other.inputModelFormat) && outputModelFormat.equals(other.outputModelFormat);
		}

	}
}
