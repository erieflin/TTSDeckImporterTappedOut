package importObjects.deck.constructed;

import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class EDH extends AbstractConstructed
{
    public EDH(String deckName, AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(deckName, importer, sleeveUrl);
    }

    public EDH(String deckName, AbstractDeckImporter importer) throws MalformedURLException
    {
        super(deckName, importer);
    }
}
