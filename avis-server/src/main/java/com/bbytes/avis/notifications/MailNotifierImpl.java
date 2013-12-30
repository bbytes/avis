package com.bbytes.avis.notifications;

import com.bbytes.avis.AbstractNotifier;
import com.bbytes.avis.Notifier;
import com.bbytes.avis.NotificationRequest;
import com.bbytes.avis.NotificationResponse;
import com.bbytes.avis.exception.AvisException;

/**
 * {@link Notifier} implementation for sending an E-Mail
 *
 * @author Dhanush Gopinath
 *
 * @version 
 */
public class MailNotifierImpl extends AbstractNotifier implements Notifier {

	/* (non-Javadoc)
	 * @see com.bbytes.avis.Notifier#sendNotification(com.bbytes.avis.Request)
	 */
	@Override
	public void sendNotification(NotificationRequest request) throws AvisException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendResponse(NotificationResponse response) throws AvisException {
		// TODO Auto-generated method stub
		
	}

}
