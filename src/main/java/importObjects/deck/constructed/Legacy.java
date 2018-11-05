package importObjects.deck.constructed;

import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class Legacy extends AbstractConstructed
{
    public Legacy(AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(importer, sleeveUrl);
    }

    public Legacy(AbstractDeckImporter importer) throws MalformedURLException
    {
        super(importer);
    }
}
