package org.geppetto.core.channel.ports;

import org.geppetto.core.channel.AChannel;
import org.geppetto.core.channel.AChannelPort;
import org.geppetto.core.channel.ChannelMessage;

public class ChannelOutPort extends AChannelPort {
    public ChannelOutPort(AChannel parentChannel) {
        super(parentChannel);
    }

    public void send(ChannelMessage message) {
        parentChannel.send(message);
    }

    public void send(ChannelMessage message, boolean block) {
        parentChannel.send(message, block);
    }

    @Override
    public String getPeer() {
        return parentChannel.getInPort().getOwner();
    }

    @Override
    public String toString() {
        return "(OutPort: " + getOwner() + ")";
    }
}
