package importers.cardImporter;

import importObjects.Card;
import importObjects.CardSetup;
import importObjects.DoubleFacedCard;

public abstract class AbstractCardImporter
{
    protected AbstractCardImporter()
    {
        //TODO implement/maybe change parameters
    }

    public abstract Card loadCard(CardSetup setup); //TODO refer to existing code in old repo for what is needed
}
