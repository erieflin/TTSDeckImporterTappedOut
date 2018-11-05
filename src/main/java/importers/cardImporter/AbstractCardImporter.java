package importers.cardImporter;

import importObjects.Card;
import importObjects.DoubleFacedCard;

public abstract class AbstractCardImporter
{
    protected AbstractCardImporter()
    {
        //TODO implement/maybe change parameters
    }

    //TODO some method to add image to a Card object or something - also needs to check existing images
    //may look into having a method of this class that is public, and some protected method that is implemented by subclasses

    protected abstract Card loadImage(String cardName); //TODO refer to existing code in old repo for what is needed

    protected abstract DoubleFacedCard loadImage(String frontSideName, String backSideName);
}
