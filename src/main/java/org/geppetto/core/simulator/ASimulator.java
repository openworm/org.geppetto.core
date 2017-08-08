

package org.geppetto.core.simulator;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.manager.Scope;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.services.AService;
import org.geppetto.core.simulation.ISimulatorCallbackListener;
import org.geppetto.model.DomainModel;
import org.geppetto.model.ExperimentState;
import org.geppetto.model.values.Pointer;

/**
 * @author matteocantarelli
 * 
 */
public abstract class ASimulator extends AService implements ISimulator
{

	private ISimulatorCallbackListener listener;

	private boolean initialized = false;

	private String timeStepUnit = "ms";

	private double runtime;

	protected DomainModel model;

	protected IAspectConfiguration aspectConfiguration;

	protected ExperimentState experimentState;

	protected GeppettoModelAccess geppettoModelAccess;

	private IExperiment experiment;


	public ASimulator()
	{
		scope = Scope.RUN;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#initialize(org.geppetto.core.model.IModel, org.geppetto.core.simulation.ISimulatorCallbackListener)
	 */
	@Override
	public void initialize(DomainModel model, IAspectConfiguration aspectConfiguration, ExperimentState experimentState, ISimulatorCallbackListener listener, GeppettoModelAccess modelAccess)
			throws GeppettoInitializationException, GeppettoExecutionException
	{
		setListener(listener);
		this.model = model;
		this.aspectConfiguration = aspectConfiguration;
		this.runtime = 0;
		this.initialized = true;
		this.experimentState = experimentState;
		this.geppettoModelAccess = modelAccess;
	}

	/**
	 * @return
	 */
	public ISimulatorCallbackListener getListener()
	{
		return listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geppetto.core.simulator.ISimulator#isInitialized()
	 */
	public boolean isInitialized()
	{
		return initialized;
	}

	@Override
	public void setInitialized(boolean initialized)
	{
		this.initialized = initialized;
	}

	/**
	 * @param listener
	 */
	public void setListener(ISimulatorCallbackListener listener)
	{
		this.listener = listener;
	}

	/**
	 * @param timeStepUnit
	 */
	public void setTimeStepUnit(String timeStepUnit)
	{
		this.timeStepUnit = timeStepUnit;
	}

	/**
	 * @param timestep
	 * @param aspect
	 */
	public void advanceTimeStep(double timestep, Pointer pointer)
	{
		runtime += timestep;
	}

	@Override
	public double getTime()
	{
		return runtime;
	}

	@Override
	public String getTimeStepUnit()
	{
		return timeStepUnit;
	}



	/**
	 * @param experiment
	 */
	public void setExperiment(IExperiment experiment)
	{
		this.experiment = experiment;
	}

	/**
	 * @return
	 */
	public IExperiment getExperiment()
	{
		return this.experiment;
	}
}
