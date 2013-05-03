/**
 * 
 */
package org.geppetto.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author matteocantarelli
 *
 */
public class ModelWrapper extends AModel
{
	
	Map<String, Object> _models;
	
	public ModelWrapper(String id)
	{
		super(id);
		_models=new HashMap<String, Object>();
	}

	public Object getModel(String id)
	{
		return _models.get(id);
	}
	
	public void wrapModel(String id,Object model)
	{
		_models.put(id,model);
	}

}
