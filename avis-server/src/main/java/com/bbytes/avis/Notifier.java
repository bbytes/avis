package com.bbytes.avis;

import com.bbytes.avis.NotificationRequest;
import com.bbytes.avis.exception.AvisException;

/**
 * The interface for implementing a type of notification
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
	 * Sets the queue-name for request
	 * @param queueName
	 * @throws AvisException
	 */
	void setRequestQueueName(String queueName) throws AvisException;

	/**
	 * Returns the queue name
	 * @return
	 * @throws AvisException
	 */
	String getRequestQueueName() throws AvisException;
	
	/**
	 * Sets the queue-name for request
	 * @param queueName
	 * @throws AvisException
	 */
	void setReplyQueueName(String queueName) throws AvisException;

	/**
	 * Returns the queue name
	 * @return
	 * @throws AvisException
	 */
	String getReplyQueueName() throws AvisException;

}
