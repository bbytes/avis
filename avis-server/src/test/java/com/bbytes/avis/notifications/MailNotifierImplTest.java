package com.bbytes.avis.notifications;

import java.io.Serializable;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bbytes.avis.NotificationData;
import com.bbytes.avis.NotificationRequest;
import com.bbytes.avis.NotificationType;
import com.bbytes.avis.Notifier;
import com.bbytes.avis.data.EmailData;
import com.bbytes.avis.exception.AvisException;


/**
 * 
 *
 * @author Dhanush Gopinath
 *
 * @version 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/app-context.xml")
public class MailNotifierImplTest {

	@Autowired
	private Notifier mailNotifier;
	private NotificationRequest request;
	
	@Before
	public void setup()  {
		request = new NotificationRequest();
		request.setId(UUID.randomUUID().toString());
		request.setNotificationType(NotificationType.EMAIL);
		NotificationData<String, Serializable> requestData = new NotificationData<>();
		EmailData data = new EmailData();
		data.setTo("dhanush@beyondbytes.co.in");
		data.setSubject("Test Email From Avis");
		data.setText("This is a test email from avis" );
		requestData.put(NotificationType.EMAIL.toString(), data);
		request.setData(requestData);
	}
	
	@Test
	public void testSendNotification() throws AvisException {
		mailNotifier.sendNotification(request );
	}
}
