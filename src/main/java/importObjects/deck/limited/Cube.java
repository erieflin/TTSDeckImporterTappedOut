package importObjects.deck.limited;

import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class Cube extends AbstractLimited
{
    public Cube(String deckName, AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(deckName, importer, sleeveUrl);
    }

    public Cube(String deckName, AbstractDeckImporter importer) throws MalformedURLException
    {
        super(deckName, importer);
    }
}
