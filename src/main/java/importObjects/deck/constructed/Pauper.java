package importObjects.deck.constructed;

import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class Pauper extends AbstractConstructed
{
    public Pauper(AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(importer, sleeveUrl);
    }

    public Pauper(AbstractDeckImporter importer) throws MalformedURLException
    {
        super(importer);
    }
}
