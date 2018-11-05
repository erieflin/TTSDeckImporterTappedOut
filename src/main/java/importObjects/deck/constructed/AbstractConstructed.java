package importObjects.deck.constructed;

import importObjects.deck.AbstractDeck;
import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class AbstractConstructed extends AbstractDeck
{
    protected AbstractConstructed(AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(importer, sleeveUrl);
    }

    protected AbstractConstructed(AbstractDeckImporter importer) throws MalformedURLException
    {
        super(importer);
    }
}
