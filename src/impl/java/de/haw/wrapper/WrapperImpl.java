package de.haw.wrapper;

import de.haw.model.WebPicture;
import de.haw.model.types.Type;
import de.haw.model.types.UserType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

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

        System.out.println("collect results: "+ resultData.size());

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

	public List<WebPicture> getPicturesForPersons(Collection<Type> personsOfInterest) {
		List<WebPicture> result = new ArrayList<WebPicture>();
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
				result.add(new WebPicture(new URL(data.getString("url"))));
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
        // run second collect
        Collection<Type> filterSearchResult = collect(requests);
        /*if(!filterSearchResult.isEmpty()) {
            System.out.println(personsOfInterest.size());
            System.out.println(filterSearchResult.size());
            filterSearchResult.retainAll(personsOfInterest);
            //TODO: this stupid "retains" won't work
        }
        System.out.println("filter results: "+ filterSearchResult.size());
		return filterSearchResult;*/
        Collection<Type> results = new ArrayList<Type>();

        for (Type obj : personsOfInterest) {
            if(filterSearchResult.contains(obj)) {
                results.add(obj);
            }
        }
        System.out.println("Results: "+results.size());
        return results;
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
