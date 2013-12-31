package com.bbytes.avis;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link AvisClient} - Use this to send requests
 * 
 * @author Dhanush Gopinath
 * 
 * @version
 */
@Component
public class DefaultAvisClientImpl implements AvisClient {

	private Logger LOG = Logger.getLogger(DefaultAvisClientImpl.class);

	@Autowired
	private RabbitOperations rabbitOperations;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bbytes.avis.AvisClient#sendNotification(com.bbytes.avis.NotificationRequest)
	 */
	@Override
	public void sendNotification(NotificationRequest request) throws AvisClientException {
		if (request == null) {
			String message = "Request is Null";
			LOG.error(message);
			throw new AvisClientException(message);
		}
		if (request.getQueueName() == null) {
			String message = "Queue name is not set on the request.";
			LOG.error(message);
			throw new AvisClientException(message);
		}
		rabbitOperations.convertAndSend(request.getQueueName(), request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bbytes.avis.AvisClient#sendNotification(com.bbytes.avis.NotificationRequest,
	 * java.lang.String)
	 */
	@Override
	public void sendNotification(String queueName, NotificationRequest request) throws AvisClientException {
		if (request == null) {
			String message = "Request is Null";
			LOG.error(message);
			throw new AvisClientException(message);
		}
		if (queueName == null || queueName.isEmpty()) {
			String message = "Queue name is not set";
			LOG.error(message);
			throw new AvisClientException(message);
		}
		rabbitOperations.convertAndSend(queueName, request);
	}
}
