package importers.deckImporter;

import core.Credentials;
import importObjects.Card;
import importers.cardImporter.AbstractCardImporter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class AbstractUrlDeckImporter extends AbstractDeckImporter
{
    protected List<Card> cardList;
    protected URL deckURL;
    protected AbstractUrlDeckImporter(AbstractCardImporter cardImportMethod, URL deckURL) throws IOException
    {
        super(cardImportMethod);
        this.deckURL = deckURL;
    }

    public List<Card> getCardList()
    {
        return cardList;
    }
}
