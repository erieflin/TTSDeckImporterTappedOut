package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exportObjects.*;
import importObjects.Card;
import importObjects.CardDetails;
import importObjects.DoubleFacedCard;
import importObjects.Token;
import importObjects.deck.AbstractDeck;
import org.apache.http.MethodNotSupportedException;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.File;

public class JsonUtils {
    public static Gson gson = initGson();

    public static Gson initGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Pages.class, new PagesAdapter());
        gsonBuilder.registerTypeAdapter(ImagePage.class, new PageAdapter());
        gson = gsonBuilder.create();
        return gson;
    }

	/***
	 * Create a base TTS_Deck, note name may not be the same as the decks actual name
	 * depending on if this tts_deck represents tokens or commanders
	 * @param cardIDs
	 * @param name
	 * @return
	 */
	public static TTS_Deck NewDeckBaseObject(List<Integer> cardIDs, String name){
		TTS_Deck deck = new TTS_Deck();
        ColorDiffuseObj colorObj = new ColorDiffuseObj();
		colorObj.setR(0.713235259);
		colorObj.setG(0.713235259);
		colorObj.setB(0.713235259);

		deck.setName(cardIDs.size() == 1 ? "Card" : "DeckCustom");
		deck.setNickname(name);
		deck.setDescription("");
		deck.setGrid(true);
		deck.setLocked(false);
		deck.setSidewaysCard(false);

		deck.setGUID(UUID.randomUUID().toString());
		deck.setColorDiffuse(colorObj);

		if(cardIDs.size() == 1){
			deck.setCardId(cardIDs.get(0));
		}else{
			deck.setDeckIds(cardIDs);
		}
		return deck;
	}
//
	public static TransformObj NewDeckPosObject(int x, int y, int z, boolean faceup, double scale){
		TransformObj transform = new TransformObj();
		transform.setPosX(2.5 * x);
		transform.setPosY(2.5 * y);
		transform.setPosZ(3.5 * z);
		transform.setRotX(0);
		transform.setRotY(180);
		transform.setRotZ(faceup ? 0 : 180);
		transform.setScaleX(scale);
		transform.setScaleY(scale);
		transform.setScaleZ(scale);

		return transform;
	}


	public static Pages NewPagesObject(List<Integer> deckIds, AbstractDeck deck){
		int cardsPerDeck = 69;

		int numberOfPages = (int) Math.ceil((deck.getCardList().size() + deck.getTokenList().size())/(double)cardsPerDeck);
		Pages pages = new Pages();
		for(int i = 0; i < deckIds.size(); i++){
			int deckID = deckIds.get(i);
			int transId = deckID - numberOfPages - 1;
//			if(transId >= 0){
//				if(transId % 2 == 0){
//					JsonObject deckObj = new JsonObject();
//					deckObj.add("FaceURL", new JsonPrimitive(deck.deckLinks[deckID-1]));
//					deckObj.add("BackURL", new JsonPrimitive(deck.deckLinks[deckID] + "{Unique}"));
//					deckStateObject.add(""+(deckID), deckObj);
//				}
//			}else{
//				JsonObject deckObj = new JsonObject();
//				deckObj.add("FaceURL", new JsonPrimitive(deck.deckLinks[deckID-1]));
//				deckObj.add("BackURL", new JsonPrimitive(deck.backUrl));
//				deckStateObject.add(""+deckID, deckObj);
//			}
			ImagePage page = new ImagePage();
			page.setFaceUrl(deck.getHostedImageUrls().get(deckID-1));//deck.deckLinks[deckID-1]));
			page.setBackUrl(deck.getHostedImageUrls().get(deckID-1) + "{Unique}");//deck.deckLinks[deckID] + "{Unique}"));
			page.setPageNumber(deckID);
			pages.getPageList().add(page);
		}

		return pages;
	}





	public static TTS_Deck generateTokensTTSDeck(AbstractDeck deck){
		//token and transform object state----------------------------------------
		String tokenKey = "Token";
		String transformKey = "Transform";
		List<Integer> tokenIds = new ArrayList<Integer>();
		ArrayList<Integer> tokenPageIDs = new ArrayList<Integer>();
		List<TTS_Card> tokenCards = new ArrayList<TTS_Card>();

		if(deck.getTTSDeckMap().containsKey(tokenKey)) {
			List<TTS_Card> cards = deck.getTTSDeckMap().get(tokenKey);
			for (TTS_Card token: cards) {

				int pageID = token.getPageId();
				int cardID = token.getCardId();

				if (!tokenPageIDs.contains(pageID)) tokenPageIDs.add(pageID);
				tokenCards.add(token);
				tokenIds.add(cardID);
			}
		}
		if(deck.getTTSDeckMap().containsKey(transformKey)) {
			List<DoubleFacedCard> transforms = deck.getTransformList();
			List<TTS_Card> cards = deck.getTTSDeckMap().get(transformKey);
			//check where exactly this image saves, may screw things up
			for (TTS_Card card: cards) {
				int pageID = card.getPageId();
				int cardID = card.getCardId();
				if (!tokenPageIDs.contains(pageID)) tokenPageIDs.add(pageID);
				tokenIds.add(cardID);
				tokenCards.add(card);
			}
		}
		TTS_Deck tokenDeck= NewDeckBaseObject(tokenIds, "Tokens");
		tokenDeck.setTransform(NewDeckPosObject(0, 1, 0, true, 1.0));
		tokenDeck.setCustomDeck(NewPagesObject(tokenPageIDs, deck));
		tokenDeck.setCards(tokenCards);
		return tokenDeck;
	}

	public static TTS_Deck generateCommanderTTSDeck(AbstractDeck deck){
		//commander object state----------------------------------------
		String commanderKey = "Commander";
		if(!deck.getTTSDeckMap().containsKey(commanderKey)){
			return new TTS_Deck();
		}
		List<Integer> commanderStateIDs = new ArrayList<Integer>();
		List<TTS_Card> commanderCards = new ArrayList<TTS_Card>();
		ArrayList<Integer> commanderStatePageIDs = new ArrayList<Integer>();

//		/* look for which card is the commander needs to be revisited and improved
		List<TTS_Card> cards = deck.getTTSDeckMap().get(commanderKey);
		for(TTS_Card card: cards){
			int pageID = card.getPageId();
			int cardID = card.getCardId();
			if(!commanderStatePageIDs.contains(pageID)) commanderStatePageIDs.add(pageID);
			commanderStateIDs.add(cardID);
			commanderCards.add(card);
		}

		TTS_Deck commanderDeck = NewDeckBaseObject(commanderStateIDs, commanderKey);
		commanderDeck.setCards(commanderCards);
		commanderDeck.setTransform(NewDeckPosObject(2, 1, 2, true, 1.25));
		commanderDeck.setCustomDeck(NewPagesObject(commanderStatePageIDs, deck));
		return commanderDeck;
	}

	public static void generateSideboardTTSDeck(TTS_Deck wipOutputDeck, AbstractDeck sourceDeck, List<Integer> sideStateIDs){
//		int ii =0;
//		for(Card card : sourceDeck.getCardList()){
//			if(!card.getBoard().equals(CardDetails.Board.SIDEBOARD))continue;
//			int cardId = TTS_MathUtils.getCardIdByCardIndex(ii);
//			int pageId = TTS_MathUtils.getPageIdByCardIndex(ii);
//			if(!wipOutputDeck.getDeckIds().contains(pageId)){
//				wipOutputDeck.getDeckIds().add(pageId);
//			}
//			//side board amount
//			for(int i = 0; i < card.getQuantity(); i++){
//				sideStateIDs.add(cardId);
//				wipOutputDeck.getCards().add(NewCardObject(cardId, card.getCardName()));
//			}
//			ii++;
//		}
	}

	public static TTS_Deck generateMainBoardTTSDeck(AbstractDeck deck){
		//main deck object state---------------------------------------------
		List<Integer> mainCardIDs = new ArrayList<Integer>();
		List<TTS_Card> mainCards = new ArrayList<TTS_Card>();
		ArrayList<Integer> mainStatePageIDs = new ArrayList<Integer>();
		if(deck.getTTSDeckMap().containsKey(CardDetails.Board.MAIN.toString())){
			List<TTS_Card> cards = deck.getTTSDeckMap().get(CardDetails.Board.MAIN.toString());
			for(TTS_Card card: cards){
				int cardId = card.getCardId();
				int pageId = card.getPageId();
				if(!mainStatePageIDs.contains(pageId)) mainStatePageIDs.add(pageId);
				mainCardIDs.add(cardId);
				mainCards.add(card);
			}
		}
		TTS_Deck mainDeck = NewDeckBaseObject(mainCardIDs, deck.getName());
		mainDeck.setCards(mainCards);
		mainDeck.setTransform(NewDeckPosObject(1, 1, 0, false, 1.0));
		mainDeck.setCustomDeck(NewPagesObject(mainStatePageIDs, deck));
		return mainDeck;
	}

	public static TTS_DeckCollection buildDecks(AbstractDeck deck){
		TTS_DeckCollection collection = new TTS_DeckCollection();

		for(String key: deck.getTTSDeckMap().keySet()){

			List<TTS_Card> cards = deck.getTTSDeckMap().get(key);
			List<Integer> pageIds = new ArrayList<Integer>();
			List<Integer> cardIds = new ArrayList<Integer>();

			for(TTS_Card card: cards){
				int cardId = card.getCardId();
				int pageId = card.getPageId();
				if(!pageIds.contains(pageId)) pageIds.add(pageId);
				cardIds.add(cardId);
			}

			String pileName = key;
			if(key.equalsIgnoreCase(CardDetails.Board.MAIN.toString())){
				pileName = deck.getName();
			}

			TTS_Deck ttsDeck = NewDeckBaseObject(cardIds, pileName);
			ttsDeck.setCards(cards);
			ttsDeck.setTransform(NewDeckPosObject(1, 1, 0, false, 1.0));
			ttsDeck.setCustomDeck(NewPagesObject(pageIds, deck));

			collection.getObjectStates().add(ttsDeck);
		}

		return collection;
	}

	public static String BuildJSONFile(AbstractDeck deck){
        String deckStr = "";

        TTS_DeckCollection deckCollection = buildDecks(deck);
        deckStr = gson.toJson(deckCollection);

		try{
			File deckDir = deck.getDeckFolder();
			String deckName = deckDir.getPath() + File.separator + deck.getName() + ".json";
			System.out.println("Saving deck to " + deckName);
			PrintWriter fileWriter = new PrintWriter(new File(deckName), StandardCharsets.UTF_8.name());
			fileWriter.write(deckStr);
			fileWriter.close();
		}catch(Exception e){
			System.out.println("Error saving deck json");
			e.printStackTrace();
		}
        return deckStr;
	}




	public static void BuildDraftJSONFile(AbstractDeck draft) throws MethodNotSupportedException {
		throw new MethodNotSupportedException("method not implemented");
//		JsonObject deckJSON = new JsonObject();
//
//		String[] emptyProps = {"SaveName", "GameMode", "Date", "Table", "Sky", "Note", "Rules", "PlayerTurn"};
//		for(String prop : emptyProps){
//			deckJSON.add(prop, new JsonPrimitive(""));
//		}
//
//		JsonArray objectStates = new JsonArray();
//
//		//token and transforms object state----------------------------------------
//		JsonArray tokenStateIds = new JsonArray();
//		ArrayList<Integer> tokenStateDeckIDs = new ArrayList<Integer>();
//		for(Token token : draft.tokens){
//			int deckID = token.jsonId/100;
//			if(!tokenStateDeckIDs.contains(deckID)) tokenStateDeckIDs.add(deckID);
//			tokenStateIds.add(new JsonPrimitive(token.jsonId));
//		}
//		for(FrogCard card : draft.transformList){
//			int deckID = card.transformJsonId/100;
//			if(!tokenStateDeckIDs.contains(deckID)) tokenStateDeckIDs.add(deckID);
//			tokenStateIds.add(new JsonPrimitive(card.transformJsonId));
//		}
//
//		JsonObject tokenState = NewDeckBaseObject(tokenStateIds, "Tokens");
//		tokenState.add("Transform", NewDeckPosObject(-3, 1, -2, true, 1.0));
//		tokenState.add("CustomDeck", NewDeckStateObject(tokenStateDeckIDs, draft));
//		if(tokenStateIds.size()>0)objectStates.add(tokenState);
//
//		//basics object state----------------------------------------
//		JsonArray basicStateIds = new JsonArray();
//		JsonArray basicContents = new JsonArray();
//		ArrayList<Integer> basicStateDeckIDs = new ArrayList<Integer>();
//		for(FrogCard card : draft.basics){
//			int deckID = card.jsonId/100;
//			if(!basicStateDeckIDs.contains(deckID)){
//				basicStateDeckIDs.add(deckID);
//			}
//			basicStateIds.add(new JsonPrimitive(card.jsonId));
//			basicContents.add(NewCardObject(card.jsonId, card.getDisplayName()));
//		}
//
//		JsonObject basicState = NewDeckBaseObject(basicStateIds, "Basic Lands");
//		basicState.add("Transform", NewDeckPosObject(-3, 1, -3, true, 1.0));
//		basicState.add("CustomDeck", NewDeckStateObject(basicStateDeckIDs, draft));
//		basicState.add("ContainedObjects", basicContents);
//		if(basicStateIds.size()>0)objectStates.add(basicState);
//
//		//pack object states---------------------------------------------
//		boolean hasMythic = draft.cardsByRarity.get(0).size() > 0;
//		if(hasMythic){
//			for(FrogCard card :	draft.cardsByRarity.get(1)){
//				draft.cardsByRarity.get(0).add(card);
//				draft.cardsByRarity.get(0).add(card);
//			}
//		}
//		for(int packi = 0; packi < draft.amountPacks; packi++){
//			JsonArray packStateIds = new JsonArray();
//			JsonArray packContents = new JsonArray();
//			ArrayList<Integer> packStateDeckIds = new ArrayList<Integer>();
//			HashSet<Integer> existingPackIds = new HashSet<Integer>();
//			for(int rarityi = 0; rarityi < DraftDeck.RARITIES.length; rarityi++){
//				if(hasMythic && rarityi == 1) continue;
//				ArrayList<FrogCard> byRarity = draft.cardsByRarity.get(rarityi);
//				if(byRarity.size() == 0)continue;
//				for(int cardi = 0; cardi < draft.boosterAmts[rarityi]; cardi++){
//					FrogCard card = null;
//					do{
//						card = byRarity.get((int)(byRarity.size() * Math.random()));
//					}while(existingPackIds.contains(card.jsonId) && Math.random() > 0.25);
//					existingPackIds.add(card.jsonId);
//					int deckID = card.jsonId/100;
//					if(!packStateDeckIds.contains(deckID)){
//						packStateDeckIds.add(deckID);
//					}
//					packStateIds.add(new JsonPrimitive(card.jsonId));
//					packContents.add(NewCardObject(card.jsonId, card.getDisplayName()));
//				}
//			}
//			JsonObject packState = NewDeckBaseObject(packStateIds, "");
//			packState.add("Transform", NewDeckPosObject(-2 + packi%6, 0, -3 + packi/6, false, 1.0));
//			packState.add("CustomDeck", NewDeckStateObject(packStateDeckIds, draft));
//			packState.add("ContainedObjects", packContents);
//			objectStates.add(packState);
//		}
//		//main obj ------------------------------------------------------------
//		deckJSON.add("ObjectStates", objectStates);
//
//		String deckStr = FrogUtils.gson.toJson(deckJSON);
//
//		try{
//			String deckName = Config.deckDir + draft.deckId + ".json";
//			System.out.println("Saving deck to " + deckName);
//			PrintWriter fileWriter = new PrintWriter(new File(deckName));
//			fileWriter.write(deckStr);
//			fileWriter.close();
//		}catch(Exception e){
//			System.out.println("Error saving deck json");
//			e.printStackTrace();
//		}
	}
}
