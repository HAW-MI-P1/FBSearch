package de.haw.wrapper;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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

    static HttpClient httpClient;

    private RequestHandler() {

        propertyHandler = PropertyHandler.getInstance();

        appID = propertyHandler.getProperty("appID");
        appSecret = propertyHandler.getProperty("appSecret");
        oauthAccessToken = propertyHandler.getProperty("oauthAccessToken");
        userAccessToken = propertyHandler.getProperty("userAccessToken");

        httpClient = new DefaultHttpClient();
    }

    public static RequestHandler getInstance() {
        if (instance == null)
            instance = new RequestHandler();
        return instance;
    }

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

    //public void get



}
