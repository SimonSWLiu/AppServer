package com.onemenu.server.util.awsnotification.tools;

/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.onemenu.server.util.StringUtil;

public class MessageGenerator {

	/*
	 * This message is delivered if a platform specific message is not specified
	 * for the end point. It must be set. It is received by the device as the
	 * value of the key "default".
	 */
	public static final String defaultMessage = "This is the default message";

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static enum Platform {
		// Apple Push Notification Service
		APNS,
		// Sandbox version of Apple Push Notification Service
		APNS_SANDBOX,
		// Amazon Device Messaging
		ADM,
		// Google Cloud Messaging
		GCM,
		// Baidu CloudMessaging Service
		BAIDU,
		// Windows Notification Service
		WNS,
		// Microsoft Push Notificaion Service
		MPNS;
	}

	public static String jsonify(Object message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw (RuntimeException) e;
		}
	}

	private static Map<String, Object> getData(String message,Map<String,Object> customData) {
		Map<String, Object> payload =  (customData==null)?new HashMap<String, Object>():customData;
		payload.put("message", message);
		return payload;
	}

	public static String getAppleMessage(String message,String sound,int badge,Map<String,Object> customData) {
		Map<String, Object> appleMessageMap = new HashMap<String, Object>();
		
		Map<String, Object> appMessageMap = (customData==null)?new HashMap<String, Object>():customData;
		appMessageMap.put("alert", message);
		appMessageMap.put("badge", badge==0?9:badge);
		appMessageMap.put("sound", StringUtil.isEmpty(sound)?"default":sound);
		appleMessageMap.put("aps", appMessageMap);
		return jsonify(appleMessageMap);
	}

	public static String getKindleMessage(String message,Map<String,Object> customData) {
		Map<String, Object> kindleMessageMap = new HashMap<String, Object>();
		kindleMessageMap.put("data", getData(message,customData));
		kindleMessageMap.put("consolidationKey", "Welcome");
		kindleMessageMap.put("expiresAfter", 1000);
		return jsonify(kindleMessageMap);
	}

	public static String getSampleAndroidMessage(String collapseKey,String message,Map<String,Object> customData) {
		Map<String, Object> androidMessageMap = new HashMap<String, Object>();
		androidMessageMap.put("collapse_key", collapseKey);
		androidMessageMap.put("data", getData(message,customData));
		androidMessageMap.put("delay_while_idle", true);
		androidMessageMap.put("time_to_live", 125);
		androidMessageMap.put("dry_run", false);
		return jsonify(androidMessageMap);
	}

	public static String getBaiduMessage(String title,String desc) {
		Map<String, Object> baiduMessageMap = new HashMap<String, Object>();
		baiduMessageMap.put("title", title);
		baiduMessageMap.put("description", desc);
		return jsonify(baiduMessageMap);
	}

	public static String getWNSMessage(String version,String value) {
		Map<String, Object> wnsMessageMap = new HashMap<String, Object>();
		wnsMessageMap.put("version", version);
		wnsMessageMap.put("value", value);
		return "<badge version=\"" + wnsMessageMap.get("version")
				+ "\" value=\"" + wnsMessageMap.get("value") + "\"/>";
	}

	public static String getMPNSMessage(String count,String payload) {
		Map<String, String> mpnsMessageMap = new HashMap<String, String>();
		mpnsMessageMap.put("count", count);
		mpnsMessageMap.put("payload", payload);
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><wp:Notification xmlns:wp=\"WPNotification\"><wp:Tile><wp:Count>"
				+ mpnsMessageMap.get("count")
				+ "</wp:Count><wp:Title>"
				+ mpnsMessageMap.get("payload")
				+ "</wp:Title></wp:Tile></wp:Notification>";
	}
}