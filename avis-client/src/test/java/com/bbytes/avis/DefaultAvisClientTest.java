package com.bbytes.avis;

import java.io.Serializable;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bbytes.avis.data.EmailData;

/**
 * Test class for {@link AvisClient}
 * 
 * @author Dhanush Gopinath
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/avis-client-app-context.xml" })
public class DefaultAvisClientTest {

	@Autowired
	private AvisClient avisClient;
	private NotificationRequest request;

	@Before
	public void setup() {
		request = new NotificationRequest();
		request.setId(UUID.randomUUID().toString());
		request.setNotificationType(NotificationType.EMAIL);
		request.setQueueName("avis.email.queue");
		NotificationData<String, Serializable> requestData = new NotificationData<>();
		EmailData data = new EmailData();
		data.setTo(new String[]{"dhanush@beyondbytes.co.in"});
		data.setSubject("Test Email From Avis");
		data.setText("This is a test email from avis");
		requestData.put(NotificationType.EMAIL.toString(), data);
		request.setData(requestData);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClient() throws AvisClientException  {
		avisClient.sendNotification(request);
	}

	@Test(expected=AvisClientException.class)
	public void testClientNull() throws AvisClientException  {
		avisClient.sendNotification(null);
	}
}
