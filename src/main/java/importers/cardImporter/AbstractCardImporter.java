package importers.cardImporter;

import images.ImageCache;
import importObjects.Card;
import importObjects.CardParams;

import java.io.IOException;
import java.sql.SQLException;

public abstract class AbstractCardImporter
{
    protected AbstractCardImporter()
    {
        //TODO implement/maybe change parameters
    }

    public Card loadCard(CardParams setup)
    {
        Card localCard = null;

        try
        {
            String sourceName = getSourceName();

            ImageCache imageCache = ImageCache.getInstance();
            localCard = imageCache.getMatchingCards(setup, sourceName);

            if(localCard != null)
            {
                return localCard;
            }
            else
            {
                Card webCard = loadCardFromImporter(setup);

                imageCache.saveCardtoCache(webCard, sourceName);

                return webCard;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage()); //TODO failing on save due to Card object lacking set - need to probably require for Card & set in importer
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

        throw new IllegalStateException("y u do dis");
    }

    protected abstract String getSourceName();

    protected abstract Card loadCardFromImporter(CardParams setup); //TODO refer to existing code in old repo for what is needed
}
