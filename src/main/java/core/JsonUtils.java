package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constants.TTS_DeckConstants;
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
		 return NewPagesObject(deckIds,deck,false);
	}

	public static Pages NewPagesObject(List<Integer> deckIds, AbstractDeck deck, boolean uniqueBack){
		int cardsPerDeck = 69;

		Pages pages = new Pages();
		for(int i = 0; i < deckIds.size(); i++){
			int deckID = deckIds.get(i);
			ImagePage page = new ImagePage();
			page.setPageNumber(deckID);
			pages.getPageList().add(page);
			if(uniqueBack){
				page.setFaceUrl(deck.getHostedImageUrls().get(deckID));
				page.setBackUrl(deck.getHostedImageUrls().get(deckID-1)+ "{Unique}");
			}else{
				page.setFaceUrl(deck.getHostedImageUrls().get(deckID-1));
				page.setBackUrl(deck.getHostedImageUrls().get(deckID-1));

			}


		}

		return pages;
	}

	public static TTS_DeckCollection buildDecks(AbstractDeck deck){
		TTS_DeckCollection collection = new TTS_DeckCollection();
        int ii =0;
		for(String key: deck.getTTSDeckMap().keySet()){
            ii++;
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

			boolean hasUniqueBack = key.equals(TTS_DeckConstants.TRANSFORMKEY);
			ttsDeck.setCustomDeck(NewPagesObject(pageIds, deck, hasUniqueBack));

			ttsDeck.setTransform(NewDeckPosObject(ii, ii, 0, hasUniqueBack, 1.0));


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
