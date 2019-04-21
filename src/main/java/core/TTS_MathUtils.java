package core;

public class TTS_MathUtils {

    public static final int TTSPageMaxSize = 69;


    /***
     * Gets Page Id (TTS calls Deck id) from TTS Card Id
     * Page Id is the 100's place and onwards. E.G
     * 121 page id is 1
     * 252 page id is 2
     * 1352 page id = 13
     * @param cardId
     * @return
     */
    public static int getPageIdFromCardId(int cardId){
        return (int) Math.floor(cardId/100);
    }

    /***
     * Gets the tts PageId by the cards actual index in the deck
     * Returns how many full multiples of the page size have happened up to the cards index
     * @param cardIndex
     * @return
     */
    public static int getPageIdByCardIndex(int cardIndex){
        return (int) Math.floor(cardIndex / TTSPageMaxSize);

    }

    /***
     * Returns the TTS Card Id by the cards actual index in the deck
     * math is convert page num into 1 indexed,
     * use page num as 100's place, and sub id as the 10/1's place
     * E.G. Card Num 69 would return 169, card 75 would return 206 (2(75-69))
     * @param cardIndex
     * @return
     */
    public static int getCardIdByCardIndex(int cardIndex){
        int pageId = getPageIdByCardIndex(cardIndex);
        int cardSubID = cardIndex % TTSPageMaxSize;
        return (int) Math.floor(100*(1+pageId) + cardSubID);
    }

}
