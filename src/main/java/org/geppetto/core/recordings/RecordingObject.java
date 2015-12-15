package org.geppetto.core.recordings;


/**
 * Object class that holds together valus for a variable yet to be created inside an HDF5 file.
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class RecordingObject
{

	private String _variable;
	private String _unit;
	private String _metaType;
	private Object _values;
	private int _dataType;
	private int _valuesLength;
	private int _dataBytes;

	public String getVariable()
	{
		return _variable;
	}

	public void setVariable(String _variable)
	{
		this._variable = _variable;
	}

	public String getUnit()
	{
		return _unit;
	}

	public void setUnit(String _unit)
	{
		this._unit = _unit;
	}

	public String getMetaType()
	{
		return _metaType;
	}

	public void setMetaType(String _metaType)
	{
		this._metaType = _metaType;
	}

	public Object getValues()
	{
		return _values;
	}

	public void setValues(Object _values)
	{
		this._values = _values;
	}

	public void setDataType(int dataType)
	{
		this._dataType = dataType;
	}

	public int getDataType()
	{
		return this._dataType;
	}

	public int getValuesLength()
	{
		return this._valuesLength;
	}

	public void setValuesLenght(int valuesLength)
	{
		this._valuesLength = valuesLength;
	}

	public void setDataBytes(int dataBytes)
	{
		this._dataBytes = dataBytes;
	}

	public int getDataBytes()
	{
		return this._dataBytes;
	}
}
