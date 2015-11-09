package org.geppetto.core.library;

import org.geppetto.core.model.geppettomodel.GeppettoModel;
import org.geppetto.core.model.typesystem.IAspect;
import org.geppetto.core.model.typesystem.types.IType;

public interface ILibraryManager
{

	void init(GeppettoModel model);
	
	IType getTypeByName(IAspect aspect, String name) throws GeppettoTypeException;;
	
	IType getTypeByName(String aspectId, String name) throws GeppettoTypeException;;
}
