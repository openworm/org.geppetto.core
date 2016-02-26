package org.geppetto.core.datasources;

import java.util.List;

import org.geppetto.model.Query;
import org.geppetto.model.QueryMatchingCriteria;
import org.geppetto.model.types.Type;

public class QueryChecker
{

	/**
	 * @param query
	 *            the query to check
	 * @param types
	 *            the types to be checked against
	 * @return true if any of the query criteria match against the types
	 */
	public static boolean check(Query query, List<Type> types)
	{
		if(!query.getMatchingCriteria().isEmpty())
		{
			for(QueryMatchingCriteria criteria : query.getMatchingCriteria())
			{
				boolean matchCriteria = true;
				for(Type typeToMatch : criteria.getType())
				{
					if(!types.contains(typeToMatch))
					{
						matchCriteria = false;
						break;
					}
				}
				if(matchCriteria)
				{
					return true;
				}
			}
			return false;
		}
		return true;
	}

}
