
package org.geppetto.core.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Map;

import org.geppetto.core.data.model.IExperiment;
import org.geppetto.core.data.model.IGeppettoProject;
import org.geppetto.core.data.model.IView;
import org.geppetto.core.data.model.local.LocalGeppettoProject;
import org.geppetto.core.data.model.local.LocalView;
import org.geppetto.core.utilities.LocalViewSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * @author matteocantarelli
 *
 */
public class FindLocalProjectsVisitor<T extends IGeppettoProject> extends SimpleFileVisitor<Path>
{

	private Map<Long, T> projects;
	private Class<T> type;

	public FindLocalProjectsVisitor(Map<Long, T> projects, Class<T> type)
	{
		this.projects = projects;
		this.type = type;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException
	{
		if(file.toString().endsWith(".json"))
		{
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
			{
				@Override
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
				{
					return new Date(json.getAsJsonPrimitive().getAsLong());
				}

			});
			BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
			
			builder.registerTypeAdapter(LocalGeppettoProject.class, new LocalViewSerializer());
			T project = builder.create().fromJson(reader, type);
			if(project.getView() == null)
			{
				project.setView(new LocalView(0, null));
			}
			for(IExperiment e : project.getExperiments())
			{
				e.setParentProject(project);
			}
			projects.put(project.getId(), project);
		}
		return FileVisitResult.CONTINUE;
	}

}
