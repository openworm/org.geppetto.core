package org.geppetto.core.simulation;

import java.net.URL;
import java.util.List;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IExperiment;

public interface ISimulationRunExternalListener {

	void simulationDone(IExperiment experiment,  List<URL> results) throws GeppettoExecutionException;
	
	void simulationFailed(String errorMessage, Exception e, IExperiment experiment);
}
