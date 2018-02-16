package org.geppetto.core.utilities;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.data.model.IView;
import org.geppetto.core.data.model.local.LocalGeppettoProject;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Creates serializer and deserializer for LocalView object and LocalGeppettoProject
 * @author jrmartin
 *
 */
public class LocalViewSerializer implements JsonDeserializer<LocalGeppettoProject>,JsonSerializer<IView>
{
	private static Log logger = LogFactory.getLog(LocalViewSerializer.class);
	
	@Override
	public LocalGeppettoProject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				SimpleDateFormat format = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
						Locale.ENGLISH);
				String timeStamp = json.toString().replaceAll("^\"|\"$", "");
				Date date = null;
				try {
					date =format.parse(timeStamp);
				} catch (ParseException e) {
					logger.error(e.getMessage());
				}
				return date; 
			} 
		});
		if(obj.has("view")){
			JsonObject view =  obj.getAsJsonObject("view");
			if(view.has("viewStates")){
				JsonObject jsonViewStates  = view.getAsJsonObject("viewStates");
				view.addProperty("viewStates",jsonViewStates.toString());
			}
			if(view.has("id")){
				JsonPrimitive id  = view.getAsJsonPrimitive("id");
				view.addProperty("id",id.getAsNumber());
			}
		}
		JsonArray experiments = (JsonArray) obj.get("experiments");
		for(int i =0; i<experiments.size();i++){
			JsonObject experiment = (JsonObject) experiments.get(i);

			if(experiment.has("lastModified")){
				long lastModified =  experiment.getAsJsonPrimitive("lastModified").getAsLong();
				String str = this.getDate(lastModified);
				experiment.addProperty("lastModified",str);
			}

			if(experiment.has("creationDate")){
				long lastModified =  experiment.getAsJsonPrimitive("creationDate").getAsLong();
				String str = this.getDate(lastModified);
				experiment.addProperty("creationDate",str);
			}

			if(experiment.has("view")){
				JsonObject view =  experiment.getAsJsonObject("view");
				if(view.has("viewStates")){
					JsonObject jsonViewStates  = view.getAsJsonObject("viewStates");
					view.addProperty("viewStates",jsonViewStates.toString());
				}
				if(view.has("id")){
					JsonPrimitive id  = view.getAsJsonPrimitive("id");
					view.addProperty("id",id.getAsNumber());
				}
			}
		}
		LocalGeppettoProject localProject = gson.create().fromJson(obj.toString(), LocalGeppettoProject.class);

		return localProject;
	}

	@Override
	public JsonElement serialize(IView src, Type typeOfSrc, JsonSerializationContext context) {
		JsonParser parser = new JsonParser();
		JsonObject json = new JsonObject();;
		JsonObject viewStates = null;
		if(src.getView() !=null){
			viewStates = parser.parse(src.getView()).getAsJsonObject();
		}

		json.addProperty("id",src.getId());
		json.add("viewStates",viewStates);
		return json;
	}

	public String getDate(long timeStamp){
		Date dateOriginal = new Date(timeStamp*1000);
		String time = dateOriginal.toString();

		return time;
	}
}
