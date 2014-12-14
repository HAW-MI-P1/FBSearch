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
import java.util.Locale;

/**
 * @author lotte
 */
public class RequestHandler {

	/* TODO: turn into singleton? */

	private static RequestHandler instance;
	private PropertyHandler propertyHandler;
	private String userAccessToken;
	private String userID;

	private JSONObject json;

	static HttpClient httpClient;

	private RequestHandler() {

		propertyHandler = PropertyHandler.getInstance();

		propertyHandler.getProperty("appID");
		propertyHandler.getProperty("appSecret");
		propertyHandler.getProperty("oauthAccessToken");
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
		if (userAccessToken == null) {
			System.err
					.println("userAccessToken missing. Please call AuthHandler.login() first.");
			return null;
		}

		if (userID != null) {
			return userID;
		}

		// ask facebook for my userID
		String requestStr = buildRequestStr("/me", "id", userAccessToken);
		System.out.println(userAccessToken);
		JSONObject response = this.get(requestStr);
		try {
			userID = (String) response.get("id");
		} catch (JSONException e) {
			System.out.println(json);
			e.printStackTrace();
		}

		// store it for future reference
		propertyHandler.setProperty("userID", userID);
		return userID;
	}

	/*
	 * getters for different search types. See
	 * https://developers.facebook.com/docs/graph-api/using-graph-api/v2.2 for
	 * all available types. TODO: add modifiers for fields?
	 */

	/* Search for a person (if they allow their name to be searched for). */
	public JSONObject searchForUser(String name) {
		String requestStr = buildRequestStr("search", name, "user",
				userAccessToken);
		JSONObject response = get(requestStr);
		return response;
	}

    /* Search for a object of "type" (if they allow their name to be searched for). */
    public JSONObject search(String name, String type) {
        String requestStr = buildRequestStr("search", name, type,
                userAccessToken);
        JSONObject response = get(requestStr);
        return response;
    }

	/* Search for a place. */
	public JSONObject searchForPlace(String name) {
		String requestStr = buildRequestStr("search", name, "place",
				userAccessToken);
		JSONObject response = get(requestStr);
		return response;
	}

	/*
	 * Search for a place and narrow your search to a specific location and
	 * distance by adding the center parameter (with latitude and longitude).
	 */
	public JSONObject searchForPlace(String name, double latitude,
			double longitude) {
		String requestStr = buildRequestStr("search", name, "place", latitude,
				longitude, userAccessToken);
		JSONObject response = get(requestStr);
		return response;
	}

	/*
	 * Search for a place and narrow your search to a specific location and
	 * distance by adding the center parameter (with latitude and longitude) and
	 * an optional distance parameter.
	 */
	public JSONObject searchForPlace(String name, double latitude,
			double longitude, double distance) {
		String requestStr = buildRequestStr("search", name, "place", latitude,
				longitude, distance, userAccessToken);
		JSONObject response = get(requestStr);
		return response;
	}

	// TODO: what about event, group, page, location etc?

	public JSONObject get(String requestStr) {
		JSONObject responseJSON = null;
		try {
			responseJSON = new JSONObject(getRaw(requestStr));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseJSON;
	}

	public String getRaw(String requestStr) {
		String responseStr = "";

		try {
			HttpGet request = new HttpGet(requestStr);
			HttpResponse response = httpClient.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
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

	/* endpoints can be "search", an id, "me" etc */
	private String buildRequestStr(String endpoint, String query, String type,
			String accessToken) {
		return String.format(
				"https://graph.facebook.com/%s?q=%s&type=%s&access_token=%s",
				endpoint, query, type, accessToken);
	}

	/* endpoints can be "search", an id, "me" etc */
	private String buildRequestStr(String endpoint, String fields,
			String accessToken) {
		return String.format(
				"https://graph.facebook.com/%s?fields=%s&access_token=%s",
				endpoint, fields, accessToken);
	}

	/* endpoints can be "search", an id, "me" etc */
	private String buildRequestStr(String endpoint, String query, String type,
			double latitude, double longitude, String accessToken) {
		return String
				.format(Locale.US,
						"https://graph.facebook.com/%s?q=%s&type=%s&center=%f,%f&access_token=%s",
						endpoint, query, type, latitude, longitude, accessToken);
	}

	/* endpoints can be "search", an id, "me" etc */
	private String buildRequestStr(String endpoint, String query, String type,
			double latitude, double longitude, double distance,
			String accessToken) {
		return String
				.format(Locale.US,
						"https://graph.facebook.com/%s?q=%s&type=%s&center=%f,%f&distance=%f&access_token=%s",
						endpoint, query, type, latitude, longitude, distance,
						accessToken);
	}

	public static String getQueryParameter(String name, String query) {
		String[] parameters = query.split("&");
		if (parameters.length < 1) {
			return null;
		}

		for (String parameter : parameters) {
			String[] keyValue = parameter.split("=");
			if (keyValue.length != 2) {
				throw new RuntimeException("Malformed query string "
						+ parameter);
			}

			if (keyValue[0].equals(name)) {
				return keyValue[1];
			}
		}

		return null;
	}
}
