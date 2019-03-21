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
    public static final String SCRYFALL_URL = "https://api.scryfall.com/";
    public static final String SCRYFALL_CARD = SCRYFALL_URL + "cards/";
    public static final String SCRYFALL_CARD_NAMED = SCRYFALL_CARD + "named";
    public static final String SCRYFALL_PRECEDE_QUERY = SCRYFALL_CARD_NAMED + "?exact=";
    public static final String SCRYFALL_CARDS_ID = "cards";
    public static final String SCRYFALL_PRINTING_ID = "printings";

    public Scryfall()
    {
        super();
    }

    private static String ScryfallQueryURL(String cardName)
    {
        return SCRYFALL_PRECEDE_QUERY+cardName;
    }

    @Override
    public Card loadCard(CardSetup parameters)
    {
        String url = ScryfallQueryURL(parameters.cardName);
        JsonObject result = Util.getJsonFromURL(url);
        if (result.isJsonArray())
        {
            //Need to select a single version based on the set

            JsonArray cards = result.get(SCRYFALL_CARDS_ID).getAsJsonArray();

            if (cards.size() == 0)
            {
                result = null;
            } else if (Util.NullOrWhitespace(parameters.set))
            {
                result = cards.get(0).getAsJsonObject();
            } else
            {
                JsonArray temp;
                for (int i = 0; i < cards.size(); i++)
                {
                    temp = cards.get(i).getAsJsonObject().get(SCRYFALL_PRINTING_ID).getAsJsonArray();
                    for (int j = 0; j < temp.size(); j++)
                    {
                        String set = temp.get(j).getAsString();
                        if (set.equalsIgnoreCase(parameters.set))
                        {
                            result = cards.get(i).getAsJsonObject();
                        }
                    }
                }
                result = cards.get(0).getAsJsonObject();
            }
        }

        //TODO parse result into a Card, also need to load image (which might need it's own class setup)
        if (result == null)
        {
            return null;
        }

        //TODO implement
        return null;
    }
}
