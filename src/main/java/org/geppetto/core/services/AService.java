package org.geppetto.core.services;

import java.util.HashMap;
import java.util.Map;
import org.geppetto.core.features.IFeature;

public abstract class AService implements IService{

	Map<GeppettoFeature, IFeature> features = new HashMap<GeppettoFeature, IFeature>();

	@Override
	public boolean isSupported(GeppettoFeature feature){
		return features.containsKey(feature);
	}
	
	@Override
	public IFeature getFeature(GeppettoFeature feature){
		return features.get(feature);
	}
	
	@Override
	public void addFeature(IFeature feature){
		features.put(feature.getType(),feature);
	}
}
