package importObjects.deck.constructed;

import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class CanadianHighlander extends AbstractConstructed
{
    protected CanadianHighlander(AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(importer, sleeveUrl);
    }

    protected CanadianHighlander(AbstractDeckImporter importer) throws MalformedURLException
    {
        super(importer);
    }
}
