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

    protected AbstractUrlDeckImporter(Credentials credentials, AbstractCardImporter cardImportMethod, URL deckURL) throws IOException
    {
        super(credentials, cardImportMethod);
        this.cardList = importDeckURL(deckURL);
    }

    protected abstract List<Card> importDeckURL(URL deckURL) throws IOException;

    public List<Card> getCardList()
    {
        return cardList;
    }
}
