package importers.deckImporter;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import core.Credentials;
import importObjects.Card;
import importObjects.CardDetails;
import importObjects.CardParams;
import importers.cardImporter.AbstractCardImporter;
import org.apache.commons.io.IOUtils;
import org.sqlite.SQLiteConfig;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TappedOutDraftImporter extends AbstractDeckImporter {

    //http://tappedout.net/api/mtg/booster/?set=count&
    private static final String TAPPED_OUT_DRAFT_API_URL = "http://tappedout.net/api/mtg/booster/?";
    private int packCount;
    private HashMap<String, Integer> packDistribution;

    public TappedOutDraftImporter( AbstractCardImporter cardImportMethod, String set, int packCount) {
        this(cardImportMethod, buildSimplePackDist(set,packCount));
    }

    public TappedOutDraftImporter(AbstractCardImporter cardImportMethod,HashMap<String, Integer>  packDistribution){
        super(cardImportMethod);
        this.packDistribution = packDistribution;
    }

    private static HashMap<String,Integer> buildSimplePackDist(String set, int count){
        HashMap<String,Integer> packDistribution = new HashMap<String,Integer>();
        packDistribution.put(set, count);
        return packDistribution;
    }
    private static String buildTappedOutAPIURL(HashMap<String,Integer> packDist, int perPack){
        String result = TAPPED_OUT_DRAFT_API_URL;
        String amp = "";
        for(String set : packDist.keySet()){
            result += set + "="+packDist.get(set)+"&";
        }
        result += "pack_size=" +perPack;
        return result;
    }
    private static String buildTappedOutAPIURL(HashMap<String,Integer> packDist){
        return buildTappedOutAPIURL(packDist,15);
    }


    @Override
    public List<Card> importDeck() throws IOException {
        URL request = new URL(buildTappedOutAPIURL(packDistribution));
        JsonObject root = getJsonFromUrl(request);
        return buildDecklistFromJson(root);
    }

    private static JsonObject getJsonFromUrl(URL request) throws IOException {
        URLConnection urlConnection = request.openConnection();
        InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());
        JsonParser jp = new JsonParser();
        return jp.parse(reader).getAsJsonObject();
    }

    private List<Card> buildDecklistFromJson(JsonObject root){
        List<Card> deckList = new ArrayList<Card>();

        for(String set: packDistribution.keySet()){
            JsonArray setArray = root.getAsJsonArray(set);

            for(int pack =0; pack < setArray.size(); pack++){

                //TODO: temporary, will need to pass through piles somehow more permanant
                String boardStr = CardDetails.Board.MAIN.toString();

                // load cards in each pack
                JsonArray packCards = setArray.get(pack).getAsJsonArray();
                for(int card=0; card< packCards.size(); card++){
                    JsonObject cardObj = packCards.get(card).getAsJsonObject();
                    String name = cardObj.get("name").getAsString();
                    CardParams params = new CardParams.CardParamsBuilder(name).set(set)
                            .board(CardDetails.Board.getFromString(boardStr)).qty(1).build();
                    Card importedCard = this.cardImporter.loadCard(params);
                    importedCard.setDesiredTTSPile("Booster # " + (pack+1));
                    if(importedCard != null)
                        deckList.add(importedCard);
                }
            }
        }
        return deckList;
    }
}


