package com.bbytes.avis.notifications;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.jibble.pircbot.IrcException;

import com.bbytes.avis.AbstractNotifier;
import com.bbytes.avis.NotificationData;
import com.bbytes.avis.NotificationRequest;
import com.bbytes.avis.NotificationType;
import com.bbytes.avis.Notifier;
import com.bbytes.avis.data.IrcData;
import com.bbytes.avis.exception.AvisException;

/**
 * 
 * {@link Notifier} implementation for sending an irc chat to given channel
 * 
 * @author Venugopal Madathil
 * 
 */
public class IrcNotifierImpl extends AbstractNotifier implements Notifier {

	private Logger LOG = Logger.getLogger(IrcNotifierImpl.class);

	IrcBot ircBot;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bbytes.avis.Notifier#sendNotification(com.bbytes.avis.NotificationRequest
	 * )
	 */
	@Override
	public void sendNotification(NotificationRequest request) throws AvisException {
		if (request == null) {
			LOG.error("Request is null");
			return;
		}
		NotificationData<String, Serializable> data = request.getData();
		if (data == null) {
			LOG.error("Notification data is null");
			return;
		}
		IrcData ircData = (IrcData) data.get(NotificationType.IRC.toString());
		if (ircData == null) {
			LOG.error("IRC data is null");
			return;
		}
		LOG.debug("Message recieved to send chat ");
		sendChat(ircData);

	}

	/**
	 * Sends chat message to given channel. Stays for 10 seconds and then
	 * disconnects from the channel.
	 * 
	 * @param ircData
	 */
	private void sendChat(IrcData ircData) {
		if (ircData != null && ircData.getIrcUrl() != null && ircData.getPort() != null && ircData.getName() != null) {
			ircBot = new IrcBot(ircData.getName());
			ircBot.setVerbose(true);
			try {
				ircBot.connect(ircData.getIrcUrl(), Integer.parseInt(ircData.getPort()));
				ircBot.changeNick(ircData.getNickname());
				ircBot.joinChannel(ircData.getChannel());
				ircBot.sendMessage(ircData.getChannel(), ircData.getMessage());
				while (ircBot.isConnected()) {
					Thread.sleep(10000);
					break;
				}
				ircBot.disconnect();
				LOG.debug("Disconnected from IRC Channel " + ircData.getChannel());
			} catch (NumberFormatException | IOException | IrcException | InterruptedException e) {
				LOG.error("EXCEPTION:" + e.getMessage());
			}

		} else {
			LOG.error("Could not send irc chat.");
		}
	}

}
