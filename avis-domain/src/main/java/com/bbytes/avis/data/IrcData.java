package com.bbytes.avis.data;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.bbytes.avis.NotificationData;
import com.bbytes.avis.NotificationRequest;

/**
 * 
 * Defines IRC Chat data that should be put n the {@link NotificationRequest}.
 * An instance of this should be added to the {@link NotificationData} as value.
 * An IRC Chat would be sent to the channel provided.
 * 
 * @author Venugopal Madathil
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class IrcData implements Serializable {

	private String name;

	private String nickname;

	private String channel;

	private String port;

	private String ircUrl;
	
	private String message;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2681359695579563261L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getIrcUrl() {
		return ircUrl;
	}

	public void setIrcUrl(String ircUrl) {
		this.ircUrl = ircUrl;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	
	

}
