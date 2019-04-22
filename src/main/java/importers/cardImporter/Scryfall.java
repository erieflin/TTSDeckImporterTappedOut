package importers.cardImporter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import core.Util;
import importObjects.Card;
import importObjects.CardParams;
import importObjects.DoubleFacedCard;
import importObjects.Scryfall.CardFaceDTO;
import importObjects.Scryfall.ScryfallCardDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Scryfall extends AbstractCardImporter
{
    public static final String SCRYFALL_URL = "https://api.scryfall.com/";
    public static final String SCRYFALL_CARD = SCRYFALL_URL + "cards/";
    public static final String SCRYFALL_CARD_NAMED = SCRYFALL_CARD + "named";
    public static final String SCRYFALL_PRECEDE_QUERY = SCRYFALL_CARD_NAMED + "?exact=";
    public static final String SCRYFALL_CARDS_ID = "cards";
    public static final String SCRYFALL_PRINTING_ID = "printings";
    private static final String DEFAULT_SIZE = "normal";
    private static final String CARD_IMAGE_FOLDER = "cardImages";
    private static final Gson gson = new Gson();
    public Scryfall()
    {
        super();
    }

    private static String ScryfallQueryURL(String cardName)
    {
        try {
            return SCRYFALL_PRECEDE_QUERY+URLEncoder.encode(cardName, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SCRYFALL_PRECEDE_QUERY+cardName;
        }
    }

    @Override
    protected String getSourceName()
    {
        return "Scryfall";
    }

    @Override
    protected Card loadCardFromImporter(CardParams parameters)
    {
        String url = ScryfallQueryURL(parameters.cardName);
        JsonObject result = Util.getJsonFromURL(url);

        if (result.isJsonArray()) {
            //Need to select a single version based on the set
            result = getCorrectCardBySet(result, parameters.set);
        }

        if (result == null)
        {
            return null;
        }

        ScryfallCardDTO scryfallCard = gson.fromJson(result,ScryfallCardDTO.class);

        List<CardFaceDTO> faces = scryfallCard.getCardFaces();
        Card importedCard;
        if(faces != null && faces.size()>0) {
            List<CardFaceIODTO> cardFaceIoDetails = new ArrayList<CardFaceIODTO>();

            faces.forEach(face -> {
                HashMap<String, String> imageUris = face.getImageUris();
                String uri = getUriFromImageUris(imageUris);

                if(uri!=null){
                    File imageDestFile =getFileForCard(face.getName(), scryfallCard.getSet());
                    CardFaceIODTO cardFaceIoDetail = new CardFaceIODTO(uri,imageDestFile);
                    cardFaceIoDetails.add(cardFaceIoDetail);
                }
            });

            //TODO: populate these cleaner if a card ever has more than two somehow

            if(cardFaceIoDetails.size()<2){
                return null;
            }

            CardFaceIODTO frontCard = cardFaceIoDetails.get(0);
            if(!frontCard.getDestFile().exists()) {
                if (!downloadCardImageToFile(frontCard.getDestFile(), frontCard.getUri())) {
                    return null;
                }
            }


            CardFaceIODTO backCard= cardFaceIoDetails.get(1);
            if(!backCard.getDestFile().exists()) {
                if (!downloadCardImageToFile(backCard.getDestFile(), backCard.getUri())) {
                    return null;
                }
            }

            importedCard = new DoubleFacedCard(parameters, frontCard.getDestFile(), backCard.getDestFile());

        }else{

            HashMap<String, String> imageUris = scryfallCard.getImageUris();

            String uri = getUriFromImageUris(imageUris);

            if(uri== null){
                return null;
            }

            File image = getFileForCard(scryfallCard.getName(), scryfallCard.getSet());
            if(!image.exists()) {
                if (!downloadCardImageToFile(image, uri)) {
                    return null;
                }
            }

            importedCard = new Card(parameters, image);
        }

        return importedCard;
    }

    private File getFileForCard(String name, String set){

        File destFile = new File(CARD_IMAGE_FOLDER + File.separator + name + "_" + set + ".png");
        if(!destFile.getParentFile().exists()){
            destFile.getParentFile().mkdirs();
        }
        return destFile;
    }

    private boolean downloadCardImageToFile(File destFile, String uri){
        try(InputStream in = new URL(uri).openStream()){
            Files.copy(in, destFile.toPath());
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private JsonObject getCorrectCardBySet(JsonObject input, String requestedSet){
        JsonObject result;
        JsonArray cards = input.get(SCRYFALL_CARDS_ID).getAsJsonArray();
        if(cards == null){
            return null;
        }
        if (cards.size() == 0)
        {
            result = null;
        } else if (Util.NullOrWhitespace(requestedSet))
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
                    if (set.equalsIgnoreCase(requestedSet))
                    {
                        result = cards.get(i).getAsJsonObject();
                    }
                }
            }
            result = cards.get(0).getAsJsonObject();
        }
        return result;
    }

    private String getUriFromImageUris(HashMap<String,String> imageUris){
        if(imageUris == null || imageUris.size() == 0){
            return null;
        }
        if(imageUris.containsKey(DEFAULT_SIZE)){
            return imageUris.get(DEFAULT_SIZE);
        }else{
            Iterator keys= imageUris.keySet().iterator();
            if(keys.hasNext()){
                return imageUris.get(keys.next());
            }
        }
        return null;
    }
}

class CardFaceIODTO{
    private String uri;
    private File destFile;
    public CardFaceIODTO(){}
    public CardFaceIODTO(String uri, File destFile){
        this.uri = uri;
        this.destFile = destFile;
    }
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public File getDestFile() {
        return destFile;
    }

    public void setDestFile(File destFile) {
        this.destFile = destFile;
    }
}
