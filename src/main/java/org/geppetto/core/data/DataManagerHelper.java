
package org.geppetto.core.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoInitializationException;

/**
 * @author dandromereschi
 *
 */
public class DataManagerHelper
{

	private static Log logger = LogFactory.getLog(DataManagerHelper.class);

	private static IGeppettoDataManager dataManager = null;

	public static IGeppettoDataManager getDataManager()
	{
		if(dataManager == null)
		{
			try
			{
				dataManager = new DataManagerServiceCreator(IGeppettoDataManager.class.getName()).getService();
			}
			catch(GeppettoInitializationException e)
			{
				logger.error("Error while retrieving a data manager", e);
			}
		}
		return dataManager;
	}
	
	/**
	 * TO BE USED BY TESTS ONLY
	 * @param dataManager
	 * @deprecated This method is to be used by tests only
	 */
	public static void setDataManager(IGeppettoDataManager dataManager)
	{
		DataManagerHelper.dataManager=dataManager;
	}

}
