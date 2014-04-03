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

import com.bbytes.avis.data.IrcData;

/**
 * 
 * Test class for {@link AvisClient}
 * 
 * @author Venugopal Madathil
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/avis-client-app-context.xml" })
public class IrcAvisClientTest {

	@Autowired
	private AvisClient avisClient;
	private NotificationRequest request;

	@Before
	public void setup() {
		request = new NotificationRequest();
		request.setId(UUID.randomUUID().toString());
		request.setNotificationType(NotificationType.IRC);
		request.setQueueName("endure.irc.queue");
		NotificationData<String, Serializable> requestData = new NotificationData<>();
		IrcData data = new IrcData();
		data.setName("false9striker");
		data.setNickname("false9striker");
		data.setChannel("#pircbot");
		data.setIrcUrl("irc.freenode.net");
		data.setPort("6665");
		data.setMessage("Test IRC Message");
		requestData.put(NotificationType.IRC.toString(), data);
		request.setData(requestData);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClient() throws AvisClientException {
		avisClient.sendNotification(request);
	}

	@Test(expected = AvisClientException.class)
	public void testClientNull() throws AvisClientException {
		avisClient.sendNotification(null);
	}
}
