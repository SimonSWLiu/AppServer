package com.onemenu.server.util.awsnotification;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;

public class BulkUploadToken {
	
	
	private static final Logger logger = Logger.getLogger(BulkUploadToken.class);
	
	 private AmazonSNS client;
	 
	 public BulkUploadToken() throws IOException{
	     client = new AmazonSNSClient(new PropertiesCredentials(SNSMobilePush.class.getClassLoader().getResourceAsStream("AwsCredentials.properties")));
	     client.setEndpoint("https://sns.us-west-2.amazonaws.com");   
	 }
	 
	 
	 public void sumbitToken(String token,String userData, String applicationArn){
		 CreatePlatformEndpointResult createResult = client
                 .createPlatformEndpoint(new CreatePlatformEndpointRequest()
                         .withPlatformApplicationArn(applicationArn)
                         .withToken(token)
                         .withCustomUserData(userData));
		 if(logger.isDebugEnabled())
			 logger.debug("EndpointArn:"+createResult.getEndpointArn());
	 }
	 
	 

}
