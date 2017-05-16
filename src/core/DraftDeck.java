package core;
import java.util.ArrayList;

public class DraftDeck extends Deck{
	public static final String[] RARITIES={"Mythic Rare","Rare","Uncommon","Common","Basic Land","Special"};
	
	public ArrayList<ArrayList<FrogCard>> cardsByRarity = new ArrayList<ArrayList<FrogCard>>();
	public ArrayList<FrogCard> basics = new ArrayList<FrogCard>();
	
	public DraftDeck(){
		compressionLevel = 1.0;
		for(int i = 0; i < RARITIES.length; i++){
			cardsByRarity.add(new ArrayList<FrogCard>());
		}
	}
	
	public String setName;
	public String code;
	public int[] boosterAmts = new int[RARITIES.length];

	public int amountPacks;
	public int curRarity;
	
	public void add(FrogCard card){
		if(!cardsByRarity.get(curRarity).contains(card)){
			cardsByRarity.get(curRarity).add(card);
		}
		super.add(card);
	}
}
