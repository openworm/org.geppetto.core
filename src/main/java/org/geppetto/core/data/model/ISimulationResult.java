
package org.geppetto.core.data.model;

public interface ISimulationResult extends IDataEntity
{

	IPersistedData getResult();

	ResultsFormat getFormat();

	String getSimulatedInstance();

}