package de.haw.wrapper;

import de.haw.db.SearchDB;
import de.haw.db.SearchDBImpl;
import de.haw.model.WebPicture;
import de.haw.model.exception.IllegalArgumentException;
import de.haw.model.types.*;

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

	// additional DB
	SearchDB searchDB;

	public WrapperImpl() {
		requestHandler = RequestHandler.getInstance();
		searchDB = new SearchDBImpl();
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
		if (requests == null) {
			throw new IllegalArgumentException("requests was null");
		}

		Collection<Type> resultData = new ArrayList<Type>();
		String dataType = null;
		ResultType resultType = null;

		// Get type of data to search for
		try {
			dataType = requests.getString("type");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Get places from requests
		List<String> places = getValues(requests, "place");

		// Get names from request
		List<String> names = getValues(requests, "name");

		// Start a request for names and add the response to the result data
		if (names.size() > 0) {
			JSONObject namesResponse = null;
			if (dataType.equals(ResultType.User.getName())) {
				resultType = ResultType.User;
				namesResponse = requestHandler
						.searchForUser(concatStrings(names));
			} else if (dataType.equals("thing")) { // TODO Parser should place
													// "page" here
				resultType = ResultType.Page;
				namesResponse = requestHandler
						.searchForPage(concatStrings(names));
			} else if (dataType.equals(ResultType.Place.getName())) {
				resultType = ResultType.Place;
				namesResponse = requestHandler
						.searchForPlace(concatStrings(names));
			}
			resultData.addAll(transformResponse(namesResponse));

			resultData.addAll(searchDB.getEntries(resultType, names));
		}

		// If a place exist, check all users
		if (places.size() > 0) {
			// findLocations(resultData);
			List<Type> results = new ArrayList<>();
			if (dataType.equals(ResultType.User.getName())) {
				String city;
				for (Type user : resultData) {
					city = ((UserType) user).getCity();
					if (city != null && !city.isEmpty()) {
						Iterator<String> iterator = places.iterator();
						while (iterator.hasNext()) {
							String s = iterator.next().toString().toLowerCase();
							if (city.toLowerCase().contains(s)) {
								results.add(user);
							}
						}
					}
				}
			} else if (dataType.equals(ResultType.Place.getName())) {
				LocationType location;
				for (Type place : resultData) {
					location = ((PlaceType) place).getLocation();
					if (location != null) {
						Iterator<String> iterator = places.iterator();
						while (iterator.hasNext()) {
							String s = iterator.next().toString().toLowerCase();
							if ((location.getCity().toLowerCase().contains(s))
									|| (location.getStreet().toLowerCase()
											.contains(s))
									|| (location.getState().toLowerCase()
											.contains(s))
									|| (location.getCountry().toLowerCase()
											.contains(s))) {
								results.add(place);
							}
						}
					}
				}
			}
			resultData = results;

		}
		System.out.println("collect results: " + resultData.size());

		return resultData;
	}

	public Collection<Type> searchForName(String type, List<String> names) {
		if (type == null || names == null) {
			throw new IllegalArgumentException("type or names was null");
		}

		Collection<Type> resultData = new ArrayList<Type>();
		if (names.size() > 0) {
			JSONObject namesResponse = requestHandler.search(
					concatStrings(names), type);
			resultData.addAll(transformResponse(namesResponse));
		}
		return resultData;
	}

	public List<WebPicture> getPicturesForPersons(
			Collection<Type> personsOfInterest) {
		if (personsOfInterest == null) {
			throw new IllegalArgumentException("personsOfIntrest was null");
		}

		List<WebPicture> result = new ArrayList<WebPicture>();
		StringBuilder userIDs = new StringBuilder();
		int count = 0;
		for (Type p : personsOfInterest) {

			// FB IDs are limited to 50
			count++;
			if (count > 50) {
				break;
			}

			// IDs are comma separated
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
		if (id == null || location == null) {
			throw new IllegalArgumentException("id or location was null");
		}
		if (id.length() == 0 || location.length() == 0) {
			return false;
		}
		if (!id.matches("[0-9]+")) {
			throw new IllegalArgumentException(
					"id contains non numerical characters...");
		}

		String name = null;
		JSONObject userResponse = requestHandler.searchForID(id, "location");
		try {
			JSONObject locationObject = userResponse.getJSONObject("location");
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
		if (requests == null || personsOfInterest == null) {
			throw new IllegalArgumentException(
					"requests or personsOfIntrest was null");
		}

        Collection<Type> results = new ArrayList<Type>();
        List<String> names = getValues(requests, "name");

		// run second collect
		Collection<Type> filterSearchResult;
        if(names.size() > 0) {
            filterSearchResult = collect(requests);
            results = new ArrayList<Type>();
            for (Type obj : personsOfInterest) {
                if (filterSearchResult.contains(obj)) {
                    results.add(obj);
                }
            }
        }else{
            String dataType = null;
            try {
                dataType = requests.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<String> places = getValues(requests, "place");
            if(places.size() > 0) {
                if (dataType.equals(ResultType.User.getName())) {
                    for (Type t : personsOfInterest) {
                        if (((UserType)t).getCity() != null &&((UserType)t).getCity().toLowerCase().equals(places.get(0).toLowerCase())) {
                            results.add(t);
                        }
                    }
                } else if (dataType.equals("thing")) { // TODO Parser should use place
                    for (Type t : personsOfInterest) {
                        if (((PlaceType) t).getLocation().getCity().equals(places.get(0))) {
                            results.add(t);
                        }
                    }
                }
            }
        }
		System.out.println("Results: " + results.size());
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

	private void findLocations(Collection<Type> resultData) {
		// No places were ever found via FB API
		// only search in SearchDB to save time
		for (Type t : resultData) {
			String name = null;
			JSONObject userResponse = requestHandler.searchForID(t.getID(), "");
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
}
