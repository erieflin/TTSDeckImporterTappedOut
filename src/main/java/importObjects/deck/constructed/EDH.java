package importObjects.deck.constructed;

import importObjects.Card;
import importers.deckImporter.AbstractDeckImporter;

import java.net.MalformedURLException;
import java.net.URL;

public class EDH extends AbstractConstructed
{
    private boolean partnerCommanders;
    private Card commander;
    private Card partnerCommander;

    public EDH(AbstractDeckImporter importer, URL sleeveUrl)
    {
        super(importer, sleeveUrl);
    }

    public EDH(AbstractDeckImporter importer) throws MalformedURLException
    {
        super(importer);
    }
}
