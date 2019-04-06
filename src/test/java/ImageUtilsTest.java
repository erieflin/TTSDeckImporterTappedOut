import com.google.gson.JsonObject;
import images.ImageUtils;
import importObjects.Card;
import importObjects.CardDetails;
import importObjects.CardParams;
import importObjects.deck.AbstractDeck;
import importObjects.deck.constructed.EDH;
import importers.deckImporter.AbstractJsonDeckImporter;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ImageUtilsTest {

    @Test
    public void getBuffer() {

    }

    @Test
    public void saveImage() {

    }

    @Test
    public void stitchDeck() {
        AbstractDeck deck = buildDeck();
        ImageUtils.StitchDeck(deck);
    }

    private static final String TESTRESOURCEDIR = "src"+ File.separator + "test" + File.separator + "resources" + File.separator;

    private AbstractDeck buildDeck(){
        EDH edh = null;
        try {
            edh = new EDH(null);
            edh.setName("testDeck");
            List<Card> cardList = new ArrayList<Card>();

            CardParams params = new CardParams.CardParamsBuilder("test").board(CardDetails.Board.MAIN).build();
            Card card = new Card(params,
                    new File(TESTRESOURCEDIR + "test1.png"));

            params = new CardParams.CardParamsBuilder("test2").board(CardDetails.Board.MAIN).build();
            Card card2 = new Card(params,
                    new File(TESTRESOURCEDIR + "test2.png"));

            params = new CardParams.CardParamsBuilder("test3").board(CardDetails.Board.MAIN).build();
            Card card3 = new Card(params,
                    new File(TESTRESOURCEDIR + "test3.png"));

            cardList.add(card);
            cardList.add(card2);
            cardList.add(card3);
            edh.setCardList(cardList);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return edh;
    }
}