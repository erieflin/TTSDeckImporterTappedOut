package importObjects;

import java.io.File;
import java.net.URI;
import java.util.List;
import importObjects.CardDetails.*;

public class Card
{
    //TODO these might change to something other than final
    public final String cardName;
    public final String set;
    public final int quantity;
    public final Board board;
    public final Tag[] modifiers;
    public final File cardImage;

    protected Card(CardParams params, File cardImage)
    {
        this.cardName = params.cardName;
        this.set = params.set;
        this.quantity = params.qty;
        this.board = params.board;
        this.modifiers = params.modifiers;
        this.cardImage = cardImage;
    }
}
