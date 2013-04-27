package org.geppetto.core.model;

import java.net.URL;
import java.util.List;

import org.geppetto.core.visualisation.model.Scene;

public interface IModelInterpreter {

	List<IModel> readModel(URL url) throws ModelInterpreterException;
	
	Scene getSceneFromModel(List<IModel> model) throws ModelInterpreterException;
	
}
