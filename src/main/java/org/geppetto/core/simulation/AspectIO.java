package org.geppetto.core.simulation;

import org.geppetto.core.channel.ports.ChannelInPort;
import org.geppetto.core.channel.ports.ChannelOutPort;

import java.util.LinkedList;
import java.util.List;

/**
 * Input/output attributes and settings of an aspect
 */
public class AspectIO {
	private String aspectName;
	private List<ChannelInPort> inPorts;
	private List<ChannelOutPort> outPorts;

	public AspectIO(String aspectName) {
		this.aspectName = aspectName;
		inPorts = new LinkedList<>();
		outPorts = new LinkedList<>();
	}

	public String getAspectId() {
		return aspectName;
	}

	public List<ChannelInPort> getInPorts() {
		return inPorts;
	}

	public List<ChannelOutPort> getOutPorts() {
		return outPorts;
	}

	public void addInPort(ChannelInPort inPort) {
		inPorts.add(inPort);
	}

	public void addOutPort(ChannelOutPort outPort) {
		outPorts.add(outPort);
	}
}
