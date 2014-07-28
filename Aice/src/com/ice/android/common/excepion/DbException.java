package com.ice.android.common.excepion;
/**
 * 数据库DB的异常对象封装
 * @author ice
 *
 */
public class DbException extends AiceException {

	private static final long serialVersionUID = 1L;

	public DbException() {
		super();
	}

	public DbException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DbException(String msg) {
		super(msg);
	}

	public DbException(Throwable throwable) {
		super(throwable);
	}

}
