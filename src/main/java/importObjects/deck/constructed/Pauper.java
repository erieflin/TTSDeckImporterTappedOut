package importObjects.deck.constructed;

import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class Pauper extends AbstractConstructed
{
    public Pauper(String deckName, AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(deckName, importer, sleeveUrl);
    }

    public Pauper(String deckName, AbstractDeckImporter importer) throws MalformedURLException
    {
        super(deckName, importer);
    }
}
