package com.bbytes.avis;

/**
 * Interface for a listener for receiving the response messages from the server. An instance of this
 * should be added to {@link AvisClient} using
 * {@link AvisClient#addMessageListener(AvisResponseListener, String)} method
 * 
 * @author Dhanush Gopinath
 * 
 * @version
 * @since 0.0.12
 */
public interface AvisResponseListener {

	/**
	 * The method which will receive the notification response
	 * 
	 * @param response
	 * @throws AvisClientException
	 */
	void handleResponse(NotificationResponse response) throws AvisClientException;
}
