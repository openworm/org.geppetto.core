package org.geppetto.core.model.runtime;

/**
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class ConnectionNode {

	public enum ConnectionType
	{
		FROM("from"),
		TO("to"),
		UNDIRECTED("undirected");
		
		private final String text;

	    /**
	     * @param text
	     */
	    private ConnectionType(final String text) {
	        this.text = text;
	    }


	    /* (non-Javadoc)
	     * @see java.lang.Enum#toString()
	     */
	    @Override
	    public String toString() {
	        return text;
	    }
	}
	
	private EntityNode entity;
	
	public void setEntity(EntityNode entity){
		this.entity = entity;
	}
	
	public EntityNode getEntity(){
		return this.entity;
	}
}
