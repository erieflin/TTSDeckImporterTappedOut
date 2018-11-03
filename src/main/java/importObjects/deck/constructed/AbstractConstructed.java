package importObjects.deck.constructed;

import importObjects.deck.AbstractDeck;
import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class AbstractConstructed extends AbstractDeck
{
    protected AbstractConstructed(String deckName, AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(deckName, importer, sleeveUrl);
    }

    protected AbstractConstructed(String deckName, AbstractDeckImporter importer) throws MalformedURLException
    {
        super(deckName, importer);
    }
}
