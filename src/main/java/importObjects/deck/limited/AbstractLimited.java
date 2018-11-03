package importObjects.deck.limited;

import importObjects.deck.AbstractDeck;
import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class AbstractLimited extends AbstractDeck
{
    protected AbstractLimited(String deckName, AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(deckName, importer, sleeveUrl);
    }

    protected AbstractLimited(String deckName, AbstractDeckImporter importer) throws MalformedURLException
    {
        super(deckName, importer);
    }
}
