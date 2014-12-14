package de.haw.wrapper;

import de.haw.model.types.Type;
import de.haw.model.types.UserType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author lotte
 */
public class WrapperImpl implements Wrapper {

	private RequestHandler requestHandler;
	private AuthHandler authHandler;

	public WrapperImpl() {
		requestHandler = RequestHandler.getInstance();
		authHandler = new AuthHandler();
		authHandler.login();
		testthings();
	}

	private void testthings() {
		// System.out.println(requestHandler.searchForUser("Max"));
		// System.out.println("-----------------------------------------------------------------\n");
		// System.out.println(requestHandler.searchForPlace("Reeperbahn"));
		// System.out.println("-----------------------------------------------------------------\n");
		// System.out.println(requestHandler.searchForPlace("Cafe",
		// 53.5499851056, 9.96649770869));
		// System.out.println("-----------------------------------------------------------------\n");
		// System.out.println(requestHandler.searchForPlace("Cafe",
		// 53.5499851056, 9.96649770869, 1.5345612));
	}

	public Collection<Type> collect(JSONObject requests) {
		Collection<Type> resultData = new ArrayList<Type>();

		// Get subject from requests
		String subject = "-";
		try {
			subject = requests.getString("subject");
			System.out.println("subject is: " + subject);
		} catch (JSONException e) {
			System.out.println("no subject");
		}

		// Get places from requests
		List<String> places = getValues(requests, "place");

		// Get names from request
		List<String> names = getValues(requests, "name");

		// Start a request for names and add the response to the result data
		if (names.size() > 0) {
			JSONObject namesResponse = requestHandler
					.searchForUser(concatStrings(names));
			resultData.addAll(transformResponse(namesResponse));
		}

		// If a place exist, check all users
		if (places.size() > 0) {
			for (Type t : resultData) {
				String name = null;
				JSONObject userResponse = requestHandler.searchForID(t.getID(),
						"");
				try {
					JSONObject locationObject = userResponse
							.getJSONObject("location");
					name = locationObject.getString("name");
				} catch (JSONException e) {
					name = null;
				}
				if (name != null) {
					System.out.println("location is: " + name);
				} else {
					System.out.println("no location for: " + t.getID());
				}
			}
		}

		return resultData;
	}

	public Collection<Type> searchForName(String type, List<String> names) {
		Collection<Type> resultData = new ArrayList<Type>();
		if (names.size() > 0) {
			JSONObject namesResponse = requestHandler.search(
					concatStrings(names), type);
			resultData.addAll(transformResponse(namesResponse));
		}
		return resultData;
	}

	public List<URL> getPicturesForPersons(Collection<Type> personsOfInterest) {
		List<URL> result = new ArrayList<URL>();
		StringBuilder userIDs = new StringBuilder();
		int count = 0;
		for (Type p : personsOfInterest) {
			
			// Facebook ids are limited to 50
			count++;
			if (count > 50) {
				break;
			}
			
			// Ids are comma separated
			if (userIDs.length() > 0) {
				userIDs.append(",");
			}
			userIDs.append(p.getID());
		}
		JSONObject response = requestHandler.picturesForUserIDs(userIDs
				.toString());
		
		@SuppressWarnings("unchecked")
		Iterator<String> i = response.keys();
		while (i.hasNext()) {
			String key = i.next();
			try {
				JSONObject user = response.getJSONObject(key);
				JSONObject data = user.getJSONObject("data");
				result.add(new URL(data.getString("url")));
			} catch (JSONException | MalformedURLException e) {
				System.out.println("no picture for: " + key);
			}
		}
		
		return result;
	}

	public boolean idLivesIn(String id, String location) {
		String name = null;
		JSONObject userResponse = requestHandler.searchForID(id,
				"location");
		try {
			JSONObject locationObject = userResponse
					.getJSONObject("location");
			name = locationObject.getString("name");
		} catch (JSONException e) {
			name = null;
		}
		if (name != null) {
			return name.contains(location);
		}
		return false;
	}

	@Override
	public Collection<Type> collectExtended(JSONObject requests,
			Collection<Type> personsOfInterest) {
		return null;
	}

	private Collection<Type> transformResponse(JSONObject response) {
		Collection<Type> resultData = new ArrayList<Type>();
		if (response != null) {
			try {
				JSONArray data = response.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					JSONObject user = data.getJSONObject(i);
					resultData.add(new UserType(user.getString("id"), user
							.getString("name")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return resultData;
	}

	private List<String> getValues(JSONObject requests, String valueType) {
		List<String> result = new ArrayList<String>();
		try {
			JSONArray p = requests.getJSONArray(valueType);
			for (int i = 0; i < p.length(); i++) {
				result.add(p.getString(i));
			}
		} catch (JSONException e) {
			System.out.println("no " + valueType + "s");
		}
		return result;
	}

	private String concatStrings(List<String> strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s + "%20");
		}
		return sb.toString();
	}

}
