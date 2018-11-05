package core;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class GettingCookies {

    public static void main(String[] args) {

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        //creates url for the given string
        URL url = null;
        try {
            url = new URL("http://tappedout.net");

            //open's a connection with the url specified and returns URLConnection object
            URLConnection  urlConnection = url.openConnection();
            // get's the contents from this url specifies
            urlConnection.getContent();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //returns the cookie store(bunch of cookies)
        CookieStore cookieStore = cookieManager.getCookieStore();

        //getting cookies which returns in the form of List of type HttpCookie
        List<HttpCookie> listOfcookies = cookieStore.getCookies();

        for(HttpCookie httpCookie: listOfcookies)
        {
            System.out.println("Cookie Name : "+httpCookie.getName()+" Cookie Value : "+httpCookie.getValue());
        }
    }

}