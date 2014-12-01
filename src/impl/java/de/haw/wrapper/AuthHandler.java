package de.haw.wrapper;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * Created by lotte on 28.10.14.
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

        appID = propertyHandler.getProperty("appID");
        appSecret = propertyHandler.getProperty("appSecret");
        oauthAccessToken = propertyHandler.getProperty("oauthAccessToken");
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
        startServer();
        redirectUri = String.format("http://localhost:%d/test", myPort);
        String requestStr = String.format("https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s", appID, redirectUri);
        System.out.println("Please copy & paste the following into your browser and grant the app access:\n");
        System.out.println(requestStr);
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
            System.out.println(exchangeStr);
            String response = requestHandler.get(exchangeStr);
            System.out.println(response);

            // TODO error handling
            propertyHandler.setProperty("userAccessToken", response.split("=")[1]);
        }
    }
}
