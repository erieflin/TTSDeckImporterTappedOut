package importObjects;

import java.io.File;
import java.net.URI;
import java.util.List;
import importObjects.CardDetails.*;

public class Card
{
    //TODO these might change to something other than final
    private String cardName;
    private String set;
    private int quantity;
    private Board board;
    private Tag[] modifiers;
    private File cardImage;

    public Card(CardParams params, File cardImage)
    {
        this.cardName = params.cardName;
        this.set = params.set;
        this.quantity = params.qty;
        this.board = params.board;
        this.modifiers = params.modifiers;
        this.cardImage = cardImage;
    }

    public String getCardName() {
        return cardName;
    }

    public String getSet() {
        return set;
    }

    public int getQuantity() {
        return quantity;
    }

    public Board getBoard() {
        return board;
    }

    public Tag[] getModifiers() {
        return modifiers;
    }

    public File getCardImage() {
        return cardImage;
    }

    public String toString(){
        return getBoard() + ": " + getQuantity() + "x " + getCardName() + " " + getSet() + " " + getModifiers();
    }
}
