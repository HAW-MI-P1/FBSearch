package de.haw.wrapper;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * @author lotte
 */
public class AuthHandler {

    private PropertyHandler propertyHandler;
    private RequestHandler requestHandler;
    private String appID;
    private String appSecret;
    private String oauthAccessToken;
    private String userAccessCode;
    private String redirectUri;
    private int myPort = 8200;

    public AuthHandler() {
        propertyHandler = PropertyHandler.getInstance();
        requestHandler = RequestHandler.getInstance();
    }

    public void  startServer() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(myPort), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext("/test", new ResponseHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public void login() {
        boolean properlyInitialized = true;

        appID = propertyHandler.getProperty("appID");
        appSecret = propertyHandler.getProperty("appSecret");
        oauthAccessToken = propertyHandler.getProperty("oauthAccessToken");


        if (propertyHandler.getProperty("appID") == null) {
            properlyInitialized = false;
            System.err.println("Property appID has not been set in the properties.txt file. Please do so manually or use setProperties() before your call to login()");
        }
        if (propertyHandler.getProperty("oauthAccessToken") == null) {
            properlyInitialized = false;
            System.err.println("Property oauthAccessToken has not been set in the properties.txt file. Please do so manually or use setProperties() before your call to login()");
        }
        if (propertyHandler.getProperty("appSecret") == null) {
            properlyInitialized = false;
            System.err.println("Property appSecret has not been set in the properties.txt file. Please do so manually or use setProperties() before your call to login()");
        }

        if(properlyInitialized && propertyHandler.getProperty("userAccessToken") == null) {
            // let the user authenticate the app *once*
            startServer();
            redirectUri = String.format("http://localhost:%d/test", myPort);
            String requestStr = String.format("https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s", appID, redirectUri);
            System.out.println("Please copy & paste the following into your browser and grant the app access:\n");
            System.out.println(requestStr);
        }
    }

    class ResponseHandler implements HttpHandler {
        public void handle(HttpExchange he) throws IOException {
            System.out.println(he.getResponseBody());
            userAccessCode = he.getRequestURI().getQuery().split("=")[1];//  .getQuery();

            System.out.println("I can haz new access code:");
            System.out.println(userAccessCode);

            // exchange code for access token
            String exchangeStr = String.format("https://graph.facebook.com/oauth/access_token?"+
                                               "client_id=%s"+
                                               "&redirect_uri=%s"+
                                               "&client_secret=%s"+
                                               "&code=%s", appID, redirectUri, appSecret, userAccessCode );

            // TODO: convert to jsonobject instead of clumsily parsing!
            String response = requestHandler.get(exchangeStr);
            String userAccessToken = response.split("=")[1];

            System.out.println("Response:\n"+response);
            System.out.println("ExchangeStr:\n"+exchangeStr);
            System.out.println("userAccessToken:\n"+userAccessToken);

            // TODO error handling
            propertyHandler.setProperty("userAccessToken", userAccessToken);
        }
    }
}
