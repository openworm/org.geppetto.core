package org.geppetto.core.datasources;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.model.Query;
import org.geppetto.model.QueryMatchingCriteria;
import org.geppetto.model.types.Type;
import org.geppetto.model.util.ModelUtility;
import org.geppetto.model.variables.Variable;
import org.hamcrest.core.IsSame;

public class QueryChecker
{

	/**
	 * @param query
	 *            the query to check
	 * @param variable
	 *            the types to be checked against
	 * @return true if any of the query criteria match against the types
	 */
	public static boolean check(Query query, Variable variable)
	{
		List<Type> allTypes = ModelUtility.getAllTypesOf(variable);
		if(!query.getMatchingCriteria().isEmpty())
		{
			for(QueryMatchingCriteria criteria : query.getMatchingCriteria())
			{
				boolean matchCriteria = true;
				for(Type typeToMatch : criteria.getType())
				{
					if(!allTypes.contains(typeToMatch))
					{
						// let's check the superTypes
						boolean isSuperType = false;
						for(Type type : allTypes)
						{
							if(type.extendsType(typeToMatch))
							{
								isSuperType = true;
							}
						}
						if(!isSuperType)
						{
							matchCriteria = false;
							break;
						}
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
