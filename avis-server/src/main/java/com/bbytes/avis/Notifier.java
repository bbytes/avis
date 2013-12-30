package com.bbytes.avis;

import com.bbytes.avis.NotificationRequest;
import com.bbytes.avis.exception.AvisException;

/**
 * 
 * 
 * @author Dhanush Gopinath
 * 
 * @version
 */
public interface Notifier {

	/**
	 * Sends the appropriate notofication
	 */
	void sendNotification(NotificationRequest request) throws AvisException;

	/**
	 * Consumes the Request from the Queue
	 * @throws AvisException
	 */
	void consumeRequestFromQueue() throws AvisException;
	
	/**
	 * Sends the response back 
	 * 
	 * @param response
	 * @throws AvisException
	 */
	void sendResponse(NotificationResponse response) throws AvisException;

	/**
	 * Sets the queue-name
	 * @param queueName
	 * @throws AvisException
	 */
	void setQueueName(String queueName) throws AvisException;

	/**
	 * Returns the queue name
	 * @return
	 * @throws AvisException
	 */
	String getQueueName() throws AvisException;

}
