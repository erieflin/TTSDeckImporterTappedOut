package importObjects.deck;

import core.Constants;
import importObjects.Card;
import importObjects.Token;
import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public abstract class AbstractDeck
{
    private String name;
    private AbstractDeckImporter deckImporter;
    private ArrayList<Card> cardList;
    private ArrayList<Token> tokenList;
    private URL sleeveImageUrl;

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
}
