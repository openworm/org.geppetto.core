package org.geppetto.core.channel.channels;

import org.geppetto.core.channel.AChannel;
import org.geppetto.core.channel.ChannelMessage;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class SimpleNonBlockingChannel extends AChannel {
    private Queue<ChannelMessage> messageQeueue;

    public SimpleNonBlockingChannel(String id) {
        super(id);
        this.messageQeueue = new LinkedList<>();
    }

    @Override
    public void send(ChannelMessage message, boolean block) {
        messageQeueue.add(message);
    }


    @Override
    public ChannelMessage receive(boolean block) {
        if (block) {
            throwCanNotBlockError();
        }

        return messageQeueue.poll();
    }

    @Override
    public ChannelMessage receive(long timeout, TimeUnit timeUnit) {
        throwCanNotBlockError();
        return null; // for stupid IDEs
    }

    @Override
    public int numQueuedMessages() {
        return messageQeueue.size();
    }

    private void throwCanNotBlockError() {
        throw new RuntimeException("SimpleNonBlockingChannel can not block" +
                " during receive()");
    }
}
