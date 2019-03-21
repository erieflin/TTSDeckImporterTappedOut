package importObjects;

import core.Util;

import java.util.ArrayList;
import java.util.List;
import importObjects.CardDetails.*;

public class CardSetup
{
    public final String cardName;
    public final String set;
    public final int qty;
    public final Board board;
    public final List<Tag> modifiers;

    private CardSetup(CardSetupBuilder builder)
    {
        if(builder == null)
            throw new NullPointerException("CardSetupBuilder cannot be null for CardSetup");

        this.cardName = builder.cardName;
        this.set = builder.set;
        this.qty = builder.qty;
        this.board = builder.board;
        this.modifiers = builder.modifiers;
    }

    public static class CardSetupBuilder
    {
        private String cardName;
        private String set;
        private int qty;
        private Board board;
        private List<Tag> modifiers;

        public CardSetupBuilder(String cardName)
        {
            if(Util.NullOrWhitespace(cardName))
                throw new IllegalArgumentException("Card name cannot be null or blank");

            this.cardName = cardName.trim();
            this.modifiers = new ArrayList<>();
            this.qty = 1;   //Default quantity
        }

        public CardSetupBuilder set(String set)
        {
            if(Util.NullOrWhitespace(set))
                this.set = null;
            else
                this.set = set.trim();

            return this;
        }

        public CardSetupBuilder qty(int quantity)
        {
            //Maximum number of cards allowed left to specific Deck constructors (for instance, Standard has a max quantity of 4, EDH 1, and Draft has no max quantity.)
            if(qty >= 1)
                throw new IllegalArgumentException("Quantity must be 1 or more");

            this.qty = quantity;
            return this;
        }

        public CardSetupBuilder board(Board board)
        {
            this.board = board;
            return this;
        }

        public CardSetupBuilder modifier(Tag tag)
        {
            modifiers.add(tag);
            return this;
        }

        public CardSetupBuilder modifiers(List<Tag> modifiers)
        {
            if(modifiers == null)
                throw new NullPointerException("List of modifier Tags for CardSetupBuilder cannot be null");

            this.modifiers = modifiers;
            return this;
        }

        public CardSetup build()
        {
            //NOTE: Alters currently are only marked as being one, and are not in any way properly pulled.  May become a future effort
            if(modifiers.contains(Tag.ALTER))
                this.set = null;

            return new CardSetup(this);
        }
    }
}
