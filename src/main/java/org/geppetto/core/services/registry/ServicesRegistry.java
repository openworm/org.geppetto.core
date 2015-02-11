package org.geppetto.core.services.registry;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.geppetto.core.conversion.IConversion;
import org.geppetto.core.model.IModelInterpreter;
import org.geppetto.core.services.ModelFormat;
import org.geppetto.core.simulator.ISimulator;

/**
 * @author Adrian Quintana (adrian.perez@ucl.ac.uk)
 * 
 */
public class ServicesRegistry {

	static Map<IModelInterpreter, List<ModelFormat>> registeredModelInterpreterServices = new HashMap<IModelInterpreter, List<ModelFormat>>();
	static Map<RegisteredConversionServiceKey, List<IConversion>> registeredConversionService = new HashMap<RegisteredConversionServiceKey, List<IConversion>>();
	static Map<ISimulator, List<ModelFormat>> registerSimulatorService = new HashMap<ISimulator, List<ModelFormat>>();
	
	
	public static void registerModelInterpreterService(IModelInterpreter interpreterService, List<ModelFormat> outputModelFormats)
	{
		registeredModelInterpreterServices.put(interpreterService, outputModelFormats);
		System.out.println("Registring Model Interpreter Services");
		System.out.println(registeredModelInterpreterServices);
	}
	
	public static void registerConversionService(IConversion conversionService, List<ModelFormat> inputModelFormats, List<ModelFormat> outputModelFormats)
	{
		for (ModelFormat inputModelFormat : inputModelFormats){
			for (ModelFormat outputModelFormat : outputModelFormats){
				ServicesRegistry.RegisteredConversionServiceKey registeredConversionServiceKey = new ServicesRegistry().new RegisteredConversionServiceKey(inputModelFormat, outputModelFormat);
				List<IConversion> conversionList = registeredConversionService.get(registeredConversionServiceKey);
				if (conversionList != null){
					conversionList.add(conversionService);
					registeredConversionService.put(registeredConversionServiceKey, conversionList);
				}
				else{
					conversionList = new ArrayList<IConversion>();
					conversionList.add(conversionService);
					registeredConversionService.put(registeredConversionServiceKey, conversionList);
				}
			}
		}
	}
	
	public static void registerSimulatorService(ISimulator simulatorService, List<ModelFormat> inputModelFormats)
	{
		registerSimulatorService.put(simulatorService, inputModelFormats);
		System.out.println("Registring Model Interpreter Services");
		System.out.println(registerSimulatorService);
	}
	
	public List<IConversion> checkCompatibility(ModelFormat inputModelFormat, ModelFormat outputModelFormat)
	{
		ServicesRegistry.RegisteredConversionServiceKey registeredConversionServiceKey = new ServicesRegistry().new RegisteredConversionServiceKey(inputModelFormat, outputModelFormat);
		return registeredConversionService.get(registeredConversionServiceKey);
	}
	
	public class RegisteredConversionServiceKey {

		ModelFormat inputModelFormat;
		ModelFormat outputModelFormat;

	    public RegisteredConversionServiceKey(ModelFormat inputModelFormat, ModelFormat outputModelFormat) {
			super();
			this.inputModelFormat = inputModelFormat;
			this.outputModelFormat = outputModelFormat;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
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
			if (o == null || !(o instanceof RegisteredConversionServiceKey)) return false;
			RegisteredConversionServiceKey other = (RegisteredConversionServiceKey) o;
			return inputModelFormat == other.inputModelFormat && outputModelFormat == other.outputModelFormat;
		}

		private ServicesRegistry getOuterType() {
			return ServicesRegistry.this;
		}
	   
	}
}
