package importObjects.deck.limited;

import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class Cube extends AbstractLimited
{
    public Cube(AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(importer, sleeveUrl);
    }

    public Cube(AbstractDeckImporter importer) throws MalformedURLException
    {
        super(importer);
    }
}
