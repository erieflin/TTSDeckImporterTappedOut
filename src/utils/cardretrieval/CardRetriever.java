package utils.cardretrieval;


import java.util.HashSet;

import core.FrogCard;

public abstract class CardRetriever {
	public HashSet<String> failedCards;

	public CardRetriever(){
		failedCards = new HashSet<String>();
	}
	
	public void ClearFailedCards(){
		failedCards.clear();
	}
	
	public void LoadFailed(FrogCard card){
		failedCards.add(card.cardKey);
	}
	
	public boolean HasCardFailed(FrogCard card){
		return failedCards.contains(card.cardKey);
	}
	
	public abstract boolean LoadCard(FrogCard card); 
}
