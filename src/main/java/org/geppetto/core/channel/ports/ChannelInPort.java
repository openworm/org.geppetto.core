package org.geppetto.core.channel.ports;

import org.geppetto.core.channel.AChannel;
import org.geppetto.core.channel.AChannelPort;
import org.geppetto.core.channel.ChannelMessage;

import java.util.concurrent.TimeUnit;

public class ChannelInPort extends AChannelPort {
    public ChannelInPort(AChannel parentChannel) {
        super(parentChannel);
    }

    public ChannelMessage receive() {
        return parentChannel.receive();
    }

    public ChannelMessage receive(boolean block) {
        return parentChannel.receive(block);
    }

    public ChannelMessage receive(long timeout, TimeUnit timeUnit) {
        return parentChannel.receive(timeout, timeUnit);
    }

    public int numAwaitingMessages() {
        return parentChannel.numQueuedMessages();
    }

    @Override
    public String getPeer() {
        return parentChannel.getOutPort().getPeer();
    }

    @Override
    public String toString() {
        return "(InPort: " + getOwner() + ")";
    }
}
