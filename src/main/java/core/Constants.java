package core;

public class Constants
{
    public static final String DEFAULT_SLEEVE_IMAGE_URL = "http://i.imgur.com/P7qYTcI.png";

    public static final String TAPPED_OUT_API_URL = "http://tappedout.net/api/collection/collection:deck/";
    public static final String TAPPED_OUT_BOARD_KEY = "b";
    public static final String TAPPED_OUT_QTY_KEY = "qty";

    public static final String SCRYFALL_URL = "https://api.scryfall.com/";
    public static final String SCRYFALL_CARD = SCRYFALL_URL + "cards/";
    public static final String SCRYFALL_CARD_NAMED = SCRYFALL_CARD + "named";
    public static final String SCRYFALL_PRECEDE_QUERY = SCRYFALL_CARD_NAMED + "?exact=";
    public static final String SCRYFALL_CARDS_ID = "cards";
    public static final String SCRYFALL_PRINTING_ID = "printings";
}
