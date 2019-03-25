package importers.deckImporter;

import com.google.gson.JsonObject;
import core.Credentials;
import importers.cardImporter.AbstractCardImporter;

public abstract class AbstractJsonDeckImporter extends AbstractDeckImporter
{
    protected AbstractJsonDeckImporter(AbstractCardImporter cardImportMethod, JsonObject deckFile)
    {
        super(cardImportMethod);
        importDeck(deckFile);
    }

    protected abstract void importDeck(JsonObject deckFile);
}
