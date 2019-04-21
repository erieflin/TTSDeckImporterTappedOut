package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exportObjects.ImagePage;
import exportObjects.PageAdapter;
import exportObjects.Pages;
import exportObjects.PagesAdapter;

public class JsonUtils {
    public static Gson gson;
    public JsonUtils(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Pages.class, new PagesAdapter());
        gsonBuilder.registerTypeAdapter(ImagePage.class, new PageAdapter());
        gson = gsonBuilder.create();
    }
//	public static JsonObject NewDeckBaseObject(JsonArray stateIDs, String name){
//		JsonObject colorObj = new JsonObject();
//		colorObj.add("r", new JsonPrimitive(0.713235259));
//		colorObj.add("g", new JsonPrimitive(0.713235259));
//		colorObj.add("b", new JsonPrimitive(0.713235259));
//
//		JsonObject baseState = new JsonObject();
//		baseState.add("Name", new JsonPrimitive(stateIDs.size() == 1 ? "Card" : "DeckCustom"));
//		baseState.add("Nickname", new JsonPrimitive(name));
//		baseState.add("Description", new JsonPrimitive(""));
//		baseState.add("Grid", new JsonPrimitive(true));
//		baseState.add("Locked", new JsonPrimitive(false));
//		baseState.add("SidewaysCard", new JsonPrimitive(false));
//
//		baseState.add("GUID", new JsonPrimitive(UUID.randomUUID().toString()));
//		baseState.add("ColorDiffuse", colorObj);
//
//		if(stateIDs.size() == 1){
//			baseState.add("CardID", stateIDs.get(0));
//		}else{
//			baseState.add("DeckIDs", stateIDs);
//		}
//		return baseState;
//	}
//
//	public static TransformObj NewDeckPosObject(int x, int y, int z, boolean faceup, double scale){
//		TransformObj transform = new TransformObj();
//		transform.setPosX(2.5 * x);
//		transform.setPosY(2.5 * y);
//		transform.setPosZ(3.5 * z);
//		transform.setRotX(0);
//		transform.setRotY(180);
//		transform.setRotZ(faceup ? 0 : 180);
//		transform.setScaleX(scale);
//		transform.setScaleY(scale);
//		transform.setScaleZ(scale);
//
//		return transform;
//	}
//	public static String postImage(String f){
//		//TODO: revisit to ensure still works, add libraries to maven
////		if(f.startsWith(".")){
////			f = f.substring(1);
////		}
////		HttpClient httpclient = new DefaultHttpClient();
////	    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
////
////	    HttpPost httppost = new HttpPost("https://imagebin.ca/upload.php");
////	    File file = new File(f);
////
////	    MultipartEntity mpEntity = new MultipartEntity();
////	    ContentBody cbFile = new FileBody(file, "image/jpeg");
////	    mpEntity.addPart("file", cbFile);
////
////
////	    httppost.setEntity(mpEntity);
////	    System.out.println("executing request " + httppost.getRequestLine());
////	    HttpResponse response;
////		try {
////			response = httpclient.execute(httppost);
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			return "";
////		}
////	    HttpEntity resEntity = response.getEntity();
////
////	    System.out.println(response.getStatusLine());
////	    if (resEntity != null) {
////	      try {
////			String res = EntityUtils.toString(resEntity);
////			String[] resSplit = res.split("\n");
////			return resSplit[1].substring(resSplit[1].indexOf(':')+1);
////		} catch (ParseException | IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	    }
////	    if (resEntity != null) {
////	      try {
////	    	  StringWriter writer = new StringWriter();
////	    	  IOUtils.copy(resEntity.getContent(), writer);
////	    	  return  writer.toString();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			return "";
////		}
////	    }
//	    return "";
//	}
//	public static JsonObject NewDeckStateObject(ArrayList<Integer> deckIds, AbstractDeck deck){
//		int cardsPerDeck = 69;
//		int regularDecks = (int) Math.ceil((deck.getCardList().size() + deck.getTokenList().size())/(double)cardsPerDeck);
//		JsonObject deckStateObject = new JsonObject();
//		for(int i = 0; i < deckIds.size(); i++){
//			int deckID = deckIds.get(i);
//			int transId = deckID - regularDecks - 1;
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
//		}
//
//		return deckStateObject;
//	}
//
//	public static TTS_Card NewCardObject(int id, String name){
//		TTS_Card cardObject = new TTS_Card();
//		cardObject.setName("Card");
//		cardObject.setNickname(""+name);
//		cardObject.setCardId(id);
//		cardObject.setTransform(NewDeckPosObject(1, 1, 1, false, 1.0));
//
//		return cardObject;
//	}
//
//	public static void BuildJSONFile(AbstractDeck deck){
//
//		JsonObject deckJSON = new JsonObject();
//
//		String[] emptyProps = {"SaveName", "GameMode", "Date", "Table", "Sky", "Note", "Rules", "PlayerTurn"};
//		for(String prop : emptyProps){
//			deckJSON.add(prop, new JsonPrimitive(""));
//		}
//
//		JsonArray objectStates = new JsonArray();
//
//		//commander object state----------------------------------------
//		int curStateIndex = 0;
//		JsonArray commanderStateIDs = new JsonArray();
//		JsonArray commanderContents = new JsonArray();
//		ArrayList<Integer> commanderStateDeckIDs = new ArrayList<Integer>();
//
//		/* look for which card is the commander needs to be revisited and improved
//		for(FrogCard card : deck.getCardList()){
//			if(card.amounts[curStateIndex] == 0)continue;
//			int deckID = card.jsonId/100;
//			if(!commanderStateDeckIDs.contains(deckID)) commanderStateDeckIDs.add(deckID);
//			commanderStateIDs.add(new JsonPrimitive(card.jsonId));
//			commanderContents.add(NewCardObject(card.jsonId, card.getDisplayName()));
//		} */
//
//		JsonObject commanderState = NewDeckBaseObject(commanderStateIDs, "Commander");
//		commanderState.add("ContainedObjects", commanderContents);
//		commanderState.add("Transform", NewDeckPosObject(2, 1, 2, true, 1.25));
//		commanderState.add("CustomDeck", NewDeckStateObject(commanderStateDeckIDs, deck));
//		if(commanderStateIDs.size()>0)objectStates.add(commanderState);
//
//		//token and transform object state----------------------------------------
//		JsonArray tokenStateIds = new JsonArray();
//		ArrayList<Integer> tokenStateDeckIDs = new ArrayList<Integer>();
//		for(Token token : deck.getTokenList()){
//			int deckID = token.jsonId/100; // what is theis
//			if(!tokenStateDeckIDs.contains(deckID)) tokenStateDeckIDs.add(deckID);
//			tokenStateIds.add(new JsonPrimitive(token.jsonId));
//		}
//		for(FrogCard card : deck.transformList){
//			int deckID = card.transformJsonId/100;
//			if(!tokenStateDeckIDs.contains(deckID)) tokenStateDeckIDs.add(deckID);
//			tokenStateIds.add(new JsonPrimitive(card.transformJsonId));
//		}
//
//		JsonObject tokenState = NewDeckBaseObject(tokenStateIds, "Tokens");
//		tokenState.add("Transform", NewDeckPosObject(0, 1, 0, true, 1.0));
//		tokenState.add("CustomDeck", NewDeckStateObject(tokenStateDeckIDs, deck));
//		if(tokenStateIds.size()>0)objectStates.add(tokenState);
//
//		//main deck object state---------------------------------------------
//		curStateIndex = 1;
//		JsonArray mainStateIDs = new JsonArray();
//		JsonArray mainContents = new JsonArray();
//		ArrayList<Integer> mainStateDeckIDs = new ArrayList<Integer>();
//		for(FrogCard card : deck.cardList){
//			if(card.amounts[curStateIndex] == 0)continue;
//			int deckID = card.jsonId/100;
//			if(!mainStateDeckIDs.contains(deckID)) mainStateDeckIDs.add(deckID);
//			for(int i = 0; i < card.amounts[1]; i++){
//				mainStateIDs.add(new JsonPrimitive(card.jsonId));
//				mainContents.add(NewCardObject(card.jsonId, card.getDisplayName()));
//			}
//		}
//		JsonObject mainState = NewDeckBaseObject(mainStateIDs, deck.name);
//		mainState.add("ContainedObjects", mainContents);
//		mainState.add("Transform", NewDeckPosObject(1, 1, 0, false, 1.0));
//		mainState.add("CustomDeck", NewDeckStateObject(mainStateDeckIDs, deck));
//		if(mainStateIDs.size()>0)objectStates.add(mainState);
//
//		//side board state ---------------------------------------------------
//		//JsonArray sideContents = new JsonArray();
////		TTS_Deck WIPSideDeck = new TTS_Deck();
////		List<TTS_Card> sideContents = WIPSideDeck.getCards();
////		List<Integer> sideStateDeckIDs = WIPSideDeck.getDeckIds();
////		populateSideBoard(WIPSideDeck, deck, sideStateIDs);
//		curStateIndex = 2;
//		JsonArray sideStateIDs = new JsonArray();
//		JsonArray sideContents = new JsonArray();
//		ArrayList<Integer> sideStateDeckIDs = new ArrayList<Integer>();
//		for(FrogCard card : deck.cardList){
//			if(card.amounts[curStateIndex] == 0)continue;
//			int deckID = card.jsonId/100;
//			if(!sideStateDeckIDs.contains(deckID)){
//				sideStateDeckIDs.add(deckID);
//			}
//			for(int i = 0; i < card.amounts[2]; i++){
//				sideStateIDs.add(new JsonPrimitive(card.jsonId));
//				sideContents.add(NewCardObject(card.jsonId, card.getDisplayName()));
//			}
//		}
//
//
//		for(FrogCard card : deck.transformList){
//			if(card.amounts[curStateIndex] == 0)continue;
//			int deckID = card.jsonId/100;
//			if(!sideStateDeckIDs.contains(deckID)){
//				sideStateDeckIDs.add(deckID);
//			}
//			for(int i = 0; i < card.amounts[1]; i++){
//				sideStateIDs.add(new JsonPrimitive(card.jsonId));
//				sideContents.add(NewCardObject(card.jsonId, card.getDisplayName()));
//			}
//		}
//		JsonObject sideState = NewDeckBaseObject(sideStateIDs, "Sideboard");
//		sideState.add("ContainedObjects", sideContents);
//		sideState.add("Transform", NewDeckPosObject(2, 1, 0, false, 1.0));
//		sideState.add("CustomDeck", NewDeckStateObject(sideStateDeckIDs, deck));
//		if(sideStateIDs.size()>0)objectStates.add(sideState);
//
//		//main obj ------------------------------------------------------------
//		deckJSON.add("ObjectStates", objectStates);
//
//		String deckStr = FrogUtils.gson.toJson(deckJSON);
//
//		try{
//			File deckDir = new File(Config.deckDir);
//			if(!deckDir.exists()){deckDir.mkdirs();}
//			String deckName = Config.deckDir + deck.deckId + ".json";
//			System.out.println("Saving deck to " + deckName);
//			PrintWriter fileWriter = new PrintWriter(new File(deckName),StandardCharsets.UTF_8.name());
//			fileWriter.write(deckStr);
//			fileWriter.close();
//		}catch(Exception e){
//			System.out.println("Error saving deck json");
//			e.printStackTrace();
//		}
//	}
//
//	public static void populateSideBoard(TTS_Deck wipOutputDeck, AbstractDeck sourceDeck, List<Integer> sideStateIDs){
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
//	}
//	public static void BuildDraftJSONFile(DraftDeck draft){
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
//	}
}
