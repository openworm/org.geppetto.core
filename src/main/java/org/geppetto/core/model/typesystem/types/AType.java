package org.geppetto.core.model.typesystem.types;

import org.geppetto.core.model.typesystem.ANode;
import org.geppetto.core.model.typesystem.aspect.IAspect;
import org.geppetto.core.model.typesystem.values.IValue;

public abstract class AType extends ANode implements IType
{

	protected IAspect aspect = null;

	protected String name = null;

	protected IValue defaultValue = null;

	protected IType baseType = null;

	protected IVisualType visualType = null;

	public AType()
	{
		super();
	}

	/**
	 * @param aspect
	 * @param name
	 */
	public AType(IAspect aspect, String name)
	{
		super();
		this.aspect = aspect;
		this.name = name;
	}

	/**
	 * @param aspect
	 * @param name
	 * @param baseType
	 */
	public AType(IAspect aspect, String name, IType baseType)
	{
		this(aspect, name);
		this.baseType = baseType;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public IAspect getAspect()
	{
		return aspect;
	}

	@Override
	public IValue getDefaultValue()
	{
		return defaultValue;
	}

	@Override
	public IType getBaseType()
	{
		return baseType;
	}

	@Override
	public IVisualType getVisualType()
	{
		return visualType;
	}

	@Override
	public void setBaseType(IType baseType)
	{
		this.baseType = baseType;
	}

}