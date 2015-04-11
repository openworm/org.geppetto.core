package org.geppetto.core.features;

import java.util.List;

/**
 * Interface use for classes that need to implement variable watch
 * 
 * @author Jesus Martinez (jesus@metacell.us)
 *
 */
public interface IVariableWatchFeature extends IFeature{
	void addWatchVariables(List<String> vars);
	void startWatch();
	void stopWatch();
	void clearWatchVariables();
	boolean isWatching();
	boolean watchListModified();
	void setWatchListModified(boolean modified);
}
