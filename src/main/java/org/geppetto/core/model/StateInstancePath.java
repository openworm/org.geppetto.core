/**
 * 
 */
package org.geppetto.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author matteocantarelli
 * This class represents an instance of a state in Geppetto
 */
public class StateInstancePath
{

	private List<String> _instancePath;

	public StateInstancePath(String instancePath)
	{
		super();
		this._instancePath = tokenize(instancePath);
	}
	
	public StateInstancePath(List<String> instancePath)
	{
		super();
		this._instancePath = instancePath;
	}

	public List<String> getInstancePath()
	{
		return _instancePath;
	}
	
	private List<String> tokenize(String instancePathString)
	{
		StringTokenizer st=new StringTokenizer(instancePathString,".");
		List<String> instancePath=new ArrayList<String>();
		while(st.hasMoreTokens())
		{
			instancePath.add(st.nextToken());
		}
		return instancePath;
	}

	@Override
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		for(String s:_instancePath)
		{
			sb.append(s+".");
		}
		return sb.toString().substring(0, sb.toString().length()-1);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_instancePath == null) ? 0 : _instancePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		StateInstancePath other = (StateInstancePath) obj;
		if(_instancePath == null)
		{
			if(other._instancePath != null) return false;
		}
		else if(!_instancePath.equals(other._instancePath)) return false;
		return true;
	}
	

	
	
}
