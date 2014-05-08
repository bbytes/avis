package com.bbytes.avis.notifications;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
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
 * Unit test for {@link MailNotifierImpl}
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
	public void setup() {
		request = new NotificationRequest();
		request.setId(UUID.randomUUID().toString());
		request.setNotificationType(NotificationType.EMAIL);
		NotificationData<String, Serializable> requestData = new NotificationData<>();
		EmailData data = new EmailData();
		data.setFrom("endure@endure.com");
		data.setTo(new String[] { "dhanush@beyondbytes.co.in" });
		data.setSubject("Test Email From Avis Server: MailNotifier");
		data.setText("This is a test email from avis");
		requestData.put(NotificationType.EMAIL.toString(), data);
		request.setData(requestData);
	}

	@Test
	public void testSendNotification() throws AvisException {
		mailNotifier.sendNotification(request);
	}

	@Test
	public void testSendNotificationHTML() throws AvisException, IOException {
		NotificationData<String, Serializable> requestData = request.getData();
		EmailData data = (EmailData) requestData.get(NotificationType.EMAIL.toString());
		data.setHtmlEmail(true);
		data.setAttachmentFileName("attach.txt");
		File file = createTempFile();
		byte[] bFile = convertFileToByteArray(file);
		data.setAttachment(bFile);
		data.setText("<h1>Hello World, This is Sparta!! </h1>");
		requestData.put(NotificationType.EMAIL.toString(), data);
		request.setData(requestData);
		mailNotifier.sendNotification(request);
	}

	protected byte[] convertFileToByteArray(File file) throws FileNotFoundException, IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		return IOUtils.toByteArray(fileInputStream);
	}

	protected File createTempFile() throws IOException {
		String content = "This is the attachment. <h1>Hello World, This is Sparta!! </h1>";

		File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "testAttach"
				+ new Date().getTime() + ".txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		return file;
	}
}
