package core;

import images.ImageUtils;
import importObjects.deck.constructed.Draft;
import importObjects.deck.constructed.EDH;
import importers.cardImporter.Scryfall;
import importers.deckImporter.TappedOutDraftImporter;
import importers.deckImporter.TappedOutImporter;

import java.io.IOException;
import java.net.URL;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        if(args.length < 2)
        {
            System.err.println("Usage: java -jar TTSDeckImporter.jar tappedOutUsername tappedOutPassword [deckUrl[deckName]]");
            System.exit(1);
        }

        String user = args[0];
        String pass = args[1];


        //TODO: Elliots decks
        //String deckUrl = "https://tappedout.net/mtg-decks/gliristofacts/";
        //String deckUrl = "https://tappedout.net/mtg-decks/test-card-bin" ;
        String deckUrl = "https://tappedout.net/mtg-decks/gliristofacts-v2-wip/";
        //TODO: Chris's decks
        //String deckUrl = "http://tappedout.net/mtg-decks/testing-area/";
        //"http://tappedout.net/mtg-decks/mayael-the-anima-irl/";

        String deckName = "gliristofacts";
        if(args.length==4){
            deckUrl = args[3];
            deckName = args[4];
        }
        URL url = new URL(deckUrl);
        TappedOutImporter importer = new TappedOutImporter(new Credentials(user, pass), new Scryfall(), url);
        EDH deck = new EDH(importer);
        deck.setName(deckName);

        System.out.println("Importing Deck " + deck.getName());
        deck.importDeck();
        System.out.println("Stitching Deck " + deck.getName());
        ImageUtils.StitchDeck(deck);
        System.out.println("Finished Deck " + deck.getName());
        JsonUtils.BuildJSONFile(deck);
    }

}

