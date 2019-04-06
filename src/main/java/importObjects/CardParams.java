package importObjects;

import core.Util;

import java.util.ArrayList;
import java.util.List;
import importObjects.CardDetails.*;

public class CardParams
{
    public final String cardName;
    public final String set;
    public final int qty;
    public final Board board;
    public final Tag[] modifiers;

    private CardParams(CardParamsBuilder builder)
    {
        if(builder == null)
            throw new NullPointerException("CardParamsBuilder cannot be null for CardParams");

        this.cardName = builder.cardName;
        this.set = builder.set;
        this.qty = builder.qty;
        this.board = builder.board;
        this.modifiers = builder.modifiers.toArray(new Tag[0]);
    }

    public static class CardParamsBuilder
    {
        private String cardName;
        private String set;
        private int qty;
        private Board board;
        private List<Tag> modifiers;

        public CardParamsBuilder(String cardName)   //TODO parse the name for Double-sided cards or create a cardname class to do so
        {
            if(Util.NullOrWhitespace(cardName))
                throw new IllegalArgumentException("Card name cannot be null or blank");

            this.cardName = cardName.trim();
            this.modifiers = new ArrayList<>();
            this.qty = 1;   //Default quantity
        }

        public CardParamsBuilder set(String set)    //TODO change to set acronym, make a parser to get the correct associated set?
        {
            if(Util.NullOrWhitespace(set))
                this.set = null;
            else
                this.set = set.trim();

            return this;
        }

        public CardParamsBuilder qty(int quantity)
        {
            //Maximum number of cards allowed left to specific Deck constructors (for instance, Standard has a max quantity of 4, EDH 1, and Draft has no max quantity.)
            if(qty < 1)
                throw new IllegalArgumentException("Quantity must be 1 or more");

            this.qty = quantity;
            return this;
        }

        public CardParamsBuilder board(Board board)
        {
            this.board = board;
            return this;
        }

        public CardParamsBuilder modifier(Tag tag)
        {
            modifiers.add(tag);
            return this;
        }

        public CardParamsBuilder modifiers(List<Tag> modifiers)
        {
            if(modifiers == null)
                throw new NullPointerException("List of modifier Tags for CardParamsBuilder cannot be null");

            this.modifiers = modifiers;
            return this;
        }

        public CardParams build()
        {
            //NOTE: Alters currently are only marked as being one, and are not in any way properly pulled.  May become a future effort
            if(modifiers.contains(Tag.ALTER))
                this.set = null;

            return new CardParams(this);
        }
    }
}
