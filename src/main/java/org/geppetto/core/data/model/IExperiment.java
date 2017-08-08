

package org.geppetto.core.data.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IExperiment extends IDataEntity
{

	List<? extends IAspectConfiguration> getAspectConfigurations();

	String getName();
	
	String getDetails();

	String getDescription();

	Date getCreationDate();

	Date getLastModified();
	
	ExperimentStatus getStatus();

	void setName(String name);

	void setDescription(String description);

	void setStatus(ExperimentStatus status);
	
	void setDetails(String details);

	void addSimulationResult(ISimulationResult result);

	String getScript();

	void setScript(String script);

	List<? extends ISimulationResult> getSimulationResults();

	Date getStartDate();

	Date getEndDate();

	@JsonIgnore
	IGeppettoProject getParentProject();

	@JsonIgnore
	void setParentProject(IGeppettoProject project);

	void updateLastModified();
	
	void updateStartDate();
	
	void updateEndDate();
	
	boolean isPublic();
	
	void setView(IView view);
	
	IView getView();
}