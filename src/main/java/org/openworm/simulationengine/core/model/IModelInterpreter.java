package org.openworm.simulationengine.core.model;

import java.net.URL;
import java.util.List;

public interface IModelInterpreter {

	List<IModel> readModel(URL url);
	
}
