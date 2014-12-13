package de.haw.wrapper;

import java.io.*;
import java.util.Properties;

/**
 * @author lotte
 */
public class PropertyHandler {

    private String propertyFileName = "properties.txt";
    private static PropertyHandler instance;
    private Properties props;

    private PropertyHandler() {
        // add file if it doesn't exist
        File propertyFile = new File(propertyFileName);
        if(!propertyFile.exists()) {
            try {
                propertyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        System.out.println(props);
    }

    // TODO: fail if property is not available
    public void setProperty(String key, String property) {
        props.setProperty(key, property);
        storeProps();
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    private void storeProps() {
        try (OutputStream out = new FileOutputStream(propertyFileName)) {
                props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProps() {
        try (InputStream in = new FileInputStream(propertyFileName)) {
            props.load(in);
        } catch  (IOException e){
            e.printStackTrace();
        }
    }
}
