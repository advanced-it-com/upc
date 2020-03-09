package com.isco.upc.common.exceptions;

import com.isco.upc.common.enums.ErrorCode;

public class UPCException  extends RuntimeException {
	private static final long serialVersionUID = -6127508569926524106L;
	private ErrorCode code;

	public UPCException(ErrorCode code) {
		this.code = code;
	}

	public UPCException(ErrorCode code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public UPCException( ErrorCode code, String msg, Throwable t) {
		super(msg, t);
		this.code = code;
	}
	
	public UPCException(String msg, Throwable t) {
		super(msg, t);
		
	}
	
	public UPCException(String msg) {
		super(msg);
		
	}

	public ErrorCode getCode() {
		return code;
	}

}
