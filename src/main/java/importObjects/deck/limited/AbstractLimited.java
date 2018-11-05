package importObjects.deck.limited;

import importObjects.deck.AbstractDeck;
import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class AbstractLimited extends AbstractDeck
{
    protected AbstractLimited(AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(importer, sleeveUrl);
    }

    protected AbstractLimited(AbstractDeckImporter importer) throws MalformedURLException
    {
        super(importer);
    }
}
