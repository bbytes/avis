package com.bbytes.avis.notifications;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bbytes.avis.AbstractNotifier;
import com.bbytes.avis.NotificationData;
import com.bbytes.avis.NotificationRequest;
import com.bbytes.avis.NotificationType;
import com.bbytes.avis.Notifier;
import com.bbytes.avis.data.GcmData;
import com.bbytes.avis.exception.AvisException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.Sender;

/**
 * Google Cloud Messaging Implementation for sending push messages to Android Mobile devices
 *
 * @author Dhanush Gopinath
 *
 * @version 
 */
public class GcmPushNotifierImpl extends AbstractNotifier implements Notifier {

	private Logger LOG = Logger.getLogger(MailNotifierImpl.class);
	
	private String apiKey;
	
	private String senderId;
	
	
	/* (non-Javadoc)
	 * @see com.bbytes.avis.Notifier#sendNotification(com.bbytes.avis.NotificationRequest)
	 */
	@Override
	public void sendNotification(NotificationRequest request) throws AvisException {
		
		if(request == null) {
			LOG.error("Request is null");
			return;
		}
		if(!request.getNotificationType().equals(NotificationType.GCM_NOTIFY)) {
			LOG.error(String.format("Notification of type %s arrived in queue %s, which is expecing type %s",
					request.getNotificationType(), getRequestQueueName(), NotificationType.GCM_NOTIFY));
			return;
		}
		NotificationData<String, Serializable> data = request.getData();
		if(data == null) {
			LOG.error("Notification data is null");
			return;
		}
		
		GcmData gcmData = (GcmData) data.get(NotificationType.GCM_NOTIFY.toString());
		if(gcmData == null) {
			LOG.error("GCM data is null");
			return;
		}
		LOG.debug(String.format("Message recieved to push. Pushing notification to %s devices", gcmData
				.getRegisteredDeviceIds().size()));
		push(gcmData);
	}

	/**
	 * Pushes the GcmData to Google Cloud Messaging Server
	 * @param gcmData
	 */
	private void push(GcmData gcmData) {
		Sender sender = new Sender(apiKey);
		//build the basic message object with the message and activity classname
		Builder messageBuilder = new Message.Builder().
				addData("message", gcmData.getMessage()).
				addData("activityClassName", gcmData.getActivityClassName());
		
		Map<String, String> activityParams = gcmData.getActivityParams();
		//iterate and add the activity parameters if there are any
		if(activityParams!=null && !activityParams.isEmpty()) {
			for(String key : activityParams.keySet()){
				messageBuilder.addData(key, activityParams.get(key));
			}
		}
		//build the message
		Message message = messageBuilder.build();
		try {
			sender.send(message, gcmData.getRegisteredDeviceIds(), 5);
			LOG.debug(String.format("Message pushed"));
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}


	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
}
