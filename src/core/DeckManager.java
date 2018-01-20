package core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;

import utils.TappedOutUtils;

public class DeckManager {
private DeckManager(){
}
private Gson gson = new Gson();
public static DeckManager getInstance(){
	return new DeckManager();
}
private ArrayList<CsvConverterDeck> decks = new ArrayList<CsvConverterDeck>();
private ArrayList<CsvConverterDeck> staged = new ArrayList<CsvConverterDeck>();
public ArrayList<CsvConverterDeck> getDecks() {
	return decks;
}
public void setDecks(ArrayList<CsvConverterDeck> decks) {
	this.decks = decks;
}
public ArrayList<CsvConverterDeck> getStaged() {
	return staged;
}
public void setStaged(ArrayList<CsvConverterDeck> staged) {
	this.staged = staged;
}

public void loadDecks(){
	for(CsvConverterDeck deck: decks){
		try {
			saveDeck(deck);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	decks.clear();
	File decksFolder= new File("decks");
	if(!decksFolder.exists()){
		decksFolder.mkdirs();
	}
	Scanner s = null;
	for(File f: decksFolder.listFiles()){
		try{
			s = new Scanner(f);
			String json = s.nextLine();
			CsvConverterDeck deck = gson.fromJson(json, CsvConverterDeck.class);
			decks.add(deck);
			s.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(s!=null){
				s.close();
			}
		}
	}
	Main.updateListBox();
}
public void addDeck(String url, String commander, String name){
	if(commander.trim().equals("")){
		commander = "auto";
	}
	if(name.trim().equals("")){
		name = "auto";
	}
	CsvConverterDeck deck = new CsvConverterDeck();
	String response = "";
	try {
		String textUrl = url;
		if (!textUrl.contains("?")) {
			textUrl += "?fmt=csv";
		}
		response = TappedOutUtils.getHTML(textUrl);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
	}

	TappedOutUtils.searchForDefaults(url, deck);
	deck.loadDeckFromCsv(response, commander, name);
	deck.setCardListUrl(url);
	decks.add(deck);
	try{
		saveDeck(deck);	
	}catch (Exception e) {
		System.out.println("non critical error below");
		e.printStackTrace();
	}
}
public void addDeck(CsvConverterDeck deck){
	decks.add(deck);
	try{
		saveDeck(deck);	
	}catch (Exception e) {
		System.out.println("non critical error below");
		e.printStackTrace();
	}
}
public void stageDeck(CsvConverterDeck deck){
	staged.add(deck);
}
public void unstageDeck(CsvConverterDeck deck){
	staged.remove(deck);
}
public void saveDeck(CsvConverterDeck deck) throws IOException{
	File decksFolder= new File("decks");
	if(!decksFolder.exists()){
		decksFolder.mkdirs();
	}
	File deckFile = Paths.get(decksFolder.getAbsolutePath(),deck.getDeckMetadata().getFriendlyDeckName()+".deck").toFile();
	if(!deckFile.exists()){
		deckFile.createNewFile();
	}

	String json = gson.toJson(deck);
	PrintWriter out = new PrintWriter(deckFile);
	out.println(json);
	out.close();
	return;
}


}
