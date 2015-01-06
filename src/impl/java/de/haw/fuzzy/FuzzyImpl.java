package de.haw.fuzzy;

import de.haw.model.exception.ConnectionException;
import de.haw.model.exception.IllegalArgumentException;
import de.haw.model.exception.InternalErrorException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class FuzzyImpl implements Fuzzy {
	
	// Public static attributes
	
	public static final int ALL             = 0x0F;
	public static final int SYNSETS         = 0x01;
	public static final int SIMILARTERMS    = 0x02;
	public static final int SUBSTRINGTERMS  = 0x04;
	public static final int STARTSWITHTERMS = 0x08;

	
	// Private attributes
	
	private URL url;
	private HttpURLConnection connection;
	
	private String urlParameters;
	private String targetURL;
	
	private final String API_URL       = "https://www.openthesaurus.de/synonyme/search";
	private final String MODE_ALL      = "mode=all";
	private final String DATATYPE_JSON = "format=application/json";
	private final String DATATYPE_XML  = "format=text/xml";
	
	// Public operations

    /**
     *
     * @param word to find synonyms for.
     * @param opt set the type of fuzzy search. <br>
     * 0x01: Search for synonym sets<br>
     * 0x02: Search for similar words<br>
     * 0x04: Search for substrings containing this words<br>
     * 0x08: Search for the words at the beginning<br>
     * 0x0F: Enable all search options<br>
     * @return
     */
	@Override
	public Collection<String> getSynonym(String word, int opt) {
		if( word == null || word.equals("")){ throw new IllegalArgumentException("Illegal word: "  + word); }
		
		word = word.toLowerCase();
		
		// 1. translate English input to German
		String[] r = translate(word,"en","de");

		// 2. find synonym
        List<String> resList = new ArrayList<String>();
		for(String w : r){
			try{
				this.targetURL = API_URL + "?q=" + this.makeUTF8(w);
			} 
			catch (Exception e)
			{throw new InternalErrorException(""); }
			
			String[] a = this.getSynonymXML(w, opt);
			
			for (String aPart : a) {
				// 3. re-translate words
				String[] r2 = translate(aPart,"de","en");
				
				for (String r2Part : r2) {
					resList.add(r2Part);
				}
				
			}
			
		}
		
		for (String string : resList) {
			System.out.println(string);
		}
		
		return resList;
//		return this.getSynonymJSON(word, opt);
		
	}
	
	
	// Private operations
	
	
	private String[] translate(String word, String from, String to){
		String param = "action=query&prop=iwlinks&format=json&iwlimit=30&iwprefix=" + to + "&titles=" + word;
		String targetURLs = "http://" + from + ".wiktionary.org/w/api.php";
		
		try {
			// Create connection
			this.url = new URL(this.makeUTF8(targetURLs));

			this.connection = (HttpURLConnection) url.openConnection();
			this.connection.setRequestMethod("GET");
			this.connection.setRequestProperty("Content-Type",
					"application/json");

			this.connection.setRequestProperty("Content-Length",
					"" + Integer.toString(targetURLs.length()));
			this.connection.setRequestProperty("Content-Language", "en-US");

			this.connection.setUseCaches(false);
			this.connection.setDoInput(true);
			this.connection.setDoOutput(true);

			// Send request
			
			DataOutputStream wr = null;
			
			try{
				wr = new DataOutputStream(
						this.connection.getOutputStream());
			}
			catch(Exception e){ throw new ConnectionException(); }
			
			wr.writeBytes(param);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = this.connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			
			System.out.println("Fuzzy still working");
			
			LinkedList<String> resList = new LinkedList<String>();
			try {
				JSONObject obj = new JSONObject(response.toString());
				JSONObject pages = obj.getJSONObject("query").getJSONObject("pages");
				@SuppressWarnings("rawtypes")
				Iterator keys = pages.keys();
				while (keys.hasNext()) {
					Object key = keys.next();
					if(key.toString().equals("-1")){break;}
					
					JSONArray res = pages.getJSONObject(key.toString()).getJSONArray("iwlinks");
					
					for (int i = 0; i < res.length(); i++) {
						resList.add(res.getJSONObject(i).getString("*").replace("Special:Search/", ""));
					}
					
				}
				
			} catch (JSONException e1) {
				// No match found
				System.err.println("Fuzzy: No translation found!");
			}
			
			String[] result = new String[0];
			
			
			return resList.toArray(result);

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			if (this.connection != null) {
				this.connection.disconnect();
			}
		}
	}
	
	private String[] getSynonymXML(String word, int opt) {
		this.urlParameters = this.MODE_ALL + "&" + this.DATATYPE_XML;

		HashMap<String, String> results = new HashMap<String, String>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(this.getSearch()));
			doc = db.parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		if ((opt & FuzzyImpl.SYNSETS) == FuzzyImpl.SYNSETS) {
			this.getTermXML(doc.getElementsByTagName("synset"), results);
		}

		if ((opt & FuzzyImpl.SIMILARTERMS) == FuzzyImpl.SIMILARTERMS) {
			this.getTermXML(doc.getElementsByTagName("similarterms"),
					results);
		}

		if ((opt & FuzzyImpl.SUBSTRINGTERMS) == FuzzyImpl.SUBSTRINGTERMS) {
			this.getTermXML(doc.getElementsByTagName("substringterms"),
					results);
		}

		if ((opt & FuzzyImpl.STARTSWITHTERMS) == FuzzyImpl.STARTSWITHTERMS) {
			this.getTermXML(doc.getElementsByTagName("startswithterms"),
					results);
		}

		return results.keySet().toArray(new String[0]);
	}

	@SuppressWarnings("unused")
	private String[] getSynonymJSON(String word, int opt) {
		this.urlParameters = this.MODE_ALL + "&" + this.DATATYPE_JSON;

		HashMap<String, String> results = new HashMap<String, String>();

		JSONObject obj = null;
		try {
			obj = new JSONObject(this.getSearch());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		if ((opt & FuzzyImpl.SYNSETS) == FuzzyImpl.SYNSETS) {
			try {
				this.getTermJSON(obj.getJSONArray("synsets"), results);
			} catch (JSONException e) {
			}
		}

		if ((opt & FuzzyImpl.SIMILARTERMS) == FuzzyImpl.SIMILARTERMS) {
			try {
				this.getTermJSON(obj.getJSONArray("similarterms"), results);
			} catch (JSONException e) {
			}
		}

		if ((opt & FuzzyImpl.SUBSTRINGTERMS) == FuzzyImpl.SUBSTRINGTERMS) {
			try {
				this.getTermJSON(obj.getJSONArray("substringterms"), results);
			} catch (JSONException e) {
			}
		}

		if ((opt & FuzzyImpl.STARTSWITHTERMS) == FuzzyImpl.STARTSWITHTERMS) {
			try {
				this.getTermJSON(obj.getJSONArray("startswithterms"), results);
			} catch (JSONException e) {
			}
		}

		return results.keySet().toArray(new String[0]);
	}

	private void getTermXML(NodeList nodes, HashMap<String, String> results) {
		for (int j = 0; j < nodes.getLength(); j++) {
			NodeList children = ((Element) nodes.item(j))
					.getElementsByTagName("term");

			for (int i = 0; i < children.getLength(); i++) {
				Element element = (Element) children.item(i);

				String tmp = element.getAttribute("term");

				tmp = cleanBraces(tmp);
				tmp = cleanSpaces(tmp);
				
				results.put(tmp, tmp);
			}
		}
	}

	private void getTermJSON(JSONArray arr, HashMap<String, String> results) {
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = null;
			try {
				obj = arr.getJSONObject(i);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			try {
				String tmp = obj.getString("term");

				tmp = cleanBraces(tmp);
				tmp = cleanSpaces(tmp);

				results.put(tmp, tmp);

			} catch (JSONException e) {
			} // term not exist
			try {
				JSONArray terms = obj.getJSONArray("terms");
				this.getTermJSON(terms, results);
			} catch (JSONException e) {
			} // term not exist
		}
	}

	private String getSearch() {

		try {
			// Create connection
			this.url = new URL(this.makeUTF8(this.targetURL));
			this.connection = (HttpURLConnection) url.openConnection();
			this.connection.setRequestMethod("POST");
			this.connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			this.connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			this.connection.setRequestProperty("Content-Language", "en-US");

			this.connection.setUseCaches(false);
			this.connection.setDoInput(true);
			this.connection.setDoOutput(true);

			// Send request
			
			DataOutputStream wr = null;
			
			try{
				wr = new DataOutputStream(
						this.connection.getOutputStream());
			}
			catch(Exception e){ throw new ConnectionException(); }
			
			wr.writeBytes(this.urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = this.connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			if (this.connection != null) {
				this.connection.disconnect();
			}
		}
	}

	private String makeUTF8(final String toConvert) {
		return toConvert.replaceAll("Ä", "%c3%84").replaceAll("Ö", "%c3%96")
				.replaceAll("Ü", "%c3%9c").replaceAll("ä", "%c3%a4")
				.replaceAll("ö", "%c3%b6").replaceAll("ü", "%c3%bc");
	}

	private String cleanBraces(String words) {
		while (words.indexOf("(") > -1) {
			String replStr = words.substring(words.indexOf("("),
					words.indexOf(")") + 1);
			words = words.replace(replStr, "");
		}
		return words;
	}

	private String cleanSpaces(String words) {
		if (words.equals("")) {
			return "";
		}
		if (words.indexOf(" ") == 0) {
			words = words.replaceFirst(" ", "");
		}
		if (words.substring(words.length() - 1, words.length()).equals(" ")) {
			words = words.substring(0, words.length() - 1);
		}
		return words;
	}

}
