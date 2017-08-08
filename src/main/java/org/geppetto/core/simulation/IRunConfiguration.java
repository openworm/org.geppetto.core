

package org.geppetto.core.simulation;

@Deprecated
public interface IRunConfiguration
{

	/**
	 * @return time step NOTE: this is ms TODO: need a way to store the unit of measurement
	 */
	Float getTimeStepLength();

	/**
	 * @return number of time steps to run
	 */
	Integer getTimeSteps();

	/**
	 * @return the sample period of the time steps. Has to be a divider of time steps. This will specify the resolution of returned partial results. Examples: TimeSteps=500, SamplePeriod=100 => 5
	 *         results are returned TimeSteps=500, SamplePeriod=1 => all 500 results returned TimeSteps=500, SamplePeriod=500 => only last result returned
	 */
	Integer getSamplePeriod();

}
