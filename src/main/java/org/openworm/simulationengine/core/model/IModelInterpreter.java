package org.openworm.simulationengine.core.model;

import java.net.URL;
import java.util.List;

import org.openworm.simulationengine.core.visualisation.model.Scene;

public interface IModelInterpreter {

	List<IModel> readModel(URL url);
	
	Scene getSceneFromModel(List<IModel> model);
	
}
