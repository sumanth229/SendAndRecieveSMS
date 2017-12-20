package com.twilio.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by sumanth on 20/12/17.
 */
@Service
@Slf4j
public class SendAndReceiveSms implements SendSmsService {
  public static final String ACCOUNT_SID = "AC70707777b0938f34a5815ba609cad736";
  public static final String AUTH_TOKEN = "a6dcac7cfe27284fbf5e6618216a4c73";

  public void sendSms() {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    Message message = Message
        .creator(new PhoneNumber("+12569063892"), new PhoneNumber("+12569527928"), "whats up?")
        .create();

    System.out.println("\n great there-twilio");
  }

  public static void getDirectionsResponse(String origin, String destination) throws IOException {

    origin = origin.replaceAll("\\s+","+");
    destination = destination.replaceAll("\\s+","+");
    System.out.println("origin: "+ origin + " ,destination :"+ destination);
    URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" +
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
      JSONObject routeJson =  bundlesArray.getJSONObject(0);
      JSONArray legs = routeJson.getJSONArray("legs");
      JSONObject legsJson = legs.getJSONObject(0);
      JSONArray steps = legsJson.getJSONArray("steps");
      processJsonData(steps);
//      System.out.println(response.toString());
    } else {
      System.out.println("invalid GET request");
    }

  }

  private static void processJsonData(JSONArray stepData){
    JSONObject stepJson;
    StringBuilder html_instructions = new StringBuilder();
    for(int i =0 ; i< stepData.length(); i++){
      stepJson = stepData.getJSONObject(i);
      html_instructions.append(html2text(stepJson.get("html_instructions").toString())).append("\n");
    }
    System.out.println(html_instructions);
  }

  public static String html2text(String html) {
    return Jsoup.parse(html).text();
  }

  public static void main(String[] args) throws IOException {
    getDirectionsResponse("coviam B block","The Hsr Club sector 3");
  }
}
