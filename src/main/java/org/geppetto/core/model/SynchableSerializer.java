
package org.geppetto.core.model;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.emfjson.jackson.databind.ser.EObjectSerializer;
import org.emfjson.jackson.utils.EObjects;
import org.geppetto.model.GeppettoPackage;
import org.geppetto.model.ISynchable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.emfjson.jackson.databind.property.EObjectPropertyMap.Builder;

/**
 * This serializer makes it so that only objects for which synched==false are serialized. Once an object is serialized then the attribute synched is set to true.
 * 
 * TODO: There is an assumption underlying this logic that, as soon as something is serialized, it can be considered synched while in fact for the system to be more robust we should considered things
 * synched only once we receive some kind of ack from the frontend.
 * 
 * @author matteocantarelli
 *
 */
public class SynchableSerializer extends StdSerializer<EObject>
{
	protected SynchableSerializer(Class<EObject> t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.emfjson.jackson.databind.ser.EObjectSerializer#serialize(org.eclipse.emf.ecore.EObject, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(EObject object, JsonGenerator jg, SerializerProvider provider) throws IOException
	{
		ISynchable synchable = (ISynchable) object;
		if(object.eContainer() != null && EObjects.isContainmentProxy(provider, object.eContainer(), object))
		{
		    this.serialize(object.eContainer(), jg, provider);
			return;
		}

		if(synchable.isSynched())
		{
			jg.writeStartObject();
			jg.writeBooleanField(GeppettoPackage.Literals.ISYNCHABLE__SYNCHED.getName(), synchable.isSynched());
			jg.writeEndObject();
			return;
		}
		else
		{
			super.serializeWithType(object, jg, provider, null);
			synchable.setSynched(true);
		}

	}

}
