# AVIS #

**AVIS** (means notify in French) is a simple multi purpose messaging framework based on Spring and RabbitMQ. Current implementations include notifications via Simple Email and Google Cloud Messaging (GCM). Any new implementations to different notification frameworks/technologies can be added into AVIS.

Avis project is a client-server project, where the client can be used by 3rd party application who wish to send notifications. It has 3 sub-projects under the main parent Maven project. Client and Server sit on either side of a RabbitMQ messaging server. Server looks for notifications in different queues based on the implementations given.

## Server ##

To run the server, just run the `Main.java` file after setting the following mandatory properties in `server.properties` file. These values denote where your RabbitMQ Server is and how you intend to access it.

   	rabbitmq.username=
    rabbitmq.password=
    rabbitmq.ipaddress=
    rabbitmq.port=
    rabbitmq.management.port=
    rabbitmq.vhost=


Then you can add these properties which are required for the default  implemenentation for Email - `MailNotifierImpl.java`  
    
    queue.email.request=
    queue.email.reply=

The main interface on the server side is `Notifier.java` which has methods :

    /**
	 * Sends the appropriate notofication
	 */
	void sendNotification(NotificationRequest request) throws AvisException;

	/**
	 * Consumes the Request from the Queue
	 * @throws AvisException
	 */
	void consumeRequestFromQueue() throws AvisException;
	
	/**
	 * Sends the response back 
	 * 
	 * @param response
	 * @throws AvisException
	 */
	void sendResponse(NotificationResponse response) throws AvisException;

	/**
	 * Sets the queue-name for request
	 * @param queueName
	 * @throws AvisException
	 */
	void setRequestQueueName(String queueName) throws AvisException;

	/**
	 * Returns the queue name
	 * @return
	 * @throws AvisException
	 */
	String getRequestQueueName() throws AvisException;
	
	/**
	 * Sets the queue-name for request
	 * @param queueName
	 * @throws AvisException
	 */
	void setReplyQueueName(String queueName) throws AvisException;

	/**
	 * Returns the queue name
	 * @return
	 * @throws AvisException
	 */
	String getReplyQueueName() throws AvisException;


There is a Abstract Implementation of the common tasks like consuming the request  in the file `AbstractNotifier.java`. This class's main job is to look for messages in the specific queues declared for each notification type and when  the request is received, process it by calling `sendNotification` on a `TaskExecutor` instance.

Newer implementations of `Notifier` interface will be declared in `app-context.xml`.

## Client ##

A client is used by 3rd party applications to send the notifications. The project consists of an interface `AvisClient.java` which has methods: 


	/**
	 * Sends a notification request to the queue name set on the request
	 * 
	 * @param request
	 * @throws AvisClientException
	 */
	public void sendNotification(NotificationRequest request) throws AvisClientException;

	/**
	 * Sends a notification request to the queue name passed, ignoring the queue name set in the
	 * request
	 * @param queueName
	 * @param request
	 * 
	 * @throws AvisClientException
	 */
	public void sendNotification(String queueName, NotificationRequest request) throws AvisClientException;

There is a DefaultAvisClientImpl class which is the implementation of this interface and can be used to send the notification, by auto wiring it once you have added the dependency. Sample code is given below:

	Your Class
	.....
	@Autowired
	private AvisClient avisClient;
	....
	....

	//where you want to send 

	NotificationRequest request = new NotificationRequest();
	request.setId(UUID.randomUUID().toString());
	request.setNotificationType(NotificationType.EMAIL);
	request.setQueueName("NAME OF THE EMAIL QUEUE SERVER IS LISTENING");
	NotificationData<String, Serializable> requestData = new NotificationData<>();
	EmailData data = new EmailData();
	data.setTo(new String[] { "you@you.com" });
	data.setSubject("You got Mail");
	data.setText("I'll be Back");
	data.setSentDate(new Date());
	requestData.put(NotificationType.EMAIL.toString(), data);
	request.setData(requestData);
	avisClient.sendNotification(request);


The classes `NotificationRequest`, `NotificationData` and `EmailData` are part of the Domain project.

## Domain ##

This project contains all the domain specific POJO classes which are used to transferd data from client to server and back. `NotificationRequest` is the class used to send a notification, with the data held in `NotificationData` class. `NotificationData` is an extension of Java `HashMap` and can hold any `Serializable` data against a key of type `String`.


`EmailData` is a POJO class which captures the Email information that needs to be send. This is then set on the `NotificationData` instance before sending the notification.
  