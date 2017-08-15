
package org.geppetto.core.services;

import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.services.registry.ApplicationListenerBean;
import org.springframework.context.ApplicationContext;

/**
 * This class creates a service using the application context.
 * 
 * @author matteocantarelli
 *
 */
public class ServiceCreator
{

	public static Object getNewServiceInstance(String discoveryId) throws GeppettoInitializationException
	{
		ApplicationContext appContext = ApplicationListenerBean.getApplicationContext(discoveryId);
		if(appContext != null)
		{
			return appContext.getBean("scopedTarget." + discoveryId);
		}
		throw new GeppettoInitializationException("The service " + discoveryId + " was not found!");
	}
	
	
}
