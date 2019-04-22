package importObjects.deck;

import core.Constants;
import exportObjects.ImagePage;
import exportObjects.TTS_Card;
import importObjects.Card;
import importObjects.DoubleFacedCard;
import importObjects.Token;
import importers.deckImporter.AbstractDeckImporter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public abstract class AbstractDeck
{
    private String name;
    private AbstractDeckImporter deckImporter;
    private List<Card> cardList;
    private List<Token> tokenList;
    private URL sleeveImageUrl;
    private static final String DECKFOLDER = "DeckOutput";
    private double imageOutCompressionLevel = .25; // default output image compression config for this deck
    private List<String> hostedImageUrls = new ArrayList<String>();
    private HashMap<String, List<TTS_Card>> ttsDeckMap = new HashMap<String, List<TTS_Card>>();

    protected AbstractDeck(AbstractDeckImporter importer, URL sleeveUrl)
    {
//        name = importer.deckName; //TODO fill in
        deckImporter = importer;
        cardList = new ArrayList<Card>();
        tokenList = new ArrayList<Token>();
        sleeveImageUrl = sleeveUrl;
    }

    protected AbstractDeck(AbstractDeckImporter importer) throws MalformedURLException
    {
        this(importer, new URL(Constants.DEFAULT_SLEEVE_IMAGE_URL));
    }

    //TODO either here or somewhere else, some constructor that pulls from a file (probably somewhere else using enum of sorts for deck type and whatnot)

    protected void addCard(Card card)
    {
        cardList.add(card);
    }

    public void importDeck()
    {
        //TODO implement
        try {
           this.cardList = deckImporter.importDeck();
        }catch(Exception e){
            e.printStackTrace();
        }
        return;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public List<DoubleFacedCard> getTransformList(){
        List<DoubleFacedCard> cardList = new ArrayList<DoubleFacedCard>();
        int count = 0;
        for(Card card: this.cardList){
            if(card instanceof DoubleFacedCard){
               cardList.add((DoubleFacedCard) card);
            }
        }
        return cardList;
    }

    public File getDeckFolder(){
       File deckFolder =  new File(DECKFOLDER);

       if(!deckFolder.exists()){
           deckFolder.mkdirs();
       }

       File currentDeckFolder = new File(deckFolder + File.separator + name);
       currentDeckFolder.mkdirs();

       return  currentDeckFolder;
    }

    public double getImageOutCompressionLevel() {
        return imageOutCompressionLevel;
    }

    public void setImageOutCompressionLevel(double imageOutCompressionLevel) {
        this.imageOutCompressionLevel = imageOutCompressionLevel;
    }

    public List<String> getHostedImageUrls() {
        return hostedImageUrls;
    }

    public void setHostedImageUrls(List<String> hostedImageUrls) {
        this.hostedImageUrls = hostedImageUrls;
    }

    public HashMap<String, List<TTS_Card>> getTTSDeckMap() {
        return ttsDeckMap;
    }

    public void setTTSDeckMap(HashMap<String, List<TTS_Card>> ttsDeckMap) {
        this.ttsDeckMap = ttsDeckMap;
    }

    public void addCardToTTSDeckMap(String deckPile, TTS_Card card){
        if(!this.ttsDeckMap.containsKey(deckPile)){
            this.ttsDeckMap.put(deckPile, new ArrayList<TTS_Card>());
        }
        this.ttsDeckMap.get(deckPile).add(card);
    }
}
