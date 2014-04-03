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
import com.bbytes.avis.data.IrcData;
import com.bbytes.avis.exception.AvisException;

/**
 * Unit test for {@link IrcNotifierImpl}
 * 
 * @author Venugopal Madathil
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/app-context.xml")
public class IrcNotifierImplTest {

	@Autowired
	private Notifier ircNotifier;
	private NotificationRequest request;

	@Before
	public void setup() {
		request = new NotificationRequest();
		request.setId(UUID.randomUUID().toString());
		request.setNotificationType(NotificationType.IRC);
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

	@Test
	public void testSendNotification() throws AvisException {
		ircNotifier.sendNotification(request);
	}

}
