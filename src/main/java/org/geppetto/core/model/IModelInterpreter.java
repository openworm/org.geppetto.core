package org.geppetto.core.model;

import java.net.URL;

import org.geppetto.core.visualisation.model.Scene;

public interface IModelInterpreter {

	IModel readModel(URL url) throws ModelInterpreterException;
	
	Scene getSceneFromModel(IModel model, StateSet stateSet) throws ModelInterpreterException;
	
}
