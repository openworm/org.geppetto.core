/**
 * 
 */
package org.geppetto.core.simulation;

/**
 * @author matteocantarelli
 *
 */
public class TimeConfiguration implements ITimeConfiguration {

	private Float _timeStepLength;
	
	private Integer _timeSteps;
	
	private Integer _samplePeriod;
	
	public Float getTimeStepLength() {
		return _timeStepLength;
	}

	public Integer getTimeSteps() {
		return _timeSteps;
	}

	public Integer getSamplePeriod() {
		return _samplePeriod;
	}

	public TimeConfiguration(Float timeStepLength, Integer timeSteps, Integer samplePeriod) {
		super();
		this._timeStepLength = timeStepLength;
		this._timeSteps = timeSteps;
		this._samplePeriod = samplePeriod;
	}

}
