package core;

import java.util.ArrayList;

public class DeckManager {
private ArrayList<DeckLocation> decks = new ArrayList<DeckLocation>();
private ArrayList<DeckLocation> staged = new ArrayList<DeckLocation>();
public ArrayList<DeckLocation> getDecks() {
	return decks;
}
public void setDecks(ArrayList<DeckLocation> decks) {
	this.decks = decks;
}
public ArrayList<DeckLocation> getStaged() {
	return staged;
}
public void setStaged(ArrayList<DeckLocation> staged) {
	this.staged = staged;
}
public DeckManager(){
	loadDecks();
}
private void loadDecks(){
	
}
public void addDeck(DeckLocation deck){
	decks.add(deck);
	saveDeck(deck);
}
public void stageDeck(DeckLocation deck){
	staged.add(deck);
}
public void unstageDeck(DeckLocation deck){
	staged.remove(deck);
}
public void saveDeck(DeckLocation deck){
	//todo: implement save
	return;
}

}
