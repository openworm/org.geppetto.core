package org.geppetto.core.data.model;

import com.google.gson.JsonObject;

public interface IView {
	
	final static String EMPTY = "{}";
	
	long getId();
	
	JsonObject getView();

	void setView(String string);
	
	
}
