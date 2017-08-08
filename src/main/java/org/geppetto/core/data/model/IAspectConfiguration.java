
package org.geppetto.core.data.model;

import java.util.List;

public interface IAspectConfiguration extends IDataEntity
{

	String getInstance();

	List<String> getWatchedVariables();

	List<? extends IParameter> getModelParameter();

	ISimulatorConfiguration getSimulatorConfiguration();

	void addModelParameter(IParameter modelParameter);

}