package org.geppetto.core.services;

import java.util.HashMap;
import java.util.Map;

import org.geppetto.core.features.IFeature;
import org.geppetto.core.manager.Scope;

/**
 * This abstract implementation allows services to implement different features.
 * 
 * @author Jesus Martinez (jesus@metacell.us)
 * @author matteocantarelli
 *
 */
public abstract class AService implements IService
{

	protected long projectId = -1;

	protected Scope scope = Scope.CONNECTION;

	public void setProjectId(long projectId)
	{
		this.projectId = projectId;
	}

	public long getsetProjectId()
	{
		return projectId;
	}

	public void setScope(Scope scope)
	{
		this.scope = scope;
	}

	public Scope getScope()
	{
		return scope;
	}

	protected Map<GeppettoFeature, IFeature> features = new HashMap<GeppettoFeature, IFeature>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.services.IService#isSupported(org.geppetto.core.services.GeppettoFeature)
	 */
	@Override
	public boolean isSupported(GeppettoFeature feature)
	{
		return features.containsKey(feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.services.IService#getFeature(org.geppetto.core.services.GeppettoFeature)
	 */
	@Override
	public IFeature getFeature(GeppettoFeature feature)
	{
		return features.get(feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.services.IService#addFeature(org.geppetto.core.features.IFeature)
	 */
	@Override
	public void addFeature(IFeature feature)
	{
		features.put(feature.getType(), feature);
	}

}
