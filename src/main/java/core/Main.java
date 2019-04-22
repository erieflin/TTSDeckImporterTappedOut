package core;

import images.ImageUtils;
import importObjects.deck.constructed.EDH;
import importers.cardImporter.Scryfall;
import importers.deckImporter.TappedOutImporter;

import java.io.IOException;
import java.net.URL;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        if(args.length != 2)
        {
            throw new IOException("Pass your TappedOut Username & Password!");
        }

        String user = args[0];
        String pass = args[1];
        String someTappedOutUrl = "https://tappedout.net/mtg-decks/gliristofacts/";
        //String someTappedOutUrl = "http://tappedout.net/mtg-decks/testing-area/";
        //"http://tappedout.net/mtg-decks/mayael-the-anima-irl/";

        URL url = new URL(someTappedOutUrl);

        TappedOutImporter importer = new TappedOutImporter(new Credentials(user, pass), new Scryfall(), url);

        EDH deck = new EDH(importer);
        deck.setName("glissaTestDeck");
        System.out.println("Importing Deck " + deck.getName());
        deck.importDeck();
        System.out.println("Stitching Deck " + deck.getName());
        ImageUtils.StitchDeck(deck);
        System.out.println("Finished Deck " + deck.getName());
        JsonUtils.BuildJSONFile(deck);
    }
}

