
package org.geppetto.core.common;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author matteocantarelli
 *
 */
public class JSONUtility
{

	public static Map<String, Object> getAsMap(String json)
	{
		Gson gson = new Gson();
		Type stringStringMap = new TypeToken<Map<String, Object>>(){}.getType();
		Map<String,Object> map = gson.fromJson(json, stringStringMap);
		return map;
	}
}
