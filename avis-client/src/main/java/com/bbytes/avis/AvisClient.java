package com.bbytes.avis;

/**
 * Interface for the avis client
 * 
 * @author Dhanush Gopinath
 * 
 * @version
 */
public interface AvisClient {

	/**
	 * Sends a notification request to the queue name set on the request
	 * 
	 * @param request
	 * @throws AvisClientException
	 */
	public void sendNotification(NotificationRequest request) throws AvisClientException;

	/**
	 * Sends a notification request to the queue name passed, ignoring the queue name set in the
	 * request
	 * @param queueName
	 * @param request
	 * 
	 * @throws AvisClientException
	 */
	public void sendNotification(String queueName, NotificationRequest request) throws AvisClientException;
}
