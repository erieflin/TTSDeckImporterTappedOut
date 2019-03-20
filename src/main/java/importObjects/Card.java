package importObjects;

import importers.cardImporter.AbstractCardImporter;
import java.util.List;

public class Card
{
    private String cardName;
    private String set;
    private int quantity;
//    private Board board;
//    private List<CardModifier> modifiers;

//    protected Card(String cardName, AbstractCardImporter cardImporter, String set, int quantity, Board board, List<CardModifier> modifiers)
//    {
//        this.cardName = cardName;
//        this.quantity = quantity;
//        this.board = board;
//        this.modifiers = modifiers;
//
//        //NOTE: Alters currently are only marked as being one, and are not in any way properly pulled.  May become a future effort
//        if(modifiers.contains(CardModifier.ALTER))
//            this.set = null;
//        else
//            this.set = set;
//
//        //TODO actually use cardImporter to get an image
//    }

//    public enum CardModifier
//    {
//        FOIL, ALTER, COMMANDER
//    }
//
//    public enum Board
//    {
//        MAIN, SIDEBOARD, MAYBEBOARD
//    }
//
//    public static class CardBuilder
//    {
//        private String cardName;
//        private String set;
//        private int quantity;
//        private Board board;
//        private List<CardModifier> modifiers;
//        private AbstractCardImporter cardImporter;
//
//        public CardBuilder(String cardName, AbstractCardImporter cardImporter)
//        {
//            this.cardName = cardName;
//            this.cardImporter = cardImporter;
//            this.set = "";
//            this.quantity = 1;
//            this.modifiers = null;
//        }
//
//        public CardBuilder set(String set)
//        {
//            this.set = set;
//            return this;
//        }
//
//        public CardBuilder quantity(int quantity)
//        {
//            this.quantity = quantity;
//            return this;
//        }
//
//        public CardBuilder board(Board board)
//        {
//            this.board = board;
//            return this;
//        }
//
//        public CardBuilder modifiers(List<CardModifier> modifiers)
//        {
//            this.modifiers = modifiers;
//            return this;
//        }
//
//        public Card construct()
//        {
//            return new Card(cardName, cardImporter, set, quantity, board, modifiers);
//        }
//    }
}
