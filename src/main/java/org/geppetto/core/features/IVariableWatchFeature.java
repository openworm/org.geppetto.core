package org.geppetto.core.features;

import java.util.List;

import org.geppetto.core.data.model.VariableList;

/**
 * Interface use for classes that need to implement variable watch
 * 
 * @author Jesus Martinez (jesus@metacell.us)
 *
 */
public interface IVariableWatchFeature extends IFeature{
	VariableList getWatcheableVariables();
	void addWatchVariables(List<String> vars);
	void startWatch();
	void stopWatch();
	void clearWatchVariables();
	boolean isWatching();
}
