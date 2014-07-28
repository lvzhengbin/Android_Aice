package com.ice.android.common.excepion;
/**
 * UI相关异常对象的封装
 * @author ice
 *
 */
public class ViewException extends AiceException {

	private static final long serialVersionUID = 1L;

	public ViewException() {
		super();
	}

	public ViewException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ViewException(String msg) {
		super(msg);
	}

	public ViewException(Throwable throwable) {
		super(throwable);
	}

}
