package de.haw.wrapper;

import java.io.*;
import java.util.Properties;

/**
 * Created by lotte on 28.10.14.
 */
public class PropertyHandler {

    /* TODO: make sure old values are overwritten */

    private static PropertyHandler instance;
    private Properties props;
    private Writer writer;
    private Reader reader;

    private PropertyHandler() {
        try {
            reader = new FileReader( "properties.txt" );
            writer = new FileWriter("properties.txt");
            props = new Properties();
            props.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyHandler getInstance() {
        if (instance == null)
            instance = new PropertyHandler();
        return instance;
    }

    /*
    * Create properties file with login credentials so they don't accidentally get pushed somewhere. Only needs to be
    * called once.
    * */
    public void setInitialProperties(String appID, String appSecret, String oauthAccessToken){
        try {
            props.setProperty("appID", appID);
            props.setProperty("appSecret", appSecret);
            props.setProperty("oauthAccessToken", oauthAccessToken);
            props.store(writer, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: fail if property is not available
    public void setProperty(String key, String property) {
        props.setProperty(key, property);
        try {
            props.store(writer, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

}
