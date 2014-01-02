/*
 * Copyright (C) 2013 The Avis Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bbytes.avis.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.module.SimpleModule;

import com.bbytes.avis.NotificationData;

/**
 * The {@link AvisObjectMapper} extends {@link ObjectMapper} to provide its own serializer and
 * deserializer for the {@link NotificationData} object that is passed along with {@link NotificationRequest} and
 * {@link NotificationResponse}
 * 
 * @see AvisDataDeserializer
 * @see AvisDataSerializer
 * 
 * @author Dhanush Gopinath
 * 
 * @version 0.0.1
 * @since 0.0.1
 */
public class AvisObjectMapper extends ObjectMapper {

	/**
	 * Default Constructor
	 */
	public AvisObjectMapper() {
		super();
		// set the serialization config object without the feature FAIL_ON_EMPTY_BEANS so that the
		// serialization will pass when there is empty objects with out serializers
		setSerializationConfig(getSerializationConfig().without(Feature.FAIL_ON_EMPTY_BEANS));
		SimpleModule avisModule = new SimpleModule("avis", new Version(0, 0, 1, "SNAPSHOT"));
		avisModule.addSerializer(new AvisDataSerializer(this));
		avisModule.addDeserializer(NotificationData.class, new AvisDataDeserializer(this));
		registerModule(avisModule);
	}
}
