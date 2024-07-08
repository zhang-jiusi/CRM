package com.bjpowernode.crm.commons.domain;

/**
 * 用于返回 json 相关信息
 *
 * @date: 2024/7/6 22:17
 */
public class ReturnObject {

	/* 处理信息是否成功的标志，1 成功，0 失败 */
	private String code;
	/* 提示信息 */
	private String message;
	/* 一些其他的返回信息 */
	private Object retData;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getRetData() {
		return retData;
	}

	public void setRetData(Object retData) {
		this.retData = retData;
	}
}