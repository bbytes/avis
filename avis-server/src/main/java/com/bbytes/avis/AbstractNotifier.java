package com.bbytes.avis;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.bbytes.avis.exception.AvisException;

/**
 * Abstract implementation of {@link Notifier} interface
 *
 * @author Dhanush Gopinath
 *
 * @version 
 */
public abstract class AbstractNotifier implements Notifier {

	private String requestQueueName = null;
	
	private String replyQueueName = null;
	
	private Logger LOG = Logger.getLogger(AbstractNotifier.class);
	
	@Autowired
	protected RabbitOperations rabbitOperations;
	
	@Autowired
	protected TaskExecutor executor;

	/* (non-Javadoc)
	 * @see com.bbytes.avis.messaging.Notifier#consumeRequestFromQueue()
	 */
	@Override
	@Async
	@Scheduled(fixedDelay = 500)
	public void consumeRequestFromQueue() throws AvisException {
		final NotificationRequest request =  (NotificationRequest) rabbitOperations.receiveAndConvert(requestQueueName);
		if(request != null) {
			LOG.debug(String.format("Received Request from Queue %s with id %s and notification type %s", requestQueueName, request.getId(), request.getNotificationType()));
			if(!request.getQueueName().equals(requestQueueName)) {
				String message = String.format("Received Request from Queue %s is not matching with the queue name set on request %s", requestQueueName, request.getQueueName());
				LOG.error(message);
				throw new AvisException(message);
			}
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						LOG.debug("Sending notification");
						sendNotification(request);
					} catch (AvisException e) {
						LOG.error(e.getMessage());
					}
				}
			});
		}
	}

	@Override
	public void setRequestQueueName(String queueName) throws AvisException {
		this.requestQueueName = queueName;
	}

	@Override
	public String getRequestQueueName() throws AvisException {
		return this.requestQueueName;
	}

	@Override
	public void setReplyQueueName(String queueName) throws AvisException {
		this.replyQueueName = queueName;
	}

	@Override
	public String getReplyQueueName() throws AvisException {
		return this.replyQueueName;
	}

	/* (non-Javadoc)
	 * @see com.bbytes.avis.Notifier#sendResponse(com.bbytes.avis.NotificationResponse)
	 */
	@Override
	public void sendResponse(NotificationResponse response) throws AvisException {
		rabbitOperations.convertAndSend(replyQueueName, response);
	}
}
