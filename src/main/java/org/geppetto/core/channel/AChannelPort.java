package org.geppetto.core.channel;

public abstract class AChannelPort {
    private static final String NO_OWNER = "NO_OWNER";
    protected AChannel parentChannel;
    protected String owner;

    public AChannelPort(AChannel parentChannel) {
        this.parentChannel = parentChannel;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public void clearOwner() {
        setOwner(null);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        if (owner == null) {
            return NO_OWNER;
        }

        return owner;
    }

    public abstract String getPeer();
}
