package com.bbytes.avis.notifications;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bbytes.avis.AbstractNotifier;
import com.bbytes.avis.NotificationData;
import com.bbytes.avis.NotificationRequest;
import com.bbytes.avis.NotificationResponse;
import com.bbytes.avis.NotificationType;
import com.bbytes.avis.Notifier;
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
	private JavaMailSender mailSender;

	public MailNotifierImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bbytes.avis.Notifier#sendNotification(com.bbytes.avis.Request)
	 */
	@Override
	public void sendNotification(NotificationRequest request) throws AvisException {
		if (request == null) {
			LOG.error("Request is null");
			return;
		}
		if (!request.getNotificationType().equals(NotificationType.EMAIL)) {
			LOG.error(String.format("Notification of type %s arrived in queue %s, which is expecing type %s",
					request.getNotificationType(), getRequestQueueName(), NotificationType.EMAIL));
			return;
		}
		NotificationData<String, Serializable> data = request.getData();
		if (data == null) {
			LOG.error("Notification data is null");
			return;
		}

		EmailData emailData = (EmailData) data.get(NotificationType.EMAIL.toString());
		if (emailData == null) {
			LOG.error("Email data is null");
			return;
		}
		try {
			if (emailData.isHtmlEmail()) {
				sendHtmlEmail(emailData);
			} else {
				sendEmail(emailData);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new AvisException(e);
		}

		LOG.debug("Notification sent via " + NotificationType.EMAIL);
		NotificationResponse response = new NotificationResponse();
		NotificationData<String, Serializable> responseData = new NotificationData<>();
		responseData.put("status", "Email Sent");
		response.setId(request.getId());
		response.setResult(responseData);
		sendResponse(response);
	}

	/**
	 * Sends the email
	 * 
	 * @param emailData
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void sendHtmlEmail(final EmailData emailData) throws MailException, MessagingException, IOException {
		LOG.debug("Beginning to send an HTML email to " + emailData.getTo());

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(emailData.getTo());
			if (emailData.getFrom() != null) {
				helper.setFrom(emailData.getFrom());
			}
			if (emailData.getCc() != null && emailData.getCc().length > 0) {
				helper.setCc(emailData.getCc());
			}
			if (emailData.getBcc() != null && emailData.getBcc().length > 0) {
				helper.setBcc(emailData.getBcc());
			}

			helper.setSentDate(new Date());
			helper.setSubject(emailData.getSubject());
			helper.setText(emailData.getText(), emailData.getText());
			if (emailData.getAttachment() != null) {
				InputStream stream = emailData.getAttachment();
				ByteArrayResource resource = new ByteArrayResource(IOUtils.toByteArray(stream));
				helper.addAttachment(emailData.getAttachmentFileName(), resource);
			}
			mailSender.send(mimeMessage);
		} catch (MailException e) {
			LOG.error(e.getMessage());
			throw e;
		} catch (MessagingException e) {
			LOG.error(e.getMessage());
			throw e;
		} catch (IOException e) {
			LOG.error(e.getMessage());
			throw e;
		}

		LOG.debug("HTML email sent");
	}

	/**
	 * Sends the email
	 * 
	 * @param emailData
	 */
	private void sendEmail(EmailData emailData) throws MailException {
		LOG.debug("Beginning to send an simple email to " + emailData.getTo());
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailData.getFrom());
		message.setTo(emailData.getTo());
		message.setCc(emailData.getCc());
		message.setBcc(emailData.getBcc());
		message.setSentDate(new Date());
		message.setSubject(emailData.getSubject());
		message.setText(emailData.getText());
		try {
			mailSender.send(message);
		} catch (MailException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		LOG.debug("Simple email sent");
	}
}
