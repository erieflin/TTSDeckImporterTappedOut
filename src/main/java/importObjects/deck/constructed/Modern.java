package importObjects.deck.constructed;

import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class Modern extends AbstractConstructed
{
    public Modern(AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(importer, sleeveUrl);
    }

    public Modern(AbstractDeckImporter importer) throws MalformedURLException
    {
        super(importer);
    }
}
