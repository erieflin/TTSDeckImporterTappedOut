package utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class ScryfallUtils {

    private ScryfallUtils() {
    }


    public static JsonObject getJsonFromURL(String url) {
        try {
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
            request.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            return root.getAsJsonObject();
        } catch(Exception e) {
            return null;
        }
    }

    public static boolean isRegexValid(String regex, String test) {
        return Pattern.compile(regex).matcher(test).find();
    }

    public static boolean isValueInArray(String key, String val, JsonArray ary) {
        JsonObject obj;
        for(int i = 0; i < ary.size(); i++) {
            obj = ary.get(i).getAsJsonObject();
            if(obj.get(key) != null)
                if(obj.get(key).getAsString().equalsIgnoreCase(val))
                    return true;
        }
        return false;
    }

    public static JsonObject getObjectFromArray(String key, String val, JsonArray ary) {
        if(isValueInArray(key, val, ary)) {
            JsonObject obj;
            for(int i = 0; i < ary.size(); i++) {
                obj = ary.get(i).getAsJsonObject();
                if(obj.get(key) != null)
                    if(obj.get(key).getAsString().equalsIgnoreCase(val))
                        return obj;
            }
        }
        return null;
    }


   

    public static HashMap<String, Object> jsonObjectToHashMap(JsonObject jObj) {
        HashMap<String, Object> list = new HashMap<>();

        for(Map.Entry<String, JsonElement> ent : jObj.entrySet()) {
            if(ent.getValue().isJsonPrimitive())
                list.put(ent.getKey(), ent.getValue().getAsString());
            else {
                ArrayList<String> t = new ArrayList<>();
                for(int i = 0; i < ent.getValue().getAsJsonArray().size(); i++) {
                    t.add(ent.getValue().getAsJsonArray().get(i).getAsString());
                }
                list.put(ent.getKey(), t);
            }
        }

        return list;
    }

    private static JsonObject getJsonFromHashMap(HashMap<String, Object> map) {
        JsonObject json = new JsonObject();
        JsonArray ary;
        ArrayList<String> aryLst;

        for(String t : map.keySet()) {
            if(map.get(t).getClass().equals(String.class))
                json.add(t, new JsonPrimitive(((String) map.get(t))));
            else {
                ary = new JsonArray();
                try {
                    aryLst = (ArrayList<String>) map.get(t);
                } catch(Exception e) {
                    continue;
                }
                for(String l : aryLst)
                    ary.add(l);
                json.add(t, ary);
            }
        }

        return json;
    }




}
