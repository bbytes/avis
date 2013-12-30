/*
 * Copyright (C) 2013 The Avis Open Source Project 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.bbytes.avis;

import java.io.Serializable;

/**
 * Domain class for making a request to the Avis
 * 
 * @author Dhanush Gopinath
 * @version 0.0.1
 * 
 */
public class NotificationRequest implements Serializable{

	private static final long serialVersionUID = -6789865933652864865L;

	protected String id;
	protected String queueName;
	protected NotificationData<String,Serializable> data;
	protected NotificationType notificationType;

	
	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public NotificationData<String,Serializable> getData() {
		return data;
	}

	public void setData(NotificationData<String,Serializable> data) {
		this.data = data;
	}
}
