package importers.cardImporter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import core.Constants;
import core.Util;
import importObjects.Card;
import importObjects.CardSetup;
import importObjects.DoubleFacedCard;

public class Scryfall extends AbstractCardImporter
{
    public Scryfall()
    {
        super();
    }

    private static String ScryfallQueryURL(String cardName)
    {
        return Constants.SCRYFALL_PRECEDE_QUERY+cardName;
    }

    //Old reference code for development purposes
    private static JsonObject query(String cardName, String setName)
    {
        String url = ScryfallQueryURL(cardName);
        JsonObject result =  Util.getJsonFromURL(url);
        if(result.isJsonArray()){
            JsonArray cards = result.get(Constants.SCRYFALL_CARDS_ID).getAsJsonArray();
            result = checkSet(cards, setName);
        }
        return result;
    }

    //Old reference code for development purposes
    private static JsonObject checkSet(JsonArray cards, String targetSet)
    {
        JsonArray temp;

        if(cards.size()==0){
            return null;
        }

        if(targetSet.trim().equalsIgnoreCase("")){
            return cards.get(0).getAsJsonObject();
        }

        for(int i = 0; i < cards.size(); i++) {
            temp = cards.get(i).getAsJsonObject().get(Constants.SCRYFALL_PRINTING_ID).getAsJsonArray();
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
    public Card loadCard(CardSetup setup)
    {
        //TODO implement
        return null;
    }
}
