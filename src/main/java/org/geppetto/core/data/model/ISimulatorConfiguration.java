
package org.geppetto.core.data.model;

import java.util.Map;

public interface ISimulatorConfiguration extends IDataEntity
{

	String getSimulatorId();

	String getConversionServiceId();

	float getTimestep();

	float getLength();

	void setLength(float length);

	void setTimestep(float timestep);

	void setSimulatorId(String simulatorId);

	void setConversionServiceId(String conversionServiceId);

	Map<String, String> getParameters();

	void setParameters(Map<String, String> parameters);
}
