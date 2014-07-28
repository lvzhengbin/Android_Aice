package com.ice.android.common.excepion;
/**
 * 
 * @author ice
 *
 */
public class AiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AiceException(){
		super();
	}
	
	public AiceException(String msg){
		super(msg);
	}

	public AiceException(Throwable throwable) {
		super(throwable);
	}
	
	public AiceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
	
}
