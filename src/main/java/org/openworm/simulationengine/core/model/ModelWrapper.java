/**
 * 
 */
package org.openworm.simulationengine.core.model;

/**
 * @author matteocantarelli
 *
 */
public class ModelWrapper extends AModel
{
	
	Object model;
	
	public ModelWrapper(String id, Object model)
	{
		super(id);
		this.model = model;
	}

	public Object getModel()
	{
		return model;
	}

}
