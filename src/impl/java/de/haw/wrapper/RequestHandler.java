package de.haw.wrapper;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * @author lotte
 */
public class RequestHandler {

    /* TODO: turn into singleton?*/

    private static RequestHandler instance;
    private PropertyHandler propertyHandler;
    private String appID;
    private String appSecret;
    private String oauthAccessToken;
    private String userAccessToken;
    private String userID;

    private JSONObject json;

    static HttpClient httpClient;

    private RequestHandler() {

        propertyHandler = PropertyHandler.getInstance();

        appID = propertyHandler.getProperty("appID");
        appSecret = propertyHandler.getProperty("appSecret");
        oauthAccessToken = propertyHandler.getProperty("oauthAccessToken");
        userAccessToken = propertyHandler.getProperty("userAccessToken");
        userID = propertyHandler.getProperty("userID");

        httpClient = new DefaultHttpClient();
    }

    public String getUserId() {
        if (userID != null) {
            System.out.println(userID);
            return userID;
        }

        // ask facebook for my userID
        String requestStr= "https://graph.facebook.com/me?fields=id&access_token="+userAccessToken;
        String response = this.get(requestStr);
        try {
            json = new JSONObject(response);
            userID = (String) json.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // store it for future reference
        propertyHandler.setProperty("userID", userID);
        return userID;
    }

    public static RequestHandler getInstance() {
        if (instance == null)
            instance = new RequestHandler();
        return instance;
    }

    // TODO make this private
    public String get(String requestStr) {
        String responseStr = "";
        try {
            HttpGet request = new HttpGet(requestStr);
            HttpResponse response = httpClient.execute(request);
            BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                responseStr += line;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    private void buildRequestStr() {
        /*TODO*/
    }
}
