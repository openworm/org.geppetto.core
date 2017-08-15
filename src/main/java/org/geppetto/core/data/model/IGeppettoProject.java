

package org.geppetto.core.data.model;

import java.util.List;

public interface IGeppettoProject extends IDataEntity
{
	String getName();

	void setName(String name);

	List<? extends IExperiment> getExperiments();

	long getActiveExperimentId();

	void setActiveExperimentId(long experimentId);

	IPersistedData getGeppettoModel();

	boolean isVolatile();

	void setVolatile(boolean volatileProject);
	
	boolean isPublic();
	
	void setView(IView view);
	
	IView getView();

	String getBaseURL();
	
	void setBaseURL(String baseURL);
}