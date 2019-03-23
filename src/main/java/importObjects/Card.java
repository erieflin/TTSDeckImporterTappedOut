package importObjects;

import java.util.List;
import importObjects.CardDetails.*;

public class Card
{
    //TODO these might change to something other than final
    public final String cardName;
    public final String set;
    public final int quantity;
    public final Board board;
    public final List<Tag> modifiers;   //TODO if this stays final it should become an array - same for the other one in CardSetup

    protected Card(CardSetup params)
    {
        this.cardName = params.cardName;
        this.set = params.set;
        this.quantity = params.qty;
        this.board = params.board;
        this.modifiers = params.modifiers;
    }
}
