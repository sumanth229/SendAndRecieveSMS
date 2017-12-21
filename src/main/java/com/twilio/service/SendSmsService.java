package com.twilio.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.twiml.Body;

/**
 * Created by sumanth on 20/12/17.
 */
public interface SendSmsService {
  void sendSms(Body body, String from) throws IOException;

  void service(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
