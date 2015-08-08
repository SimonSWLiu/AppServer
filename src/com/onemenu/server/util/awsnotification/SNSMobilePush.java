package com.onemenu.server.util.awsnotification;

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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.onemenu.server.cache.ConfigCache;
import com.onemenu.server.constant.ParameterConstant.ConfigKey;
import com.onemenu.server.util.awsnotification.tools.AmazonSNSClientWrapper;
import com.onemenu.server.util.awsnotification.tools.MessageGenerator;
import com.onemenu.server.util.awsnotification.tools.MessageGenerator.Platform;

public final class SNSMobilePush {

	private static final Logger		logger			= Logger.getLogger(SNSMobilePush.class);

	private static final String		S_Certificate	= "-----BEGIN CERTIFICATE-----\nMIIFjTCCBHWgAwIBAgIIacsCbX9vRvwwDQYJKoZIhvcNAQEFBQAwgZYxCzAJBgN\nVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHBs\n"
															+ "ZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEIGA1UEAww7QXBwb\nGUgV29ybGR3aWRlIERldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBdX\nRob3JpdHkwHhcNMTUwNjA3MDEzNjUzWhcNMTYwNjA2MDEzNjUzWjCBjDEkMCIG\n"
															+ "CgmSJomT8ixkAQEMFGNvbS5PbmVNZW51LkRlbGl2ZXJ5MUIwQAYDVQQDDDlBc\nHBsZSBEZXZlbG9wbWVudCBJT1MgUHVzaCBTZXJ2aWNlczogY29tLk9uZU1lbnUu\nRGVsaXZlcnkxEzARBgNVBAsMCjk0NDZCWTRaQkQxCzAJBgNVBAYTAlVTMIIBIjA\n"
															+ "NBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2yxHtgtMygakwr+rtUTkcd+j6EO5\nNhKOrDy5cFGKO41apUGFPmf9EV0H5utaCnld8vaNbqIy0NJkuo4L2Wao3ObtTJdh\nS0Po4l70XI2sYlbJa1aOSTjU7hvLBl+Y9KjCeSeeNhihd9FKoOebrzZl+9/LUeBLiyQ5Z\nSiK+RDm7rx4l1lvyrib9nZ8ML1PcNxi5LJsOncI2YpmXcb8AV2OCO9MEY9yMUOWL9\n"
															+ "nbrnKGgVAETxe20AX7EB2D4MX+xFS6oaY0BnwFBdVXztWr31/Jo2LoluR8gFU296\nZKHQ7JJelZd1hZgr+lBUzvenkLE1OKwBG+GXIkxtN52kLpbIgvfQIDAQABo4IB5TCC\nAeEwHQYDVR0OBBYEFD1efmeuEIo9hMN0BlTNRdCTX7ztMAkGA1UdEwQCMAAw\nHwYDVR0jBBgwFoAUiCcXCam2GGCL7Ou69kdZxVJUo7cwggEPBgNVHSAEggEG\n"
															+ "MIIBAjCB/wYJKoZIhvdjZAUBMIHxMIHDBggrBgEFBQcCAjCBtgyBs1JlbGlhbmNlIG9u\nIHRoaXMgY2VydGlmaWNhdGUgYnkgYW55IHBhcnR5IGFzc3VtZXMgYWNjZXB0YW\n5jZSBvZiB0aGUgdGhlbiBhcHBsaWNhYmxlIHN0YW5kYXJkIHRlcm1zIGFuZCBjb25k\naXRpb25zIG9mIHVzZSwgY2VydGlmaWNhdGUgcG9saWN5IGFuZCBjZXJ0aWZpY2\n"
															+ "F0aW9uIHByYWN0aWNlIHN0YXRlbWVudHMuMCkGCCsGAQUFBwIBFh1odHRwOi\n8vd3d3LmFwcGxlLmNvbS9hcHBsZWNhLzBNBgNVHR8ERjBEMEKgQKA+hjxodHRw\nOi8vZGV2ZWxvcGVyLmFwcGxlLmNvbS9jZXJ0aWZpY2F0aW9uYXV0aG9yaXR5L3\nd3ZHJjYS5jcmwwCwYDVR0PBAQDAgeAMBMGA1UdJQQMMAoGCCsGAQUFBwM\nCMBAGCiqGSIb3Y2QGAwEEAgUAMA0GCSqGSIb3DQEBBQUAA4IBAQAA8UWVfn\n"
															+ "Dw1QdCDETTAAfXfoB1MiD/q1MNgDZoC4pG3tjdeoetbYZaqyHfFMLkVWXruf1yApA\nP3oD7WsCXYT5hGyfj4F2QP+G+chE5N4XAgVf/R4VkpvG2kxd0LvHwkYnpCMkQ4p1\nPGWCy3R5KDMm8tIWpyVWDcNyA4XYgL6wiogsDjs20MZlnySbWS8QJ2Zq3vDiSgY\n88jNXJTfymMh7LoftPjuB8icaNr0YfCU+VQqRkb/zbp9LAD42at5KjZs4EByZtIg7SSX3t\n+KHxPCv+OjUkn6/5YfT6GvCG9ngvXHkkjWfz3Ekyk1YUJ3qb17738v3GOjHgeUyaqk\n"
															+ "Pz975N\n-----END CERTIFICATE-----";

	private static final String		S_Private_Key	= "-----BEGIN PRIVATE KEY-----\nMIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDbLEe2C0zKBq\nTCv6u1RORx36PoQ7k2Eo6sPLlwUYo7jVqlQYU+Z/0RXQfm61oKeV3y9o1uojLQ0m\nS6jgvZZqjc5u1Ml2FLQ+jiXvRcjaxiVslrVo5JONTuG8sGX5j0qMJ5J542GKF30Uqg55u\nvNmX738tR4EuLJDllKIr5EObuvHiXWW/KuJv2dnwwvU9w3GLksmw6dwjZimZdxvwB\n"
															+ "XY4I70wRj3IxQ5Yv2duucoaBUARPF7bQBfsQHYPgxf7EVLqhpjQGfAUF1VfO1avfX8\nmjYuiW5HyAVTb3pkodDskl6Vl3WFmCv6UFTO96eQsTU4rAEb4ZciTG03naQulsiC9\n9AgMBAAECggEBALYI8QaiHAUvv2iBCXxk29g4h0pB9XKTtywWw24mS0lq/f6DjMdo\nVyg0vHtuvjo003MYlB9vWAVYuZC4qd7HBTl0E2Op14VLwgFCHHNCuAp1SpV8LnFi\nYnfhXZ7Nh5ytDdjSrvT/QBzkF7fMpeHArLOmDJjUUeofDAyYv9Rd1XAyT5sGy23Xiyk\n"
															+ "LPtV/iM4rlXtRg2ueNjLnymKOhVnL3adpjdeAKsarQE6iwzAA5lPnqpo9C1TSVhC9Soh\nueDSoFQKPev9fdNN7WJwOcuze/ukn+I/6B8IKyFDH9DX0veVFXXOUxsk8/rP2BRm8\nR0t06B/FkyIA0FF+Ic0Oa7HvBAECgYEA94gdyOKc2b1b78XuxCuBWch2HlgiKAgWm\nq+2/dBJ5ET/4jaWpp2xxCP5JAtD4i0mMgWNc38O0QZpzeyAmpg6xgtb8ria7COztnov\n"
															+ "xNPV6MG3UxGZf0lKgXmftdpJybV6WeTcAYxKXCGEAuDEnif9YjieG/8Pvcp7fHf5Ou7\nf70kCgYEA4qvMQHhF7nebME7SUIL276S0tb4NruwZqLcgNa/d+ZcNadmztISRCQH\nuLbECSjtqZJBjtXYmXrLFyv9KWtObMG+XLAOO+thF2ACi0nLY/GJ3M7vWPX8xykg4\nhYWMnhgTLkKimNZiSM9etD1Know9FVGCJIz4qlLQJfdbUvxAmpUCgYEA3BaT3Qz\nYx6VdfFjyTkwca9cYZyyX2h4u8wTvYMf1G1jaZ6l/0898wtf1ar31csIQJwPW3G2JoAL\n"
															+ "iymzRgCOAhpyM+ch4PyQTY1lK5egwVw7+0IZig9GHxI1+mvPnDckla45Yq6uwZjCKr\nwBo8fK+kbmjo2Lq/SZvG6GU4XiJdbkCgYEAwKF9eEB+NbVhpkEmDJPv5SKVDGqS\nWFh/Sdkuv1FRFD5hgNYpGT0bwIrqbdwi9RDfTs1CX0EkRJAq9WPVPbDdDuGWmC\noCBX1sha+sr6o4auwHH2j/Hc4NcznDBv3Czcvp6QNqCPcQGY5ZjlUUJ/9IHejh78v5u\n02bZNh8vN5wzK0CgYEA9bB/YiYC864dtPQ6YvQQ7YWw37jNTV0EnJuClu83KPJHk\n"
															+ "cg5d9zF3IFP7h9sMndxxJACwZSKAfp7eSEBqvs8TN7vwwhr85qbIsA8+MA/M2EuR9l\nDnfqSBZSEgGCeQ+ua5QsnTVhW0uGw5A0mafOxUAPX9rIYtJJM1vG/o6Z8cKs=\n-----END PRIVATE KEY-----";

	private static final String		P_Certificate	= "-----BEGIN CERTIFICATE-----\nMIIFjDCCBHSgAwIBAgIIPRw4CbYOBYMwDQYJKoZIhvcNAQEFBQAwgZYxCzAJBg\nNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHB\nsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEIGA1UEAww7QXBw\n"
															+ "bGUgV29ybGR3aWRlIERldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBd\nXRob3JpdHkwHhcNMTUwNjA3MDEzNzI3WhcNMTYwNjA2MDEzNzI3WjCBizEkMCI\nGCgmSJomT8ixkAQEMFGNvbS5PbmVNZW51LkRlbGl2ZXJ5MUEwPwYDVQQDDD\nhBcHBsZSBQcm9kdWN0aW9uIElPUyBQdXNoIFNlcnZpY2VzOiBjb20uT25lTWVudS\n5EZWxpdmVyeTETMBEGA1UECwwKOTQ0NkJZNFpCRDELMAkGA1UEBhMCVVM\nwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDbLEe2C0zKBqTCv6u1\n"
															+ "RORx36PoQ7k2Eo6sPLlwUYo7jVqlQYU+Z/0RXQfm61oKeV3y9o1uojLQ0mS6jgvZZ\nqjc5u1Ml2FLQ+jiXvRcjaxiVslrVo5JONTuG8sGX5j0qMJ5J542GKF30Uqg55uvNmX73\n8tR4EuLJDllKIr5EObuvHiXWW/KuJv2dnwwvU9w3GLksmw6dwjZimZdxvwBXY4I70w\nRj3IxQ5Yv2duucoaBUARPF7bQBfsQHYPgxf7EVLqhpjQGfAUF1VfO1avfX8mjYuiW5\nHyAVTb3pkodDskl6Vl3WFmCv6UFTO96eQsTU4rAEb4ZciTG03naQulsiC99AgMBA\nAGjggHlMIIB4TAdBgNVHQ4EFgQUPV5+Z64Qij2Ew3QGVM1F0JNfvO0wCQYDVR0\n"
															+ "TBAIwADAfBgNVHSMEGDAWgBSIJxcJqbYYYIvs67r2R1nFUlSjtzCCAQ8GA1UdIAS\nCAQYwggECMIH/BgkqhkiG92NkBQEwgfEwgcMGCCsGAQUFBwICMIG2DIGzUmVs\naWFuY2Ugb24gdGhpcyBjZXJ0aWZpY2F0ZSBieSBhbnkgcGFydHkgYXNzdW1lcyBh\nY2NlcHRhbmNlIG9mIHRoZSB0aGVuIGFwcGxpY2FibGUgc3RhbmRhcmQgdGVybX\nMgYW5kIGNvbmRpdGlvbnMgb2YgdXNlLCBjZXJ0aWZpY2F0ZSBwb2xpY3kgYW5kI\nGNlcnRpZmljYXRpb24gcHJhY3RpY2Ugc3RhdGVtZW50cy4wKQYIKwYBBQUHAgE\nWHWh0dHA6Ly93d3cuYXBwbGUuY29tL2FwcGxlY2EvME0GA1UdHwRGMEQwQqB\n"
															+ "AoD6GPGh0dHA6Ly9kZXZlbG9wZXIuYXBwbGUuY29tL2NlcnRpZmljYXRpb25hdXR\nob3JpdHkvd3dkcmNhLmNybDALBgNVHQ8EBAMCB4AwEwYDVR0lBAwwCgYIKwYB\nBQUHAwIwEAYKKoZIhvdjZAYDAgQCBQAwDQYJKoZIhvcNAQEFBQADggEBAFeH7\nlLR+RGEcqyhkbgbDI0gdZZvz+r//Hztd6juFazsRDl1JrX3ynPID6Xln6oSpAnYV3W5QZ\nafOKal+dDf6WKQaqAfpVRlfySsOdhZ1oOS28QCaDR656HBvgldOXHt1odbBAs1fTiB\nHF51iozE1XPSB7p4ev8iDsZjEgW7/5PtR31ipv/H4PyuasrTmln/80c2Y39E5PcvfLwtob\n"
															+ "O7BRheyKgE7meCF/gS41OBFqC+WXCLqVErkYX/s2WwE9eUGGl1XOSWyKKvUrE\ncfaQUbfTDwRDikViGiuCOjWJtJ2ebvakSOq5Ox4cE6SFOhvp22ahqeoZ5InLB4gi5GG\nlfD5k=\n-----END CERTIFICATE-----";

	private static final String		P_Private_Key	= "-----BEGIN PRIVATE KEY-----\nMIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDbLEe2C0zKBq\nTCv6u1RORx36PoQ7k2Eo6sPLlwUYo7jVqlQYU+Z/0RXQfm61oKeV3y9o1uojLQ0m\nS6jgvZZqjc5u1Ml2FLQ+jiXvRcjaxiVslrVo5JONTuG8sGX5j0qMJ5J542GKF30Uqg55u\n"
															+ "vNmX738tR4EuLJDllKIr5EObuvHiXWW/KuJv2dnwwvU9w3GLksmw6dwjZimZdxvwB\nXY4I70wRj3IxQ5Yv2duucoaBUARPF7bQBfsQHYPgxf7EVLqhpjQGfAUF1VfO1avfX8\nmjYuiW5HyAVTb3pkodDskl6Vl3WFmCv6UFTO96eQsTU4rAEb4ZciTG03naQulsiC9\n9AgMBAAECggEBALYI8QaiHAUvv2iBCXxk29g4h0pB9XKTtywWw24mS0lq/f6DjMdo\nVyg0vHtuvjo003MYlB9vWAVYuZC4qd7HBTl0E2Op14VLwgFCHHNCuAp1SpV8LnFi\n"
															+ "YnfhXZ7Nh5ytDdjSrvT/QBzkF7fMpeHArLOmDJjUUeofDAyYv9Rd1XAyT5sGy23Xiyk\nLPtV/iM4rlXtRg2ueNjLnymKOhVnL3adpjdeAKsarQE6iwzAA5lPnqpo9C1TSVhC9Soh\nueDSoFQKPev9fdNN7WJwOcuze/ukn+I/6B8IKyFDH9DX0veVFXXOUxsk8/rP2BRm8\nR0t06B/FkyIA0FF+Ic0Oa7HvBAECgYEA94gdyOKc2b1b78XuxCuBWch2HlgiKAgWm\nq+2/dBJ5ET/4jaWpp2xxCP5JAtD4i0mMgWNc38O0QZpzeyAmpg6xgtb8ria7COztnov\nxNPV6MG3UxGZf0lKgXmftdpJybV6WeTcAYxKXCGEAuDEnif9YjieG/8Pvcp7fHf5Ou7\n"
															+ "f70kCgYEA4qvMQHhF7nebME7SUIL276S0tb4NruwZqLcgNa/d+ZcNadmztISRCQH\nuLbECSjtqZJBjtXYmXrLFyv9KWtObMG+XLAOO+thF2ACi0nLY/GJ3M7vWPX8xykg4\nhYWMnhgTLkKimNZiSM9etD1Know9FVGCJIz4qlLQJfdbUvxAmpUCgYEA3BaT3Qz\nYx6VdfFjyTkwca9cYZyyX2h4u8wTvYMf1G1jaZ6l/0898wtf1ar31csIQJwPW3G2JoALi\nymzRgCOAhpyM+ch4PyQTY1lK5egwVw7+0IZig9GHxI1+mvPnDckla45Yq6uwZjCKr\nwBo8fK+kbmjo2Lq/SZvG6GU4XiJdbkCgYEAwKF9eEB+NbVhpkEmDJPv5SKVDGqS\n"
															+ "WFh/Sdkuv1FRFD5hgNYpGT0bwIrqbdwi9RDfTs1CX0EkRJAq9WPVPbDdDuGWmC\noCBX1sha+sr6o4auwHH2j/Hc4NcznDBv3Czcvp6QNqCPcQGY5ZjlUUJ/9IHejh78v5u\n02bZNh8vN5wzK0CgYEA9bB/YiYC864dtPQ6YvQQ7YWw37jNTV0EnJuClu83KPJHk\ncg5d9zF3IFP7h9sMndxxJACwZSKAfp7eSEBqvs8TN7vwwhr85qbIsA8+MA/M2EuR9l\nDnfqSBZSEgGCeQ+ua5QsnTVhW0uGw5A0mafOxUAPX9rIYtJJM1vG/o6Z8cKs=\n-----END PRIVATE KEY-----";

	private AmazonSNSClientWrapper	snsClientWrapper;

	private SNSMobilePush(AmazonSNS snsClient) {
		this.snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
	}

	public static final Map<Platform, Map<String, MessageAttributeValue>>	attributesMap	= new HashMap<Platform, Map<String, MessageAttributeValue>>();

	static {
		attributesMap.put(Platform.ADM, null);
		attributesMap.put(Platform.GCM, null);
		attributesMap.put(Platform.APNS, null);
		attributesMap.put(Platform.APNS_SANDBOX, null);
		attributesMap.put(Platform.BAIDU, addBaiduNotificationAttributes());
		attributesMap.put(Platform.WNS, addWNSNotificationAttributes());
		attributesMap.put(Platform.MPNS, addMPNSNotificationAttributes());
	}

	public static void pushIOSNotification(String message, String deviceToken, Map<String, Object> customData, int badge) throws IOException {

		AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(SNSMobilePush.class.getClassLoader().getResourceAsStream("AwsCredentials.properties")));

		sns.setEndpoint("https://sns.us-west-2.amazonaws.com");
		if (logger.isDebugEnabled()) {
			logger.debug("===========================================\n");
			logger.debug("Getting Started with Amazon SNS  IOS");
			logger.debug("===========================================\n");
		}

		try {
			SNSMobilePush push = new SNSMobilePush(sns);
			String applicationName = ConfigCache.getValue(ConfigKey.APPLICATION_NAME, "One-Menu");
			String _message = MessageGenerator.getAppleMessage(message, null, badge, customData);
			boolean isSandBox = Boolean.parseBoolean(ConfigCache.getValue(ConfigKey.IS_SAND_BOX, "true"));
			Platform platform = null;
			String Certificate = null;
			String Private_Key = null;
			if (isSandBox) {
				platform = Platform.APNS_SANDBOX;
				Certificate = S_Certificate;
				Private_Key = S_Private_Key;
			} else {
				platform = Platform.APNS;
				Certificate = P_Certificate;
				Private_Key = P_Private_Key;
			}

			push.snsClientWrapper.notification(platform, Certificate, Private_Key, deviceToken, applicationName, attributesMap, _message);

		} catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SNS, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SNS, such as not " + "being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	public static void pushAndroidNotification(String message, String deviceToken, Map<String, Object> customData) throws IOException {

		AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(SNSMobilePush.class.getClassLoader().getResourceAsStream("AwsCredentials.properties")));

		sns.setEndpoint("https://sns.us-west-2.amazonaws.com");
		if (logger.isDebugEnabled()) {
			logger.debug("===========================================\n");
			logger.debug("Getting Started with Amazon SNS android");
			logger.debug("===========================================\n");
		}

		try {
			SNSMobilePush push = new SNSMobilePush(sns);
			String applicationName = ConfigCache.getValue(ConfigKey.APPLICATION_NAME, "One-Menu");
			String serverAPIKey = "";
			String _message = MessageGenerator.getSampleAndroidMessage("NewOrder", message, customData);
			push.snsClientWrapper.notification(Platform.GCM, "", serverAPIKey, deviceToken, applicationName, attributesMap, _message);

		} catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SNS, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SNS, such as not " + "being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {
		/*
		 * TODO: Be sure to fill in your AWS access credentials in the
		 * AwsCredentials.properties file before you try to run this sample.
		 * http://aws.amazon.com/security-credentials
		 */
		AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(SNSMobilePush.class.getClassLoader().getResourceAsStream("AwsCredentials.properties")));

		sns.setEndpoint("https://sns.us-west-2.amazonaws.com");
		System.out.println("===========================================\n");
		System.out.println("Getting Started with Amazon SNS");
		System.out.println("===========================================\n");
		try {
			SNSMobilePush sample = new SNSMobilePush(sns);
			sample.demoAppleSandboxAppNotification();
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SNS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SNS, such as not " + "being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	public void demoAndroidAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAndroidMessage()
		String serverAPIKey = "";
		String applicationName = "";
		String registrationId = "";
		String message = "";
		snsClientWrapper.notification(Platform.GCM, "", serverAPIKey, registrationId, applicationName, attributesMap, message);
	}

	public void demoKindleAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleKindleMessage()
		String clientId = "";
		String clientSecret = "";
		String applicationName = "";

		String registrationId = "";
		String message = "";
		snsClientWrapper.notification(Platform.ADM, clientId, clientSecret, registrationId, applicationName, attributesMap, message);
	}

	public void demoAppleAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAppleMessage()
		String certificate = ""; // This should be in pem format with \n at the
									// end of each line.
		String privateKey = ""; // This should be in pem format with \n at the
								// end of each line.
		String applicationName = "";
		String deviceToken = ""; // This is 64 hex characters.
		String message = "";
		snsClientWrapper.notification(Platform.APNS, certificate, privateKey, deviceToken, applicationName, attributesMap, message);
	}

	public void demoAppleSandboxAppNotification() {
		// TODO: Please fill in following values for your application. You can
		// also change the notification payload as per your preferences using
		// the method
		// com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAppleMessage()
		// String certificate = ""; // This should be in pem format with \n at
		// the
		// end of each line.
		// String privateKey = ""; // This should be in pem format with \n at
		// the
		// end of each line.
		String applicationName = "One-Menu";
		// d405d462fd98d0ef5ba9735a0f54e2f2f7b2aa5c4b2a926ea06239a646ecf4f2
		// arn:aws:sns:us-east-1:032952716647:endpoint/APNS_SANDBOX/One-Menu/cf25ecf8-8c13-327c-a778-12c401cb0892
		// Vincent's iphone5s
		// true
		String deviceToken = "d405d462fd98d0ef5ba9735a0f54e2f2f7b2aa5c4b2a926ea06239a646ecf4f2"; // This
																									// is
																									// 64
																									// hex
																									// characters.
		String message = MessageGenerator.getAppleMessage("this is file test", null, 0, null);
		snsClientWrapper.notification(Platform.APNS_SANDBOX, S_Certificate, S_Private_Key, deviceToken, applicationName, attributesMap, message);
	}

	public void demoBaiduAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your Baidu notification, you can
		 * do so by modifying the attribute values in the method
		 * addBaiduNotificationAttributes() . You can also change the
		 * notification payload as per your preferences using the method
		 * com.amazonaws
		 * .sns.samples.tools.SampleMessageGenerator.getSampleBaiduMessage()
		 */
		String userId = "";
		String channelId = "";
		String apiKey = "";
		String secretKey = "";
		String applicationName = "";
		String message = "";
		snsClientWrapper.notification(Platform.BAIDU, apiKey, secretKey, channelId + "|" + userId, applicationName, attributesMap, message);
	}

	public void demoWNSAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your WNS notification, you can
		 * do so by modifying the attribute values in the method
		 * addWNSNotificationAttributes() . You can also change the notification
		 * payload as per your preferences using the method
		 * com.amazonaws.sns.samples
		 * .tools.SampleMessageGenerator.getSampleWNSMessage()
		 */
		String notificationChannelURI = "";
		String packageSecurityIdentifier = "";
		String secretKey = "";
		String applicationName = "";
		String message = "";
		snsClientWrapper.notification(Platform.WNS, packageSecurityIdentifier, secretKey, notificationChannelURI, applicationName, attributesMap, message);
	}

	public void demoMPNSAppNotification() {
		/*
		 * TODO: Please fill in the following values for your application. If
		 * you wish to change the properties of your MPNS notification, you can
		 * do so by modifying the attribute values in the method
		 * addMPNSNotificationAttributes() . You can also change the
		 * notification payload as per your preferences using the method
		 * com.amazonaws
		 * .sns.samples.tools.SampleMessageGenerator.getSampleMPNSMessage ()
		 */
		String notificationChannelURI = "";
		String applicationName = "";
		String message = "";
		snsClientWrapper.notification(Platform.MPNS, "", "", notificationChannelURI, applicationName, attributesMap, message);
	}

	private static Map<String, MessageAttributeValue> addBaiduNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.DeployStatus", new MessageAttributeValue().withDataType("String").withStringValue("1"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageKey",
				new MessageAttributeValue().withDataType("String").withStringValue("default-channel-msg-key"));
		notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageType", new MessageAttributeValue().withDataType("String").withStringValue("0"));
		return notificationAttributes;
	}

	private static Map<String, MessageAttributeValue> addWNSNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.WNS.CachePolicy", new MessageAttributeValue().withDataType("String").withStringValue("cache"));
		notificationAttributes.put("AWS.SNS.MOBILE.WNS.Type", new MessageAttributeValue().withDataType("String").withStringValue("wns/badge"));
		return notificationAttributes;
	}

	private static Map<String, MessageAttributeValue> addMPNSNotificationAttributes() {
		Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
		notificationAttributes.put("AWS.SNS.MOBILE.MPNS.Type", new MessageAttributeValue().withDataType("String").withStringValue("token")); // This
																																				// attribute
																																				// is
																																				// required.
		notificationAttributes.put("AWS.SNS.MOBILE.MPNS.NotificationClass", new MessageAttributeValue().withDataType("String").withStringValue("realtime")); // This
																																								// attribute
																																								// is
																																								// required.

		return notificationAttributes;
	}
}
