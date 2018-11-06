package importers.deckImporter;

import java.net.URL;
import java.util.List;

import com.google.gson.JsonObject;
import core.Credentials;
import importObjects.Card;
import importObjects.deck.AbstractDeck;
import importers.cardImporter.AbstractCardImporter;

public abstract class AbstractDeckImporter
{
    protected AbstractCardImporter cardImporter;
    protected AbstractDeck deck;
    protected Credentials credentials;

    protected AbstractDeckImporter(Credentials credentials, AbstractCardImporter cardImportMethod)
    {
        cardImporter = cardImportMethod;
        this.credentials = credentials;
    }
}
