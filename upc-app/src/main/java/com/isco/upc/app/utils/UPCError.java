package com.isco.upc.app.utils;

import javax.ws.rs.core.Response;

public class UPCError {
	private String code;
	private String description;
	private String severity;
	private Response.Status status;
	private String module;

	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public Response.Status getStatus() {
		return status;
	}

	public void setStatus(Response.Status status) {
		this.status = status;
	}

	public void setStatus(String status) {
		this.status = Response.Status.valueOf(status);
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Override
	public String toString() {
		return "StoreError [code=" + code + ", description=" + description + ", severity=" + severity + ", status="
				+ status + ", module=" + module + "]";
	}
}
