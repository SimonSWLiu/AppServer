package com.onemenu.server.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.onemenu.server.util.awsnotification.SNSMobilePush;

@Controller
@RequestMapping("/sys/notification")
public class NotificationController {
	
	@RequestMapping(value="/in" , method=RequestMethod.GET)
	public String enterTest(){
		return "notify";
	}
	@RequestMapping(value="/push" , method=RequestMethod.POST)
	public String notification(String deviceToken,String message){
		try {
			SNSMobilePush.pushIOSNotification(message, deviceToken,null,0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "notify";
	}

}
