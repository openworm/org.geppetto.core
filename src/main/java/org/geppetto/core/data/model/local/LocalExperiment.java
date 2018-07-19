

package org.geppetto.core.data.model.local;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geppetto.core.data.model.ExperimentStatus;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.ISimulationResult;
import org.geppetto.core.data.model.IView;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "parentProject" })
public class LocalExperiment implements IExperiment {
	private long id;

	private List<LocalAspectConfiguration> aspectConfigurations;

	private String name;

	private String description;

	private Date creationDate;

	private Date lastModified;

	private ExperimentStatus status;

	private List<LocalSimulationResult> simulationResults = new ArrayList<LocalSimulationResult>();

	private Date startDate;

	private Date endDate;

	@JsonIgnore
	private transient IGeppettoProject parentProject;

	private String script;

	private Date lastRan;

	private LocalView view;

	public LocalExperiment(long id, List<LocalAspectConfiguration> aspectConfigurations, String name,
			String description, Date creationDate, Date lastModified, ExperimentStatus status,
			List<LocalSimulationResult> simulationResults, Date startDate, Date endDate, IGeppettoProject project) {
		this.id = id;
		this.aspectConfigurations = aspectConfigurations;
		this.name = name;
		this.description = description;
		this.creationDate = creationDate;
		this.lastModified = lastModified;
		this.status = status;
		this.simulationResults = simulationResults;
		this.startDate = startDate;
		this.endDate = endDate;
		this.parentProject = project;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public List<LocalAspectConfiguration> getAspectConfigurations() {
		return aspectConfigurations;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public Date getLastModified() {
		return lastModified;
	}

	public void addSimulationResult(ISimulationResult result) {
		if (result instanceof LocalSimulationResult) {
			simulationResults.add((LocalSimulationResult) result);
		}
	}

	@Override
	public List<LocalSimulationResult> getSimulationResults() {
		return simulationResults;
	}

	@Override
	public ExperimentStatus getStatus() {
		return status;
	}

	@Override
	public synchronized void setStatus(ExperimentStatus status) {
		this.status = status;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}

	@Override
	@JsonIgnore
	public IGeppettoProject getParentProject() {
		return parentProject;
	}

	@Override
	@JsonIgnore
	public void setParentProject(IGeppettoProject project) {
		this.parentProject = project;

	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void updateLastModified() {
		this.lastModified = new Date();
	}

	@Override
	public String getScript() {
		return script;
	}

	@Override
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * Operation not supported on local experiment
	 */
	@Override
	public String getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Operation not supported on local experiment
	 */
	@Override
	public void setDetails(String details) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStartDate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateEndDate() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPublic() {
		return false;
	}

	@Override
	public IView getView() {
		return this.view;
	}

	@Override
	public void setView(IView view) {
		this.view = (LocalView) view;
	}
}