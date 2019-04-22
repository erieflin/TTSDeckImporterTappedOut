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
        ImageCache imageCache = null;
        String sourceName = getSourceName();

        Card webCard = null;
        try

        {


            imageCache = ImageCache.getInstance();
            localCard = imageCache.getMatchingCards(setup, sourceName);

            if(localCard != null)
            {
                return localCard;
            }
            webCard = loadCardFromImporter(setup);
            imageCache.saveCardtoCache(webCard, sourceName);
            return webCard;
        }
        catch(SQLException e)
        {
           e.printStackTrace(); //TODO failing on save due to Card object lacking set - need to probably require for Card & set in importer
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        // if we failed, we are having db issues or something, go ahead and just load the card
        // because we could not pull our cached version even if it exists
        if(webCard == null){
            webCard = loadCardFromImporter(setup);
        }
        return webCard;
    }

    protected abstract String getSourceName();

    protected abstract Card loadCardFromImporter(CardParams setup); //TODO refer to existing code in old repo for what is needed
}
