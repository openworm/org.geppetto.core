package org.geppetto.core.data.model;

public interface IView {
	
	final static String EMPTY = "{}";
	
	long getId();
	
	String getView();

	void setView(String string);
	
	
}
