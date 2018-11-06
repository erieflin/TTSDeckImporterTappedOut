package importers.deckImporter;

import com.google.gson.JsonObject;
import core.Credentials;
import importers.cardImporter.AbstractCardImporter;

public abstract class AbstractJsonDeckImporter extends AbstractDeckImporter
{
    protected AbstractJsonDeckImporter(Credentials credentials, AbstractCardImporter cardImportMethod, JsonObject deckFile)
    {
        super(credentials, cardImportMethod);
        importDeckURL(deckFile);
    }

    protected abstract void importDeckURL(JsonObject deckFile);
}
