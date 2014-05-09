package com.bbytes.avis.notifications;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.bbytes.avis.AbstractNotifier;
import com.bbytes.avis.NotificationData;
import com.bbytes.avis.NotificationRequest;
import com.bbytes.avis.NotificationResponse;
import com.bbytes.avis.NotificationType;
import com.bbytes.avis.Notifier;
import com.bbytes.avis.exception.AvisException;

/**
 * A test implementation to check if Avis is running or not
 *
 * @author Dhanush Gopinath
 *
 * @version 
 */
public class TestNotifierImpl extends AbstractNotifier implements Notifier {

	private Logger LOG = Logger.getLogger(TestNotifierImpl.class);
	
	/* (non-Javadoc)
	 * @see com.bbytes.avis.Notifier#sendNotification(com.bbytes.avis.NotificationRequest)
	 */
	@Override
	public void sendNotification(NotificationRequest request) throws AvisException {
		LOG.info("Sample Notification received " + request.getId());
		NotificationData<String, Serializable> data = request.getData();
		String message = (String) data.get(NotificationType.TEST.toString());
		LOG.info("Received message :: " + message);
		
		NotificationResponse response = new NotificationResponse();
		NotificationData<String, Serializable> responseData = new NotificationData<>();
		responseData.put("status", "message received");
		response.setId(request.getId());
		response.setResult(responseData);
		sendResponse(response);
	}

}
