
package org.geppetto.core.services.registry;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.conversion.IConversion;
import org.geppetto.core.datasources.IDataSourceService;
import org.geppetto.core.datasources.IQueryProcessor;
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
		Map<String, IModelInterpreter> modelBeans = applicationContext.getBeansOfType(IModelInterpreter.class, true, false);

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

		Map<String, IConversion> conversionBeans = applicationContext.getBeansOfType(IConversion.class, true, false);
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

		Map<String, ISimulator> simulatorBeans = applicationContext.getBeansOfType(ISimulator.class, true, false);
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
		
		Map<String, IDataSourceService> dataSourceServiceBeans = applicationContext.getBeansOfType(IDataSourceService.class, true, false);
		for(Map.Entry<String, IDataSourceService> dataSourceServiceBean : dataSourceServiceBeans.entrySet())
		{
			_logger.info("Registering Data Source Services: " + dataSourceServiceBean.getKey());
			try
			{
				applicationContexts.put(dataSourceServiceBean.getKey(), applicationContext);
				dataSourceServiceBean.getValue().registerGeppettoService();
			}
			catch(Exception e)
			{
				_logger.error("Error registering data source service: " + dataSourceServiceBean.getKey() + " Error:" + e.getMessage());
			}
		}
		
		Map<String, IQueryProcessor> queryProcessorBeans = applicationContext.getBeansOfType(IQueryProcessor.class, true, false);
		for(Map.Entry<String, IQueryProcessor> queryProcessorBean : queryProcessorBeans.entrySet())
		{
			_logger.info("Registering Query Processor Services: " + queryProcessorBean.getKey());
			try
			{
				applicationContexts.put(queryProcessorBean.getKey(), applicationContext);
				queryProcessorBean.getValue().registerGeppettoService();
			}
			catch(Exception e)
			{
				_logger.error("Error registering query processor service: " + queryProcessorBean.getKey() + " Error:" + e.getMessage());
			}
		}
	}
}