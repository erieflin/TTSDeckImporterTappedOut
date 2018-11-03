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

    protected AbstractDeck(String deckName, AbstractDeckImporter importer, URL sleeveUrl)
    {
        name = deckName;
        deckImporter = importer;
        cardList = new ArrayList<>();
        tokenList = new ArrayList<>();
        sleeveImageUrl = sleeveUrl;
    }

    protected AbstractDeck(String deckName, AbstractDeckImporter importer) throws MalformedURLException
    {
        this(deckName, importer, new URL(Constants.defaultSleeveImageUrl));
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
