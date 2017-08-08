

package org.geppetto.core.utilities;

import java.util.HashMap;
import java.util.Map;

public class StringSplitter
{
	public static Map<String, String> keyValueSplit(String str, String separator)
	{
		String pattern = separator + "|=";
		String[] tokens = str.split(pattern);
		Map<String, String> map = new HashMap<>();
		for(int i = 0; i < tokens.length - 1;)
			map.put(tokens[i++], tokens[i++]);
		return map;
	}
}
