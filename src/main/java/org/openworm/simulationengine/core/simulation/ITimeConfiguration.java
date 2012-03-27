package org.openworm.simulationengine.core.simulation;

public interface ITimeConfiguration {

	
	/**
	 * @return how long is one time step
	 */
	Float getTimeStepLength();
	
	/**
	 * @return for how many time steps a operation will have to be done
	 */
	Integer getTimeSteps();
	
	/**
	 * @return the simple period of the time steps. Has to be a divider of time steps.
	 * This will specify the resolution of returned partial results. 
	 * E.g. TimeSteps=500, SamplePeriod=100 => 5 results are returned
	 * TimeSteps=500, SamplePeriod=1 => all 500 results returned
	 * TimeSteps=500, SamplePeriod=500 => only last result returned
	 */
	Integer getSamplePeriod();
	
	
}