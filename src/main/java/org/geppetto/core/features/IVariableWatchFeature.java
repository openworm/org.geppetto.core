package org.geppetto.core.features;

import java.util.List;

import org.geppetto.core.data.model.VariableList;

/**
 * This feature allows the users to watch the values of parameters and variables
 * during the execution.
 * 
 * @author matteocantarelli
 * @author Jesus Martinez (jesus@metacell.us)
 * 
 */
public interface IVariableWatchFeature extends IFeature
{
	VariableList getWatcheableVariables();

	void addWatchVariables(List<String> vars);

	void startWatch();

	void stopWatch();

	void clearWatchVariables();

	boolean isWatching();

	boolean watchListModified();

	void setWatchListModified(boolean modified);
}
