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

    public static RequestHandler getInstance() {
        if (instance == null)
            instance = new RequestHandler();
        return instance;
    }

    public String getUserId() {
        if (userID != null) {
            return userID;
        }

        // ask facebook for my userID
        //String requestStr= "https://graph.facebook.com/me?fields=id&access_token="+userAccessToken;
        String requestStr = buildRequestStr("/me", "id", userAccessToken);
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

    /*
    * getters for different search types. See https://developers.facebook.com/docs/graph-api/using-graph-api/v2.2
    * for all available types.
    */

    /* Search for a person (if they allow their name to be searched for). */
    public JSONObject searchForUser(String name) {
        /*TODO implement me*/

        return null;
    }

    /* Search for a place. */
    public JSONObject searchForPlace(String name) {
        /*TODO implement me*/
        return null;
    }

    /* Search for a place and narrow your search to a specific location and distance by adding the center parameter
     * (with latitude and longitude).*/
    public JSONObject searchForPlace(String name, float latitude, float longitude) {
        /*TODO implement me*/
        return null;
    }

    /* Search for a place and narrow your search to a specific location and distance by adding the center parameter
     * (with latitude and longitude) and an optional distance parameter.*/
    public JSONObject searchForPlace(String name, float latitude, float longitude, float distance) {
        /*TODO implement me*/
        return null;
    }
    // TODO: what about event, group, page, location etc?

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

    /* endpoints can be "search", an id, "me" etc  */
    private String buildRequestStr(String endpoint, String query, String type, String accessToken) {
        /*TODO implement me*/
        return null;
    }


    /* endpoints can be "search", an id, "me" etc  */
    private String buildRequestStr(String endpoint, String fields, String accessToken) {
        return String.format("https://graph.facebook.com/%s?fields=%s&access_token=%s", endpoint, fields, accessToken);
    }
}
