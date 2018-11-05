package importers.deckImporter;

import java.net.URL;
import com.google.gson.JsonObject;
import core.Credentials;
import importObjects.deck.AbstractDeck;
import importers.cardImporter.AbstractCardImporter;

public abstract class AbstractDeckImporter
{
    protected AbstractCardImporter cardImporter;
    protected AbstractDeck deck;
    protected Credentials credentials;

    protected AbstractDeckImporter(Credentials credentials, AbstractCardImporter cardImportMethod, URL deckURL)
    {
        cardImporter = cardImportMethod;
        this.credentials = credentials;
        importDeckURL(deckURL);

    }

    protected AbstractDeckImporter(Credentials credentials, AbstractCardImporter cardImportMethod, JsonObject deckFile)
    {
        cardImporter = cardImportMethod;
        this.credentials = credentials;
        importDeckURL(deckFile);
    }

    protected abstract void importDeckURL(URL deckURL);

    protected abstract void importDeckURL(JsonObject deckFile);
}
