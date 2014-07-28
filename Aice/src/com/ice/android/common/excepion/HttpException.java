package com.ice.android.common.excepion;
/**
 * 网络请求产生的相关异常对象封装
 * @author ice
 *
 */
public class HttpException extends AiceException {

	private static final long serialVersionUID = 1L;

	public HttpException() {
		super();
	}

	public HttpException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public HttpException(String msg) {
		super(msg);
	}

	public HttpException(Throwable throwable) {
		super(throwable);
	}

}
