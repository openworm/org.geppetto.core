package org.geppetto.core.model.runtime;

import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class ConnectionNode extends ANode{

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
	
	private String entity;
	private ConnectionType type;
	
	public ConnectionNode(String id) {
		super(id);
	}

	public void setEntityId(String entityId){
		this.entity = entityId;
	}
	
	public String getEntityId(){
		return this.entity;
	}
	
	public void setType(ConnectionType type){
		this.type = type;
	}
	
	public ConnectionType getConnectionType(){
		return this.type;
	}

	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitConnectionNode(this);
	}
}
