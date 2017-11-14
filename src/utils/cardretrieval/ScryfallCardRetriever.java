package utils.cardretrieval;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import core.Config;
import core.FrogCard;
import utils.Constants;
import utils.FrogUtils;
import utils.ImageUtils;
import utils.ScryfallUtils;

public class ScryfallCardRetriever extends CardRetriever {

    private static final String REGEX_REMOVE = "^(http(s|.{0}):\\/\\/)?scryfall\\.com\\/saved\\/share(.{0}|.json|.text)\\?id=";
    private static final String REGEX_VALIDATE = "^(http(s|.{0}):\\/\\/)?scryfall\\.com\\/saved\\/share(.{0}|.json|.text)\\?id=(.{3,4}\\:\\d{1,3}\\/?)+";
    private static final String CARD_FORMAT_REG = ".{3,4}\\:\\d{1,3}\\/?";
  
    
    public static JsonObject getCardByName(String name) throws UnsupportedEncodingException{
    	return getCardByName(name,"");
    }
    public static JsonObject getCardByName(String name, String set) throws UnsupportedEncodingException{

    	String tempName = name.replaceAll("\\s+","+");
    	if(name.contains("/"))
    	{
    		Pattern pattern = Pattern.compile("(.+\\()(.+\\/.+)(\\))");
        	Matcher matcher = pattern.matcher(tempName);
        	if(matcher.find())
        	{
        		tempName = matcher.group(2).replaceAll("\\/", "+\\/\\/+");
        	}
    	}
    	
    	String query = "?exact="+ tempName;
    	
    	String url = Constants.SCRYFALL_CARD_NAMED+query;
    	JsonObject result =  ScryfallUtils.getJsonFromURL(url);
    	if(result.isJsonArray()){
    		JsonArray cards = result.get(Constants.CARDS_ID).getAsJsonArray();
    		result = checkSet(cards,set);
    	}
    	
    	return result;
    }
    private static JsonObject checkSet(JsonArray cards, String targetSet){
        JsonArray temp;
        
        if(cards.size()==0){
      	  return null;
        }
        
        if(targetSet.trim().equalsIgnoreCase("")){
        	return cards.get(0).getAsJsonObject();
        }
        
        for(int i = 0; i < cards.size(); i++) {
      	  temp = cards.get(i).getAsJsonObject().get(Constants.PRINTING_ID).getAsJsonArray();
              for(int j = 0; j < temp.size(); j++) {
                String set = temp.get(j).getAsString();
                if(set.equalsIgnoreCase(targetSet)){
              	 return cards.get(i).getAsJsonObject();
                }
              }
        }
        return cards.get(0).getAsJsonObject();
  }

	@Override
	public boolean LoadCard(FrogCard card) {
		boolean success = true;
		success = LoadCard(card, false) && success;
		if(card.transformName != null) success = LoadCard(card, true) && success;
		return success;
	}
	
	public boolean LoadCard(FrogCard card, boolean isBack){
		String cardKey = isBack ? card.transformCardKey : card.cardKey;
		String cardName = isBack ? card.transformName : card.name;
		String cleanCardName = FrogUtils.ReplaceHardChars(cardName);
		if(cardKey == null){
			cardKey = cleanCardName;
		}
		cardKey = cardKey.replaceAll("/", "");
		String imageFileName = Config.imageDir+cardKey.toLowerCase().trim();
		imageFileName = imageFileName.replaceAll("<", "").replaceAll(">", "").replaceAll("\\[", "").replaceAll("\\]", "")
				.replaceAll("\\{", "").replaceAll("\\}", "")
				+".jpg";
		try{			
			if(isBack) card.transformImageFileName = imageFileName;
			else card.imageFileName = imageFileName;
			System.out.println(imageFileName);
			if(new File(imageFileName).exists()) return true;
			if(HandleHardCard(cardName, imageFileName)) return true;
			if(HandleHardCard(cleanCardName, imageFileName)) return true;

			JsonObject jsonCard = getCardByName(cardName,card.set);
			if(!jsonCard.get("layout").getAsString().equals("split") && (card.transformName != null || jsonCard.has(Constants.CARD_FACES_ID))){
				JsonArray faces = jsonCard.get(Constants.CARD_FACES_ID).getAsJsonArray();
				
				if(!isBack){
					jsonCard = faces.get(0).getAsJsonObject();
				}else{
					jsonCard = faces.get(1).getAsJsonObject();
				}
				if(card.transformName == null){
					card.transformName = faces.get(1).getAsJsonObject().get(Constants.NAME_ID).getAsString();
				}
			}
		    JsonObject images = jsonCard.get(Constants.IMAGE_URI_ID).getAsJsonObject(); 
			String url = images.get(Constants.SIZE_ID).getAsString();
			return ImageUtils.SaveImage(url, imageFileName, 1.0);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Failed to download \""+imageFileName+"\"");
			LoadFailed(card);
			return false;
		}
	}
	
	public static String FindSet(String language, String printing, String html){
		String set = "";
		String regexStr = "(?i)/(..?.?.?.?)/"+language+"/"+printing+".html";
		Pattern regex = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);

		try{
			Matcher matcher = regex.matcher(html);
			matcher.find();
			if(matcher.groupCount() > 0){
				set = matcher.group(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return set;
	}
	


	public static boolean HandleHardCard(String cardName, String imageFileName){
		for(String[] hardPair : Config.hardUrls){
			if(hardPair[0].equalsIgnoreCase(cardName)){
				if(!new File(hardPair[1]).exists()) ImageUtils.SaveImage(hardPair[1], imageFileName, 1.0);
				return true;
			}
		}
		return false;
	}

}
