package com.bbytes.avis.notifications;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import com.bbytes.avis.data.GcmData;
import com.bbytes.avis.exception.AvisException;

/**
 * Unit test for {@link GcmPushNotifierImpl} - Since we are dealing with dummy reg ids we will not
 * be able to pass this test unless we expect and error. Only way to test it is by giving a proper reg id
 * 
 * @author Dhanush Gopinath
 * 
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/app-context.xml")
public class GcmPushNotifierImplTest {

	@Autowired
	private Notifier gcmPushNotifier;
	private NotificationRequest request;

	@Before
	public void setup() {
		request = new NotificationRequest();
		request.setId(UUID.randomUUID().toString());
		request.setNotificationType(NotificationType.GCM_NOTIFY);
		NotificationData<String, Serializable> requestData = new NotificationData<>();
		GcmData data = new GcmData();
		data.setMessage("Test Me");
		List<String> regIds = new ArrayList<>();
		regIds.add("T1");
		data.setRegisteredDeviceIds(regIds);
		requestData.put(NotificationType.GCM_NOTIFY.toString(), data);
		request.setData(requestData);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSendNotification() throws AvisException {
		gcmPushNotifier.sendNotification(request);
	}
}
