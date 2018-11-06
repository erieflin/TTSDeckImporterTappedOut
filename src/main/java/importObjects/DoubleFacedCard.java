package importObjects;

import importers.cardImporter.AbstractCardImporter;

import java.util.List;

public class DoubleFacedCard extends Card
{
    protected DoubleFacedCard(String cardName, AbstractCardImporter cardImporter, String set, int quantity, Board board, List<CardModifier> modifiers)
    {
        super(cardName, cardImporter, set, quantity, board, modifiers);
        //TODO implement
    }
}
