package com.bbytes.avis;

import org.springframework.amqp.core.MessageListener;

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
	void sendNotification(NotificationRequest request) throws AvisClientException;

	/**
	 * Sends a notification request to the queue name passed, ignoring the queue name set in the
	 * request
	 * 
	 * @param queueName
	 * @param request
	 * 
	 * @throws AvisClientException
	 */
	void sendNotification(String queueName, NotificationRequest request) throws AvisClientException;

	/**
	 * Returns the name of the messaging server this client pushes the data to.
	 * 
	 * @return
	 */
	String getMessageQueueServerHost();

	/**
	 * Returns the port of the messaging server this client pushes the data to.
	 * 
	 * @return
	 */
	int getMessageQueueServerPort();

	/**
	 * Adds a message listener to the reply queue to get the message replied by Avis Server
	 * 
	 * @param listener
	 * @param replyQueueName
	 * 
	 * @throws AvisClientException
	 */
	void addMessageListener(MessageListener listener, String replyQueueName) throws AvisClientException;
	
	/**
	 * Removes the message listener for the queue
	 * 
	 * @param replyQueueName
	 * @throws AvisClientException
	 */
	void removeMessageListener(String replyQueueName) throws AvisClientException;
}
