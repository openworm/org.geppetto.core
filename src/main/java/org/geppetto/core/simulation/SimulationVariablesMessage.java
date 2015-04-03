package org.geppetto.core.simulation;

import org.geppetto.core.channel.ChannelMessage;
import org.geppetto.core.model.values.AValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimulationVariablesMessage extends ChannelMessage {
	private Map<String, AValue> _varNamesMap = new HashMap<>();

	public void addVariable(String name, AValue value) {
		_varNamesMap.put(name, value);
	}

	public Set<String> getVariableNames() {
		return _varNamesMap.keySet();
	}

	public AValue getVariableValue(String name) {
		return _varNamesMap.get(name);
	}

	public void clear() {
		_varNamesMap.clear();
	}

	public int getNumberOfVarsInMessage() {
		return _varNamesMap.size();
	}
}
