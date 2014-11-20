package de.haw.parser;

import org.json.JSONObject;

/**
 * Created by Fenja on 19.11.2014.
 */
public interface Parser {

    public JSONObject parse(String naturalLanguage);

}
