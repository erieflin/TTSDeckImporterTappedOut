package importers.cardImporter;

import importObjects.Card;
import importObjects.CardParams;

public abstract class AbstractCardImporter
{
    protected AbstractCardImporter()
    {
        //TODO implement/maybe change parameters
    }

    public Card loadCard(CardParams setup)
    {
        //TODO implement, needs to check FS for if card already exists?

        return loadCardFromImporter(setup);
    }

    protected abstract Card loadCardFromImporter(CardParams setup); //TODO refer to existing code in old repo for what is needed
}
