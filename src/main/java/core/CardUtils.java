package core;

import importObjects.BaseCard;
import importObjects.Card;
import importObjects.CardDetails;

import java.util.Arrays;

public class CardUtils {

    public static boolean checkCardHasTag(Card card, CardDetails.Tag tag) {
        return Arrays.stream(card.getModifiers())
                .anyMatch(modifier -> modifier.equals(tag));
    }

    public static boolean checkCardHasTag(BaseCard card, CardDetails.Tag tag){
        if(!(card instanceof  Card)){
            return false;
        }
        return checkCardHasTag((Card) card, tag);
    }
}
