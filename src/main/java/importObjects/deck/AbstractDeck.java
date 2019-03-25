package importObjects.deck;

import core.Constants;
import importObjects.Card;
import importObjects.DoubleFacedCard;
import importObjects.Token;
import importers.deckImporter.AbstractDeckImporter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    public List<DoubleFacedCard> getTranformList(){
        List<DoubleFacedCard> cardList = new ArrayList<DoubleFacedCard>();
        int count = 0;
        for(Card card: cardList){
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

       File currentDeckFolder = new File(deckFolder + name);
       currentDeckFolder.mkdirs();

       return  currentDeckFolder;
    }
}
