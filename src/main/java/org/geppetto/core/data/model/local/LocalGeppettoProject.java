

package org.geppetto.core.data.model.local;

import java.util.List;

import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.IView;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LocalGeppettoProject implements IGeppettoProject
{
	private long id;

	private String name;

	private List<LocalExperiment> experiments;

	private LocalPersistedData geppettoModel;

	private long activeExperimentId;

	@JsonIgnore
	private transient boolean volatileProject;

	private List<String> modelReferences;

	private boolean isPublic = false;

	private LocalView view;

	@JsonIgnore
	private transient String baseURL;

	public LocalGeppettoProject(long id, String name, List<LocalExperiment> experiments, LocalPersistedData geppettoModel)
	{
		this.id = id;
		this.name = name;
		this.experiments = experiments;
		this.geppettoModel = geppettoModel;
		this.activeExperimentId = -1;
	}

	@Override
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public List<LocalExperiment> getExperiments()
	{
		return experiments;
	}

	@Override
	public LocalPersistedData getGeppettoModel()
	{
		return geppettoModel;
	}

	@Override
	public boolean equals(Object obj)
	{
		return id == ((LocalGeppettoProject) obj).id;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public boolean isVolatile()
	{
		return this.volatileProject;
	}

	@Override
	public void setVolatile(boolean volatileProject)
	{
		this.volatileProject = volatileProject;
	}

	@Override
	public long getActiveExperimentId()
	{
		return this.activeExperimentId;
	}

	@Override
	public void setActiveExperimentId(long experimentId)
	{
		this.activeExperimentId = experimentId;
	}

	@Override
	public boolean isPublic()
	{
		return this.isPublic;
	}

	public void setPublic(boolean b)
	{
		this.isPublic = b;
	}

	/**
	 * Operation not supported on local experiment
	 */
	@Override
	public void setView(IView view)
	{
		this.view = (LocalView) view;
	}

	@Override
	public IView getView()
	{
		return this.view;
	}

	public void setBaseURL(String baseURL)
	{
		this.baseURL=baseURL;
	}
	
	public String getBaseURL()
	{
		return this.baseURL;
	}
}