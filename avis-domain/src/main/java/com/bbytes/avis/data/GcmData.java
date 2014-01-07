package com.bbytes.avis.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * POJO class for encapsulating Data required for sending push notifications to via GCM
 * 
 * @author Dhanush Gopinath
 * 
 * @version
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class GcmData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6158773456267027249L;

	/**
	 * Id's of the registered devices to whom notifications are sent
	 */
	private List<String> registeredDeviceIds;

	/**
	 * The class name of the activity to start, when we click the notification
	 */
	private String activityClassName;

	/**
	 * Any parameters that should be
	 */
	private Map<String, String> activityParams;

	/**
	 * Message in the notification
	 */
	private String message;

	/**
	 * @return the registeredDeviceIds
	 */
	public List<String> getRegisteredDeviceIds() {
		return registeredDeviceIds;
	}

	/**
	 * @param registeredDeviceIds
	 *            the registeredDeviceIds to set
	 */
	public void setRegisteredDeviceIds(List<String> registeredDeviceIds) {
		this.registeredDeviceIds = registeredDeviceIds;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the activityClassName
	 */
	public String getActivityClassName() {
		return activityClassName;
	}

	/**
	 * @param activityClassName
	 *            the activityClassName to set
	 */
	public void setActivityClassName(String activityClassName) {
		this.activityClassName = activityClassName;
	}

	/**
	 * @return the activityParams
	 */
	public Map<String, String> getActivityParams() {
		return activityParams;
	}

	/**
	 * @param activityParams
	 *            the activityParams to set
	 */
	public void setActivityParams(Map<String, String> activityParams) {
		this.activityParams = activityParams;
	}
}
