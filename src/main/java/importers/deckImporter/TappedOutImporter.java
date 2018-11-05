package importers.deckImporter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import core.Constants;
import core.Credentials;
import importers.cardImporter.AbstractCardImporter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TappedOutImporter extends AbstractDeckImporter
{
    public TappedOutImporter(Credentials tappedOutCredentials, AbstractCardImporter cardImportMethod, URL deckURL)
    {
        super(tappedOutCredentials, cardImportMethod, deckURL);
    }

    public TappedOutImporter(Credentials tappedOutCredentials, AbstractCardImporter cardImportMethod, JsonObject deckFile)
    {
        super(tappedOutCredentials, cardImportMethod, deckFile);
    }

    @Override
    protected void importDeckURL(URL deckURL)
    {
        //http://tappedout.net/api/collection/collection:deck/mayael-the-anima-irl/

        String url = deckURL.getPath();
        if(url.endsWith("/"))
            url = url.substring(0, url.length() - 1);

        String[] arr = url.split("/");
        String urlDeckName = arr[arr.length - 1];

        URL request = null;
        try
        {
            request = new URL(Constants.TAPPED_OUT_API_URL + urlDeckName);

            URLConnection urlConnection = request.openConnection();
            urlConnection.setRequestProperty("Cookie", "tapped="+getLoginCookie());

            InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());

            TappedOutDto readDto = new Gson().fromJson(reader, TappedOutDto.class); //TODO dto doesn't work, see error in console

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            //TODO handle
        }
        catch (IOException e)
        {
            e.printStackTrace();
            //TODO handle
        }


    }

    @Override
    protected void importDeckURL(JsonObject deckFile)
    {
        //TODO finish
    }

    private static class TappedOutDto
    {
        String user_display;
        String name;
        String url;
        String featured_card;
        String dateUpdated;
        String score;
        List<List<String>> inventory;
        String resource_uri;
        String thumbnail_url;
        String slug;
        String small_thumbnail_url;
        String user;
    }

    private String getLoginCookie() throws IOException
    {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        Document doc = Jsoup.connect("https://tappedout.net/accounts/login/?next=/").get();

        String csrfmiddlewaretoken = doc.select("input[name=csrfmiddlewaretoken]").first().val();

        CookieStore cookieStore = cookieManager.getCookieStore();

        //getting cookies which returns in the form of List of type HttpCookie
        List<HttpCookie> listOfcookies = cookieStore.getCookies();

        String csrftoken = "";
        String cfduid = "";

        for(HttpCookie httpCookie: listOfcookies)
        {
            String name = httpCookie.getName();
            String value = httpCookie.getValue();

            if(name.contains("cfduid"))
                cfduid = value;
            else if(name.contains("csrftoken"))
                csrftoken = value;
        }

        if(cfduid == "" || csrftoken == "")
            throw new IllegalStateException("Couldn't get both cfduid and csrftoken");

        URL request = new URL("https://tappedout.net/accounts/login/");

        HttpsURLConnection conn = (HttpsURLConnection)request.openConnection();

        String urlParameters  = "next=/&username="+ credentials.getUserName() + "&password=" + credentials.getPassword() + "&csrfmiddlewaretoken="+csrfmiddlewaretoken;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postData.length));
        conn.setRequestProperty("Cookie", "__cfduid=" + cfduid + "; csrftoken=" + csrftoken);
        conn.setRequestProperty("referer","https://tappedout.net/accounts/login/?next=/");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postData);

//        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//
//        for (int c; (c = in.read()) >= 0; )
//            System.out.print((char) c);

        CookieStore cookieStore2 = cookieManager.getCookieStore();

        //getting cookies which returns in the form of List of type HttpCookie
        List<HttpCookie> listOfcookies2 = cookieStore2.getCookies();

        for(HttpCookie httpCookie: listOfcookies2)
        {
            String name = httpCookie.getName();
            String value = httpCookie.getValue();

            if(name.contains("tapped"))
                return value;
        }

        throw new IOException("Was not able to retrieve login cookie");
    }


//    private static class CardDto
//    {
//        String specificCardName;
//        List<CardDtoDetails> details;
//    }
//
//    private static class CardDtoDetails
//    {
//        String board;
//        int quantity;
//    }
}