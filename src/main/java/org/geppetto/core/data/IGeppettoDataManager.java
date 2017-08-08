

package org.geppetto.core.data;

import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.IAspectConfiguration;
import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.IParameter;
import org.geppetto.core.data.model.IPersistedData;
import org.geppetto.core.data.model.ISimulationResult;
import org.geppetto.core.data.model.ISimulatorConfiguration;
import org.geppetto.core.data.model.IUser;
import org.geppetto.core.data.model.IUserGroup;
import org.geppetto.core.data.model.IView;
import org.geppetto.core.data.model.PersistedDataType;
import org.geppetto.core.data.model.ResultsFormat;
import org.geppetto.core.data.model.UserPrivileges;

import com.google.gson.Gson;

/**
 * This interface contains methods to deal with the persisted data model of Geppetto this includes fetching stuff from the database (or whatever data source) and adding stuff to the database
 * 
 * @author dandromerecschi
 * @author matteocantarelli
 * 
 */
public interface IGeppettoDataManager
{
	String getName();

	boolean isDefault();

	IUser getUserByLogin(String login);
	
	IUserGroup getUserGroupById(long id);

	IGeppettoProject getGeppettoProjectById(long id);

	List<? extends IUser> getAllUsers();

	Collection<? extends IGeppettoProject> getAllGeppettoProjects();

	Collection<? extends IGeppettoProject> getGeppettoProjectsForUser(String login);

	IGeppettoProject getProjectFromJson(Gson gson, String json);

	IGeppettoProject getProjectFromJson(Gson gson, Reader json, String baseURL);

	List<? extends IExperiment> getExperimentsForProject(long projectId);

	ISimulationResult newSimulationResult(String parameterPath, IPersistedData results, ResultsFormat format);

	void addWatchedVariable(IAspectConfiguration found, String instancePath);

	IPersistedData newPersistedData(URL url, PersistedDataType type);

	IParameter newParameter(String parameterPath, String value);

	IExperiment newExperiment(String name, String description, IGeppettoProject project);
	
	IView newView(String view, IGeppettoProject project);
	
	IView newView(String view, IExperiment experiment);

	IUser newUser(String name, String password, boolean persistent, IUserGroup group);
	
	IUserGroup newUserGroup(String name, List<UserPrivileges> privileges, long spaceAllowance, long timeAllowance);

	IUser updateUser(IUser user, String password);

	IAspectConfiguration newAspectConfiguration(IExperiment experiment, String instancePath, ISimulatorConfiguration simulatorConfiguration);

	ISimulatorConfiguration newSimulatorConfiguration(String simulator, String conversionService, float timestep, float length,Map<String, String> parameters);

	void addGeppettoProject(IGeppettoProject project, IUser user) throws GeppettoExecutionException;

	void makeGeppettoProjectPublic(long projectId, boolean isPublic) throws GeppettoExecutionException;

	Object deleteGeppettoProject(long id, IUser user);

	Object deleteExperiment(IExperiment experiment);

	void clearWatchedVariables(IAspectConfiguration aspectConfig);

	void saveEntity(Object entity);

	void saveEntity(IExperiment entity);

	void saveEntity(IGeppettoProject entity);

	IExperiment cloneExperiment(String name, String description,
			IGeppettoProject project, IExperiment originalExperiment);

		
}