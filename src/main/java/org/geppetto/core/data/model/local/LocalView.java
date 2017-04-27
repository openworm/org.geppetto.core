package org.geppetto.core.data.model.local;

import java.io.Serializable;

import org.geppetto.core.data.model.IView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * In case of local projects the view is handled by the client for persistence in local storage 
 * but this class can provide the initial view state if a view is present in the static local sample projects.
 * 
 * @author giovanniidili
 *
 */
public class LocalView implements Serializable, IView {

	private static final long serialVersionUID = 1L;
	private long id;
	
	private JsonObject viewStates;
	
	public LocalView(long id, String view)
	{
		this.id = id;
		JsonParser parser = new JsonParser();
		if(view !=null){
			JsonObject o = parser.parse(view).getAsJsonObject();
			this.viewStates = o;
		}
		this.viewStates = null;
	}
	
	@Override
	public JsonObject getView() {
		return this.viewStates;
	}

	@Override
	public void setView(String view) {
		JsonParser parser = new JsonParser();
		if(view!=null){
		JsonObject o = parser.parse(view).getAsJsonObject();
		this.viewStates = o;
		}else{
			this.viewStates = null;
		}
	}

	@Override
	public long getId() {
		return this.id;
	}

}
