package com.javasampleapproach.fcm.pushnotif.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.fcm.pushnotif.service.PushNotificationsService;

@RestController
public class WebController {

	private final String TOPIC = "offers";
	
	@Autowired
	PushNotificationsService pushNotificationsService;

	@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send() throws JSONException {

		JSONObject body = new JSONObject();
		//String tokens = FirebaseIn
		//body.put("to", "AAAA3EI3TIw:APA91bG832EcmjRdAbSAkyGAtVLBxMqLwL2vsqhO1quOlJGPgWjkp7jSYzl8kS63xaMIiF70B3JAx2SdIjXHtJVHzK9TWT7o6qLFIFrX_ZI2uVVqJoDEd7P_RSfAyFJm69jeYxf2Xlvb");
		body.put("to", "/topics/" + TOPIC);
		body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "offers");
		
		notification.put("body", "Diwali Offers");
		
		JSONObject data = new JSONObject();
		data.put("offerId", "3.21.15");
		data.put("contents", "http://www.news-magazine.com/world-week/21659772");
		
		

		body.put("notification", notification);
		body.put("data", data);

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		CompletableFuture<String> pushNotification = pushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();

		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}
}
