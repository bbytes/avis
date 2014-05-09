package com.bbytes.avis;

import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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

	@Autowired
	private CachingConnectionFactory rabbitConnectionFactory;

	/**
	 * A Map to hold the listeners that are added by client
	 */
	private Map<String, SimpleMessageListenerContainer> messageListenerMaps = new Hashtable<>();

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
		LOG.debug(String.format("Sending Notification to the queue %s on rabbitmq server %s on port %s ",
				request.getQueueName(), rabbitConnectionFactory.getHost(), rabbitConnectionFactory.getPort()));
		try {
			rabbitOperations.convertAndSend(request.getQueueName(), request);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new AvisClientException(e.getMessage());
		}

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
		try {
			rabbitOperations.convertAndSend(queueName, request);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new AvisClientException(e.getMessage());
		}
	}

	@Override
	public String getMessageQueueServerHost() {
		return rabbitConnectionFactory.getHost();
	}

	@Override
	public int getMessageQueueServerPort() {
		return rabbitConnectionFactory.getPort();
	}

	@Override
	public void addMessageListener(final AvisResponseListener listener, String replyQueueName)
			throws AvisClientException {
		if (!messageListenerMaps.containsKey(replyQueueName)) {
			SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
			container.setConnectionFactory(rabbitConnectionFactory);
			container.setQueueNames(replyQueueName);
			container.setMessageListener(new MessageListenerImpl(listener));
			container.start();
			messageListenerMaps.put(replyQueueName, container);
		} else {
			SimpleMessageListenerContainer container = messageListenerMaps.get(replyQueueName);
			if (!container.isRunning()) {
				container.start();
			}
		}
	}

	@Override
	public void removeMessageListener(String replyQueueName) throws AvisClientException {
		if (messageListenerMaps.containsKey(replyQueueName)) {
			SimpleMessageListenerContainer container = messageListenerMaps.remove(replyQueueName);
			container.shutdown();
		}
	}

	/**
	 * Default implementation of {@link MessageListener} which will convert the {@link Message}
	 * object to {@link NotificationResponse}
	 * 
	 * @author Dhanush Gopinath
	 * 
	 * @version
	 */
	private class MessageListenerImpl implements MessageListener {

		private AvisResponseListener responseListener;

		public MessageListenerImpl(AvisResponseListener listener) {
			super();
			this.responseListener = listener;
		}

		@Override
		public void onMessage(Message message) {
			NotificationResponse response = (NotificationResponse) ((RabbitTemplate) rabbitOperations)
					.getMessageConverter().fromMessage(message);
			try {
				responseListener.handleResponse(response);
			} catch (AvisClientException e) {
				LOG.error(e.getMessage());
			}
		}
	}
}
