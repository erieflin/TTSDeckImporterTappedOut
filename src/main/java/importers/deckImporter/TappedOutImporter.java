package importers.deckImporter;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import core.Credentials;
import importObjects.Card;
import importObjects.CardParams;
import importers.cardImporter.AbstractCardImporter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import importObjects.CardDetails.*;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TappedOutImporter extends AbstractUrlDeckImporter
{
    private static final String TAPPED_OUT_API_URL = "http://tappedout.net/api/collection/collection:deck/";
    private static final String TAPPED_OUT_BOARD_KEY = "b";
    private static final String TAPPED_OUT_QTY_KEY = "qty";
    
    private String loginCookie;
    protected Credentials credentials;
    public TappedOutImporter(Credentials tappedOutCredentials, AbstractCardImporter cardImportMethod, URL deckURL) throws IOException
    {
        super(cardImportMethod, deckURL);
        this.credentials = tappedOutCredentials;
    }

    @Override
    public List<Card> importDeck() throws IOException
    {
        String url = deckURL.getPath();
        if(url.endsWith("/"))
            url = url.substring(0, url.length() - 1);

        String[] arr = url.split("/");
        String urlDeckName = arr[arr.length - 1];

        URL request = new URL(TAPPED_OUT_API_URL + urlDeckName);

        URLConnection urlConnection = request.openConnection();
        urlConnection.setRequestProperty("Cookie", "tapped="+getLoginCookie());

        InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());

        TappedOutDto readDto = new Gson().fromJson(reader, TappedOutDto.class);

        List<Card> deckList = new ArrayList<>();

        for(int i = 0; i < readDto.inventory.size(); i++)
        {
            String cardNameWithExtras = (String)(readDto.inventory.get(i).get(0));

            Matcher m = Pattern.compile("(^.*?)(?=(\\*|\\(|$))").matcher(cardNameWithExtras);
            String cardName = m.find() ? m.group(1).trim() : null;

            m = Pattern.compile("(\\(.*\\))").matcher(cardNameWithExtras);
            String set = m.find() ? m.group(1).trim() : null;

            m = Pattern.compile("(\\*.*?\\*)").matcher(cardNameWithExtras);
            List<Tag> modifiers = new ArrayList<>();
            while (m.find())
            {
                String modifier = m.group(1).trim();

                if(modifier.equalsIgnoreCase("*F*"))
                    modifiers.add(Tag.FOIL);
                else if(modifier.equalsIgnoreCase("*CMDR*"))
                    modifiers.add(Tag.COMMANDER);
                else if(modifier.toUpperCase().contains("*A:"))
                    modifiers.add(Tag.ALTER);
            }

            LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>)(readDto.inventory.get(i).get(1));
            Board board = Board.valueOf(((String)map.get(TAPPED_OUT_BOARD_KEY)).toUpperCase());
            int quantity = (int)((double)map.get(TAPPED_OUT_QTY_KEY));

            CardParams cardParams = new CardParams.CardParamsBuilder(cardName).set(set).qty(quantity).modifiers(modifiers).board(board).build();
            Card card = this.cardImporter.loadCard(cardParams);

            if(card != null)
                deckList.add(card);
        }

        //TODO redo what this returns - should return a full deck by parsing what type of deck this is and instantiating accordingly
        return deckList;
    }

    private static class TappedOutDto
    {
        String user_display;
        String name;
        String url;
        String featured_card;
        String dateUpdated;
        String score;

        //[0] = String cardname thingy
        //[1] = LinkedTreeMap, with the following:
            //key b = board ("main" usually)
            //key qty = double of quantity (1.0 usually)
        List<List<Object>> inventory;

        String resource_uri;
        String thumbnail_url;
        String slug;
        String small_thumbnail_url;
        String user;
    }

    private String getLoginCookie() throws IOException
    {
        if(loginCookie != null)
        {
            return loginCookie;
        }

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        Document doc = Jsoup.connect("https://tappedout.net/accounts/login/?next=/").get(); //TODO look into making this a constant

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

        URL request = new URL("https://tappedout.net/accounts/login/"); //TODO look into making this a constant

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

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

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
            {
                loginCookie = value;
                return loginCookie;
            }
        }

        throw new IOException("Was not able to retrieve login cookie");
    }
}