package com.twilio.service;

import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * Created by sumanth on 20/12/17.
 */
@Service
public class SendAndReceiveSms {
  public static final String ACCOUNT_SID = "AC70707777b0938f34a5815ba609cad736";
  public static final String AUTH_TOKEN = "a6dcac7cfe27284fbf5e6618216a4c73";

  public static void main(String[] args) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    Message message =
        Message.creator(new PhoneNumber("+12569063892"),new PhoneNumber("+12569527928"),
            "whats up?").create();

    System.out.println(message.getSid());
  }
}
