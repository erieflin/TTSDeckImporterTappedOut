package importObjects.deck.constructed;

import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class Legacy extends AbstractConstructed
{
    public Legacy(String deckName, AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(deckName, importer, sleeveUrl);
    }

    public Legacy(String deckName, AbstractDeckImporter importer) throws MalformedURLException
    {
        super(deckName, importer);
    }
}
