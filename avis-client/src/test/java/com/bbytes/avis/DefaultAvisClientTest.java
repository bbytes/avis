package com.bbytes.avis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
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
		request.setQueueName("endure.email.queue");
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
	
	@Test
	public void testClientWithAttachment() throws AvisClientException, IOException  {
		NotificationData<String, Serializable> requestData = request.getData();
		EmailData data = (EmailData) requestData.get(NotificationType.EMAIL.toString());
		data.setHtmlEmail(true);
		String content = "This is the attachment. <h1>Hello World, This is Sparta!! </h1>";
		 
		File file = new File(System.getProperty("java.io.tmpdir")+File.separator+"testAttach"+new Date().getTime()+".txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();

		data.setAttachment(file);

		requestData.put(NotificationType.EMAIL.toString(), data);
		request.setData(requestData);
		
		avisClient.sendNotification(request);
	}
	
	
	@Test(expected=AvisClientException.class)
	public void testClientNull() throws AvisClientException  {
		avisClient.sendNotification(null);
	}
}
