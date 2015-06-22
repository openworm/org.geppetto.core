package org.geppetto.core.services;

import java.util.HashMap;
import java.util.Map;

import org.geppetto.core.beans.PathConfiguration;
import org.geppetto.core.features.IFeature;

/**
 * This abstract implementation allows services to implement different features.
 * 
 * @author Jesus Martinez (jesus@metacell.us)
 * @author matteocantarelli
 *
 */
public abstract class AService implements IService
{

	private PathConfiguration pathConfig = new PathConfiguration();
	
	protected Map<GeppettoFeature, IFeature> features = new HashMap<GeppettoFeature, IFeature>();

	/* (non-Javadoc)
	 * @see org.geppetto.core.services.IService#isSupported(org.geppetto.core.services.GeppettoFeature)
	 */
	@Override
	public boolean isSupported(GeppettoFeature feature)
	{
		return features.containsKey(feature);
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.services.IService#getFeature(org.geppetto.core.services.GeppettoFeature)
	 */
	@Override
	public IFeature getFeature(GeppettoFeature feature)
	{
		return features.get(feature);
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.services.IService#addFeature(org.geppetto.core.features.IFeature)
	 */
	@Override
	public void addFeature(IFeature feature)
	{
		features.put(feature.getType(), feature);
	}
	
	/* (non-Javadoc)
	 * @see org.geppetto.core.services.IService#addFeature(org.geppetto.core.features.IFeature)
	 */
	@Override
	public PathConfiguration getPathConfiguration(){
		return pathConfig;
	}
}
