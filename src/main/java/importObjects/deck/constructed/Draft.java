package importObjects.deck.constructed;

import importObjects.deck.AbstractDeck;
import importers.deckImporter.AbstractDeckImporter;
import importers.deckImporter.TappedOutDraftImporter;
import importers.deckImporter.TappedOutImporter;

import java.io.IOException;
import java.net.MalformedURLException;

public class Draft extends AbstractDeck {

    public Draft(AbstractDeckImporter importer) throws MalformedURLException, IOException {
        super(importer);
    }

}
