package core;

import importObjects.Card;
import importObjects.Token;
import importObjects.deck.AbstractDeck;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Integer> getAllCardIds(AbstractDeck deck){
        List<Integer> cardIds = new ArrayList<Integer>();

        // for each card in the card list, add its tts index to the array
        for(int ii = 0; ii < deck.getCardList().size(); ++ii){
            cardIds.add(getCardIdByCardIndex(ii));
        }

        return cardIds;
    }

    public static List<Integer> getAllTokenCardIds(AbstractDeck deck){
        List<Integer> cardIds = new ArrayList<Integer>();
        // tokens card indexs start immediatly after the decks, so index starts at card list size-1
        for(int ii=deck.getCardList().size()-1; ii < deck.getTokenList().size(); ++ii){
            cardIds.add(getCardIdByCardIndex(ii));
        }
        return cardIds;
    }

}
