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

public abstract class AbstractDeck
{
    private String name;
    private AbstractDeckImporter deckImporter;
    private ArrayList<Card> cardList;
    private ArrayList<Token> tokenList;
    private URL sleeveImageUrl;
    private static final String DECKFOLDER = "DeckOutput";
    protected AbstractDeck(AbstractDeckImporter importer, URL sleeveUrl)
    {
//        name = importer.deckName; //TODO fill in
        deckImporter = importer;
        cardList = new ArrayList<>();
        tokenList = new ArrayList<>();
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
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }

    public void setCardList(ArrayList<Card> cardList) {
        this.cardList = cardList;
    }

    public ArrayList<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(ArrayList<Token> tokenList) {
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
