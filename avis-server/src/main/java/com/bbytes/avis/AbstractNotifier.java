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

	private String queueName = null;
	
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
		final NotificationRequest request =  (NotificationRequest) rabbitOperations.receiveAndConvert(queueName);
		if(request != null) {
			LOG.debug(String.format("Received Request from Queues %s with id %s and notification type %s", queueName, request.getId(), request.getNotificationType()));
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

	/* (non-Javadoc)
	 * @see com.bbytes.avis.messaging.Notifier#setQueueName(java.lang.String)
	 */
	@Override
	public void setQueueName(String queueName) throws AvisException {
		this.queueName = queueName;
	}

	/* (non-Javadoc)
	 * @see com.bbytes.avis.messaging.Notifier#getQueueName()
	 */
	@Override
	public String getQueueName() throws AvisException {
		return this.queueName;
	}

}
