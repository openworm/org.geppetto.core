
package org.geppetto.core.data;

import org.springframework.http.HttpStatus;

public class JsonRequestException
{
	private String message;

	private HttpStatus status = HttpStatus.OK;

	public JsonRequestException(String message, HttpStatus status)
	{
		this.message = message;
		this.status = status;
	}

	public String getMessage()
	{
		return message;
	}

	public HttpStatus getStatus()
	{
		return status;
	}

}
