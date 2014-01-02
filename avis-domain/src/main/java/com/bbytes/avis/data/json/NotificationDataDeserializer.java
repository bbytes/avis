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
package com.bbytes.avis.data.json;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.bbytes.avis.NotificationData;

/**
 * Implementation of {@link JsonDeserializer} to handle the creation of {@link NotificationData} objects by
 * deserializing the JSON Data
 * 
 * TODO : Handle serialization when the value is a Map<K,V>
 * TODO : Test Custom Serializer with Custom Data
 * 
 * @author Dhanush Gopinath
 * 
 * @version 0.0.1
 * @since 0.0.1
 */
public class NotificationDataDeserializer extends JsonDeserializer<NotificationData<String, Serializable>> {

	private NotificationDataObjectMapper avisObjectMapper;
	private static final Logger log = Logger.getLogger(NotificationDataDeserializer.class);

	/**
	 * @param avisObjectMapper
	 * @param T
	 */
	public NotificationDataDeserializer(NotificationDataObjectMapper avisObjectMapper) {
		this.avisObjectMapper = avisObjectMapper;
	}

	@Override
	public NotificationData<String, Serializable> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		
		
		NotificationData<String, Serializable> avisData = new NotificationData<String, Serializable>();
		JsonToken t = jp.getCurrentToken();
		if (t == JsonToken.START_OBJECT) {
			t = jp.nextToken();
		}
		for (; t != JsonToken.END_OBJECT; t = jp.nextToken()) {
			String key = jp.getCurrentName();
			t = jp.nextToken();
			// and then others, generally requiring use of @JsonCreator
			switch (t) {
			case VALUE_STRING:
				String strValue = jp.getText();
				avisData.put(key, strValue);
				break;
			case VALUE_NUMBER_INT:
				int intValue = jp.getIntValue();
				avisData.put(key, intValue);
				break;
			case VALUE_NUMBER_FLOAT:
				float flValue = jp.getFloatValue();
				avisData.put(key, flValue);
				break;
			case VALUE_EMBEDDED_OBJECT:
				// return jp.getEmbeddedObject();
				System.out.println(jp.getEmbeddedObject().toString());
				break;
			case VALUE_TRUE:
			case VALUE_FALSE:
				boolean boolValue = jp.getBooleanValue();
				avisData.put(key, boolValue);
				break;
			case START_ARRAY:
				// these only work if there's a (delegating) creator...
				Object arrayObj = deserializeFromArray(jp, ctxt);
				avisData.put(key, (Serializable) arrayObj);
				break;
			case START_OBJECT:
				// handle objects inside the object
				Object customObject = null;
				try {
					customObject = deserializeFromCustomObject(jp, ctxt);
				} catch (ClassNotFoundException e) {
					log.error(e.getMessage(), e);
				}
				avisData.put(key, (Serializable) customObject);
				break;
			case FIELD_NAME:
			case END_OBJECT:
				break;

			}
		}
		return avisData;
	}

	private Object deserializeFromArray(JsonParser jp, DeserializationContext ctxt) throws JsonProcessingException,
			IOException {
		JsonToken t = jp.getCurrentToken();
		if (t == JsonToken.START_ARRAY) {
			t = jp.nextToken();
		}
		List<String> strArray = new ArrayList<String>();
		List<Integer> intArray = new ArrayList<Integer>();
		List<Float> floatArray = new ArrayList<Float>();
		List<Boolean>booleanArray = new ArrayList<Boolean>();
		// for holding array of arrays
		List<Object>objectArray = new ArrayList<Object>();
		List<Serializable> serializableCustomArray = new ArrayList<Serializable>();
		for (; t != JsonToken.END_ARRAY; t = jp.nextToken()) {
			switch (t) {
				case VALUE_STRING:
					strArray.add(jp.getText());
					break;
				case VALUE_NUMBER_INT:
					intArray.add(jp.getIntValue());
					break;
				case VALUE_NUMBER_FLOAT:
					floatArray.add(jp.getFloatValue());
					break;
				case VALUE_TRUE:
				case VALUE_FALSE:
					booleanArray.add(jp.getBooleanValue());
					break;
				case START_ARRAY:
					objectArray.add(deserializeFromArray(jp, ctxt));
					break;
				case START_OBJECT:
					// handle objects inside the array
					Object customObject = null;
					try {
						customObject = deserializeFromCustomObject(jp, ctxt);
					} catch (ClassNotFoundException e) {
						log.error(e.getMessage(), e);
					}
					serializableCustomArray.add((Serializable)customObject);
					break;
				case FIELD_NAME:
				case END_OBJECT:
					break;
				}
		}
		if(!strArray.isEmpty())
			return strArray;
		else if(!intArray.isEmpty())
			return intArray;
		else if (!floatArray.isEmpty())
			return floatArray;
		else if(!booleanArray.isEmpty())
			return booleanArray;
		else if (!objectArray.isEmpty())
			return objectArray;
		else if (!serializableCustomArray.isEmpty())
			return serializableCustomArray;
		return null;
	}

	/**
	 * Deserializes the JSON object to the custom object after fetching the value of the property @class
	 * 
	 * @param jp
	 * @param ctxt
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Object deserializeFromCustomObject(JsonParser jp, DeserializationContext ctxt) throws JsonParseException,
			IOException, ClassNotFoundException {
		JsonToken t = jp.getCurrentToken();
		if (t == JsonToken.START_OBJECT) {
			t = jp.nextToken();
		}
		// The assumption is custom class will have @JsonTypeInfo defined with Property name as
		// @class
		/*
		 * String classKey = jp.getCurrentName(); if(classKey!="@class") { throw new
		 * JsonParseException("Custom Classes should declare a JsonTypeInfo as the first entry",
		 * jp.getCurrentLocation()); } t = jp.nextToken(); String className = jp.getText(); Class<?>
		 * clazz = Class.forName(className); return avisObjectMapper.readValue(jp,
		 * CustomAddress.class);
		 */

		/*
		 * The code commented above should be the proper way of parsing the json. But, when we move
		 * to the next token to get the class name, the JsonParser object advances one level and the
		 * readValue method throws RuntimeException
		 * 
		 * The code given below first converts the parser into a JsonNode and then we fetch the
		 * class name and converts it to the value
		 */

		JsonNode jNode = avisObjectMapper.readTree(jp);
		String className = jNode.get("@class").getTextValue();
		Class<?> clazz = Class.forName(className);
		return avisObjectMapper.treeToValue(jNode, clazz);
	}
}
