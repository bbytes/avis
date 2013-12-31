package com.bbytes.avis;

/**
 * Exception thrown at client side
 *
 * @author Dhanush Gopinath
 *
 * @version 
 */
public class AvisClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9218323073475870723L;

	/**
	 * 
	 */
	public AvisClientException() {
	}

	/**
	 * @param message
	 */
	public AvisClientException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public AvisClientException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AvisClientException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AvisClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
