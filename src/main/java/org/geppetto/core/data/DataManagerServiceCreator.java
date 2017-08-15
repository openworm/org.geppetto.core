
package org.geppetto.core.data;

import org.geppetto.core.common.GeppettoInitializationException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class DataManagerServiceCreator
{

	private BundleContext _bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

	private String _type = null;

	public DataManagerServiceCreator(String type)
	{
		super();
		_type = type;
	}

	/**
	 * A method to get a service of a given type
	 * 
	 * @param type
	 * @return
	 * @throws InvalidSyntaxException
	 */
	public IGeppettoDataManager getService() throws GeppettoInitializationException
	{
		IGeppettoDataManager service = null;
		ServiceReference<?>[] sr;
		try
		{
			sr = _bc.getServiceReferences(_type, null);
		}
		catch(InvalidSyntaxException e)
		{
			throw new GeppettoInitializationException(e);
		}
		if(sr != null && sr.length > 0)
		{
			service = (IGeppettoDataManager) _bc.getService(sr[0]);
			for(ServiceReference<?> s : sr)
			{
				if(!((IGeppettoDataManager) _bc.getService(s)).isDefault())
				{
					// TODO Check are we always creating the two of them to decide?
					service = (IGeppettoDataManager) _bc.getService(s);
				}
			}
		}

		return service;
	}

}
