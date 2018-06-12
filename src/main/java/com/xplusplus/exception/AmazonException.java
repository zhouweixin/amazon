package com.xplusplus.exception;

/**
 * AmazonException amazon异常
 * 
 * @author zhouweixin
 *
 */
public class AmazonException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Integer code;

	public AmazonException(EnumException exceptionsEnum) {
		super(exceptionsEnum.getMessage());

		this.code = exceptionsEnum.getCode();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
