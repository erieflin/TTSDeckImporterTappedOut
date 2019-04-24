import core.Credentials;
import core.JsonUtils;
import images.ImageUtils;
import importObjects.deck.constructed.Draft;
import importers.cardImporter.Scryfall;
import importers.deckImporter.TappedOutDraftImporter;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TappedOutDraftTest {

    @Test
    public void testTODraftApiE2E() throws Exception{
        TappedOutDraftImporter importer = new TappedOutDraftImporter(new Scryfall(),"rna",3);
        Draft deck = new Draft(importer);
        deck.setName("draftTestDeck");
        System.out.println("Importing Deck " + deck.getName());
        deck.importDeck();
        assertTrue(deck.getCardList().size()==45);
        System.out.println("Stitching Deck " + deck.getName());
        ImageUtils.StitchDeck(deck);
        assertTrue(deck.getTTSDeckMap().get("Booster # 1").size()==15);
        assertTrue(deck.getTTSDeckMap().get("Booster # 2").size()==15);
        assertTrue(deck.getTTSDeckMap().get("Booster # 3").size()==15);
        System.out.println("Finished Deck " + deck.getName());
        JsonUtils.BuildJSONFile(deck);
    }
}
