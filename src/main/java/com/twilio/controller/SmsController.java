package com.twilio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.service.SendSmsService;
import com.twilio.twiml.Body;

/**
 * Created by sumanth on 20/12/17.
 */
@RestController
public class SmsController {

  @Autowired
  private SendSmsService sendSmsService;

  @GetMapping("/test")
  public void hitSmsApi(){
     sendSmsService.sendSms();
  }
}
