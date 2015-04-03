package org.geppetto.core.channel;

import org.geppetto.core.channel.ports.ChannelInPort;
import org.geppetto.core.channel.ports.ChannelOutPort;

import java.util.concurrent.TimeUnit;

public abstract class AChannel {
    protected String id;
    protected ChannelInPort inPort;
    protected ChannelOutPort outPort;

    public AChannel(String id) {
        this.id = id;
        inPort = new ChannelInPort(this);
        outPort = new ChannelOutPort(this);
    }

    public void send(ChannelMessage message) {
        send(message, true);
    }

    public ChannelMessage receive() {
        return receive(true);
    }

    public ChannelInPort getInPort() {
        return inPort;
    }

    public ChannelOutPort getOutPort() {
        return outPort;
    }

    public synchronized boolean isConnected() {
        return inPort.hasOwner() && outPort.hasOwner();
    }

    public String getId() {
        return id;
    }

    abstract public void send(ChannelMessage message, boolean block);
    abstract public ChannelMessage receive(boolean block);
    abstract public ChannelMessage receive(long timeout, TimeUnit timeUnit);
    abstract public int numQueuedMessages();

    @Override
    public String toString() {
        return "AChannel{in='" + inPort.getOwner() + "'; out='" +
                outPort.getOwner() + "'}";
    }
}
