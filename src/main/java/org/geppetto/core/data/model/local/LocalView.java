package org.geppetto.core.data.model.local;

import org.geppetto.core.data.model.IView;

/**
 * In case of local projects the view is handled by the client for persistence in local storage 
 * but this class can provide the initial view state if a view is present in the static local sample projects.
 * 
 * @author giovanniidili
 *
 */
public class LocalView implements IView {

	private long id;
	
	private String viewString;
	
	public LocalView(long id, String view)
	{
		this.id = id;
		this.viewString = view;
	}
	
	@Override
	public String getView() {
		return this.viewString;
	}

	@Override
	public void setView(String view) {
		this.viewString = view;
	}

	@Override
	public long getId() {
		return this.id;
	}

}
