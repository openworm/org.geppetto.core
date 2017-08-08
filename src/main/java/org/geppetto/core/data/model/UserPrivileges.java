

package org.geppetto.core.data.model;

public enum UserPrivileges {
	READ_PROJECT("READ_PROJECT"), 
	WRITE_PROJECT("WRITE_PROJECT"),
	RUN_EXPERIMENT("RUN_EXPERIMENT"),
	DROPBOX_INTEGRATION("DROPBOX_INTEGRATION"),
	DOWNLOAD("DOWNLOAD"),
	ADMIN("ADMIN");
	
	private final String text;

    /**
     * @param text
     */
    private UserPrivileges(final String text) {
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
