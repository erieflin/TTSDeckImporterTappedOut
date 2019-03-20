package importObjects;

import java.util.ArrayList;
import java.util.List;

public class CardSetup
{
    private String cardName;
    private String set;
    private int qty;
    private Board board;
    private List<Tag> modifiers;

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
            this.cardName = cardName;
            this.modifiers = new ArrayList<>();
            this.qty = 1;   //Default quantity
        }

        public CardSetupBuilder set(String set)
        {
            this.set = set;
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
            return new CardSetup(this);
        }
    }

    public enum Tag
    {
        FOIL, ALTER, COMMANDER
    }

    public enum Board
    {
        MAIN, SIDEBOARD, MAYBEBOARD
    }
}
