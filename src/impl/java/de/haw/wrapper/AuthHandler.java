package de.haw.wrapper;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

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
            // https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/v2.2
            redirectUri = String.format("http://localhost:%d/test", myPort);
            String requestStr = String.format("https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s&response_type=code", appID, redirectUri);

            String osName = System.getProperty("os.name");

            if (osName.contains("Mac OS")) {
                String[] cmd = {"open", requestStr};
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Please copy & paste the following into your browser and grant the app access:\n");
                System.out.println(requestStr);
            }

        }
    }

    class ResponseHandler implements HttpHandler {
        public void handle(HttpExchange he) {
            try {
                String response = "Success";
                he.sendResponseHeaders(200, response.length());
                he.getResponseBody().write(response.getBytes());
                he.close();

                URI uri = he.getRequestURI();

                userAccessCode = RequestHandler.getQueryParameter("code", uri.getQuery());
                System.out.println("Facebook gave us a user access code: " + userAccessCode);

                // https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/v2.2#exchangecode
                String exchangeStr = String.format("https://graph.facebook.com/oauth/access_token?" +
                        "client_id=%s" +
                        "&redirect_uri=%s" +
                        "&client_secret=%s" +
                        "&code=%s", appID, redirectUri, appSecret, userAccessCode);

                String userAccessTokenResponse = requestHandler.getRaw(exchangeStr);
                String userAccessToken = RequestHandler.getQueryParameter("access_token", userAccessTokenResponse);

                System.out.println("Acquired user access token: " + userAccessToken);

                if(userAccessToken == null) {
                    System.err.println("Something went wrong :(");
                } else {
                    propertyHandler.setProperty("userAccessToken", userAccessToken);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
