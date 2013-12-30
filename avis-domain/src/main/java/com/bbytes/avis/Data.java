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
import java.util.HashMap;

/**
 * A {@link Serializable} wrapper around {@link HashMap} which will be used to pass the custom data.
 *
 * @author Dhanush Gopinath
 *
 * @version 
 */
@SuppressWarnings("hiding")
public class Data<String, T extends Serializable> extends HashMap<String, Serializable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
