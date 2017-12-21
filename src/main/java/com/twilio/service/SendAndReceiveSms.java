package com.twilio.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.twiml.Body;
import com.twilio.twiml.TwiMLException;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;

/**
 * Created by sumanth on 20/12/17.
 */
@Service
@Slf4j
public class SendAndReceiveSms implements SendSmsService {
  public static final String ACCOUNT_SID = "AC70707777b0938f34a5815ba609cad736";
  public static final String AUTH_TOKEN = "a6dcac7cfe27284fbf5e6618216a4c73";

  public static final String SH_ACCOUNT_SID = "AC169fdaa726b323a61d9f27f05605aa44";
  public static final String SH_AUTH_TOKEN = "89424c9ca7c40ce793937a2364eb1dd2";


  public static String getDirectionsResponse(String origin, String destination) throws IOException {

    System.out.println("origin: " + origin + " ,destination :" + destination);
    System.out.println();
    origin = origin.replaceAll("\\s+", "+");
    destination = destination.replaceAll("\\s+", "+");
    URL url = new URL(
        "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" +
            destination);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      JSONObject json = new JSONObject(response.toString());
      JSONArray bundlesArray = json.getJSONArray("routes");
      JSONObject routeJson = bundlesArray.getJSONObject(0);
      JSONArray legs = routeJson.getJSONArray("legs");
      JSONObject legsJson = legs.getJSONObject(0);
      JSONArray steps = legsJson.getJSONArray("steps");
      return processJsonData(steps);
    } else {
      System.out.println("invalid GET request");
    }
    return null;
  }

  private static String processJsonData(JSONArray stepData) throws IOException {
    JSONObject stepJson;
    StringBuilder html_instructions = new StringBuilder();
    for (int i = 0; i < stepData.length(); i++) {
      stepJson = stepData.getJSONObject(i);
      html_instructions.append(html2text(stepJson.get("html_instructions").toString()))
          .append("\n");
    }
     return html_instructions.toString();
  }

  public static String html2text(String html) {
    return Jsoup.parse(html).text();
  }

  public void sendSms(Body body, String from) throws IOException {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    String bodyData = body.getBody();
    String origin = bodyData.substring(bodyData.indexOf('(')+1, bodyData.indexOf(')'));
    String destination = bodyData.substring(bodyData.lastIndexOf('(')+1, bodyData.lastIndexOf(')'));
    String htmlBody = getDirectionsResponse(origin, destination);
    from = from.substring(3);
    from = ("+" + from).trim();
    com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message
        .creator(new PhoneNumber("+918218536877"), new PhoneNumber("+12569527928"), htmlBody.substring(0,130))
        .create();
    System.out.println(from.trim());
    System.out.println(body.toString());
    System.out.println(message.getSid());
  }

  public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String bodyData = request.getParameter("Body");
    String origin = bodyData.substring(bodyData.indexOf('(')+1, bodyData.indexOf(')'));
    String destination = bodyData.substring(bodyData.lastIndexOf('(')+1, bodyData.lastIndexOf(')'));
    String message = getDirectionsResponse(origin, destination);
    // Create a TwiML response and add our friendly message.
    Message sms = new Message.Builder().body(new Body(message)).build();
    MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
    System.out.println("iam here");
    response.setContentType("application/xml");

    try {
      response.getWriter().print(twiml.toXml());
    } catch (TwiMLException e) {
      e.printStackTrace();
    }
  }
}
