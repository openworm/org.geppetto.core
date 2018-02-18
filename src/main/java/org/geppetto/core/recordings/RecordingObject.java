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
	private int _YValuesLength;
	private int _dataBytes;
	private int _XValuesLength = 1;

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

	public int getYValuesLength()
	{
		return this._YValuesLength;
	}

	public void setYValuesLenght(int valuesLength)
	{
		this._YValuesLength = valuesLength;
	}

	public int getXValuesLength()
	{
		return this._XValuesLength;
	}

	public void setXValuesLenght(int valuesLength)
	{
		this._XValuesLength = valuesLength;
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
