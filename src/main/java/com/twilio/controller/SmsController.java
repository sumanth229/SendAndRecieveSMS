package com.twilio.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public void hitSmsApi(@RequestParam(value = "Body") Body body, @RequestParam(value = "From") String from) throws IOException {
     sendSmsService.sendSms(body, from);
  }

  @GetMapping("/test1")
  public void hiSmsService1(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    sendSmsService.service(request, response);
  }
}
