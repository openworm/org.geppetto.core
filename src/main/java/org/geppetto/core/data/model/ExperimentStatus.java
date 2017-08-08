

package org.geppetto.core.data.model;

public enum ExperimentStatus
{
	DESIGN("DESIGN"), QUEUED("QUEUED"), RUNNING("RUNNING"), ERROR("ERROR"), COMPLETED("COMPLETED"), DELETED("DELETED"), CANCELED("CANCELED");

	private ExperimentStatus(final String text)
	{
		this.text = text;
	}

	private final String text;

	@Override
	public String toString()
	{
		return text;
	}
}
