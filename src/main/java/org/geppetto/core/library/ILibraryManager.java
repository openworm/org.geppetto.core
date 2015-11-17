package org.geppetto.core.library;


public interface ILibraryManager
{

	IGeppettoLibrary getLibrary(String modelInterpreterId) throws GeppettoTypeException;
	
	
}
