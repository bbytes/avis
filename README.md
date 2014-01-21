# AVIS #

**AVIS** (means notify in French) is a simple multi purpose messaging framework based on Spring and RabbitMQ. Current implementations include notifications via Simple Email and Google Cloud Messaging (GCM). Any new implementations to different notification frameworks/technologies can be added into AVIS.

Avis project is a client-server project, where the client can be used by 3rd party application who wish to send notifications. It has 3 sub-projects under the main parent Maven project. Client and Server sit on either side of a RabbitMQ messaging server. Server looks for notifications in different queues based on the implementations given.

**Current Stable Release Version  - 0.0.1**

**Current Development Version  - 0.0.2-SNAPSHOT**

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

### Running Maven Release Plugin ###

Running the maven release plugin, requires the user to have ssh_keys in his/her system as we have to access the Github Repo. Please see the [link](https://help.github.com/articles/generating-ssh-keys) for generating ssh-keys.

To upgrade the version and cut a release build - run the following commands on command prompt

     mvn release:prepare -DdryRun=true -Dargument="-DskipTests=true" 

This will walk you through a dry run and you can view the temporary POMs it creates to verify you did everything correctly. It is not mandatory to do this, if you are confident. It also skips the test execution
    
    mvn release:clean release:prepare -Dargument="-DskipTests=true"

This will actually commit a tag with non-snapshot versions of your project and also increment all your versions to the next release. So for example, if your current version is 0.0.1-SNAPSHOT, it will commit a tag with 0.0.1 version, and update the trunk POMs with 0.0.2-SNAPSHOT. Of course when using the prepare goal you are prompted for the release numbers and next release numbers. Please note, you need to add `git` command in the path to run this.

Finally deploy and perform the release by running the command :
    
    mvn release:perform -Darguments="-DskipTests=true -Dmaven.javadoc.skip=true"

For this command to run successfully your Maven settings.xml should have the authentication information to loginto Nexus. Please add the `<servers>` entry in the xml

    <servers>
      <server>
       	<id>releases</id>
       	<username>USERNAME_FOR_NEXUS</username>
       	<password>PASSWORD_FOR_NEXUS</password>
      </server>
    </servers>

***Issues with running in Windows***

When running the above command in Windows, it hands after the git push. This is because of the ssh key passphrase not being entered. As a work around to this do the following on a command prompt.

Run the command from `Git/bin` folder

    ssh-agent

 After you run it, it outputs some environment variables that you need to set. For example:

    SSH_AUTH_SOCK=/tmp/ssh-LhiYjP7924/agent.7924; export SSH_AUTH_SOCK;
    SSH_AGENT_PID=2792; export SSH_AGENT_PID;
    echo Agent pid 2792;


So, you need to place these in your environment:

    C:\> set SSH_AUTH_SOCK=/tmp/ssh-LhiYjP7924/agent.7924
    C:\> set SSH_AGENT_PID=2792

Then, you will need to add a passphrase for your particular key file. So, the command is:

    ssh-add

This will prompt to enter the passphrase which you used to create ssh_keys

    Enter passphrase for /c/Users/<windows_username>/.ssh/id_rsa:

Once entered you should see an Identity added message. Now, the agent knows the passphrase so that you will not be prompted to specify it anymore.

If you have multiple instances of command prompts, make sure that each command prompt has the appropriate `SSH_AUTH_SOCK` and `SSH_AGENT_PID` environment variables set. You can validate that this is working by running a command like `ssh -v git@github.com` and if you DO NOT get prompted for a passphrase, it is working!

Note that when you logoff, the `ssh-agent` will shut down and so when you log back in or restart your computer, you will need to repeat these steps.