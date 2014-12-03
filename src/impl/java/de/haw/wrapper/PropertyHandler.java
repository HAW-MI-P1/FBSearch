package de.haw.wrapper;

import java.io.*;
import java.util.Properties;

/**
 * @author lotte
 */
public class PropertyHandler {

    private String propertyFile;
    private static PropertyHandler instance;
    private Properties props;

    private PropertyHandler() {
        propertyFile = "properties.txt";
        props = new Properties();
        loadProps();
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
        props.setProperty("appID", appID);
        props.setProperty("appSecret", appSecret);
        props.setProperty("oauthAccessToken", oauthAccessToken);
        storeProps();
    }

    // TODO: fail if property is not available
    public void setProperty(String key, String property) {
        props.setProperty(key, property);
        System.out.println(props);
        System.out.println(key);
        System.out.println(property);
        storeProps();
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    private void storeProps() {
        try (OutputStream out = new FileOutputStream(propertyFile)) {
                props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProps() {
        try (InputStream in = new FileInputStream(propertyFile)) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
