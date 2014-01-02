package com.bbytes.avis.notifications;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.bbytes.avis.AbstractNotifier;
import com.bbytes.avis.NotificationData;
import com.bbytes.avis.NotificationType;
import com.bbytes.avis.Notifier;
import com.bbytes.avis.NotificationRequest;
import com.bbytes.avis.NotificationResponse;
import com.bbytes.avis.data.EmailData;
import com.bbytes.avis.exception.AvisException;

/**
 * {@link Notifier} implementation for sending an E-Mail
 *
 * @author Dhanush Gopinath
 *
 * @version 
 */
public class MailNotifierImpl extends AbstractNotifier implements Notifier {

	private Logger LOG = Logger.getLogger(MailNotifierImpl.class);
	
	@Autowired
	private MailSender mailSender;
	
	
	
	public MailNotifierImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.bbytes.avis.Notifier#sendNotification(com.bbytes.avis.Request)
	 */
	@Override
	public void sendNotification(NotificationRequest request) throws AvisException {
		if(request == null) {
			LOG.error("Request is null");
			return;
		}
		if(!request.getNotificationType().equals(NotificationType.EMAIL)) {
			LOG.error(String.format("Notification of type %s arrived in queue %s, which is expecing type %s",
					request.getNotificationType(), getRequestQueueName(), NotificationType.EMAIL));
			return;
		}
		NotificationData<String, Serializable> data = request.getData();
		if(data == null) {
			LOG.error("Notification data is null");
			return;
		}
		
		EmailData emailData = (EmailData) data.get(NotificationType.EMAIL.toString());
		if(emailData == null) {
			LOG.error("Email data is null");
			return;
		}
		sendEmail(emailData);
		LOG.debug("Notification sent via "+NotificationType.EMAIL);
		NotificationResponse response = new NotificationResponse();
		NotificationData<String, Serializable> responseData = new NotificationData<>();
		responseData.put("status", "Email Sent");
		response.setId(request.getId());
		response.setResult(responseData);
		sendResponse(response);
	}

	/**
	 * Sends the email
	 * @param emailData
	 */
	private void sendEmail(EmailData emailData) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailData.getFrom());
		message.setTo(emailData.getTo());
		message.setCc(emailData.getCc());
		message.setBcc(emailData.getBcc());
		message.setSentDate(new Date());
		message.setSubject(emailData.getSubject());
		message.setText(emailData.getText());
		mailSender.send(message);
	}
}
