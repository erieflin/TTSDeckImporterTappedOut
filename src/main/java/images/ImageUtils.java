package images;

import importObjects.Card;
import importObjects.DoubleFacedCard;
import importObjects.Token;
import importObjects.deck.AbstractDeck;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.nio.file.Path;
import java.util.ArrayList;

public class ImageUtils {
	public static ArrayList<BufferedImage> freeBuffers = new ArrayList<BufferedImage>();
	public static ArrayList<BufferedImage> occupiedBuffers = new ArrayList<BufferedImage>();
//	public static ArrayList<CardRetriever> cardRetrievers = new ArrayList<CardRetriever>();

//	public static long resetTime = 0;
	
//	static{
//		cardRetrievers.add(new ScryfallCardRetriever());
////		cardRetrievers.add(new MagicCardsInfoRetriever());
////		cardRetrievers.add(new MythicSpoilerRetriever());
////		cardRetrievers.add(new GathererRetriever());
////		cardRetrievers.add(new MTGOfficialRetriever());
//	}
//
	public static BufferedImage GetBuffer(int width, int height){
		for(int i = freeBuffers.size()-1; i >= 0; i--){
			BufferedImage img = freeBuffers.get(i);
			if(img.getWidth() == width && img.getHeight() == height){
				freeBuffers.remove(i);
				occupiedBuffers.add(img);
				return img;
			}
		}

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		occupiedBuffers.add(img);
		return img;
	}
//
//	public static void FreeAllBuffers(){
//		for(int i = occupiedBuffers.size()-1; i >= 0; i--){
//			freeBuffers.add(occupiedBuffers.remove(i));
//		}
//	}
//
//	public static void AttemptClearFailedCards(){
//		long curTime = System.currentTimeMillis();
//		if(curTime >= resetTime){
//			for(CardRetriever retriever : cardRetrievers){
//				retriever.ClearFailedCards();
//			}
//
//			Calendar tomorrow = Calendar.getInstance();
//			tomorrow.set(Calendar.HOUR_OF_DAY, 0);
//			tomorrow.set(Calendar.MINUTE, 0);
//			tomorrow.set(Calendar.SECOND, 0);
//			tomorrow.add(Calendar.DAY_OF_MONTH, 1);
//
//			resetTime = tomorrow.getTimeInMillis();
//		}
//	}
//
//	public static void DownloadImages(Deck deck){
//		AttemptClearFailedCards();
//
//		for(int i = deck.cardList.size()-1; i >= 0; i--){
//			FrogCard card = deck.cardList.get(i);
//			boolean success = false;
//			for(CardRetriever retriever : cardRetrievers){
//				if(!retriever.HasCardFailed(card)){
//					success = retriever.LoadCard(card);
//					if(success) break;
//				}
//			}
//
//			if(!success){
//				deck.unknownCards.add(card);
//				deck.cardList.remove(i);
//			}
//		}
//
//		for(int i = deck.transformList.size()-1; i >= 0; i--){
//			FrogCard card = deck.transformList.get(i);
//			boolean success = false;
//			for(CardRetriever retriever : cardRetrievers){
//				if(!retriever.HasCardFailed(card)){
//					success = retriever.LoadCard(card);
//					if(success) break;
//				}
//			}
//
//			if(!success){
//				deck.cardList.remove(card);
//			}
//		}
//
//		if(deck.hiddenUrl != null && !deck.hiddenUrl.equals("default")){
//			deck.hiddenImage = ImageFromUrl(deck.hiddenUrl);
//		}
//	}
//
//	public static BufferedImage ImageFromUrl(String rawurl){
//		try{
//			URL url = new URL(rawurl);
//			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
//			httpcon.addRequestProperty("User-Agent", Config.userAgent);
//			return ImageIO.read(httpcon.getInputStream());
//		}catch(Exception e){}
//		return null;
//	}
//
//	public static boolean SaveImage(String rawurl, String destFile, double compressionLevel){
//		BufferedImage source = null;
//
//		try{
//			source = ImageFromUrl(rawurl);
//			if(source != null){
//				SaveImage(source, destFile, compressionLevel);
//				return true;
//			}
//		}catch(Exception e){}
//		return false;
//	}
//
	public static void SaveImage(StitchedImage image, double compressionLevel){
		BufferedImage source = image.getBuffer();
		String dest = image.getImagePath();
		ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
		ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
		jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpgWriteParam.setCompressionQuality((float)compressionLevel);
		try {
			File f = new File(dest);
			File dir = f.getParentFile();
			if(dir != null && !dir.exists()){
				dir.mkdirs();
			}
			if(!f.exists()){
				f.createNewFile();
			}
			jpgWriter.setOutput(new FileImageOutputStream(f));
			IIOImage outputImage = new IIOImage(source, null, null);
			jpgWriter.write(null, outputImage, jpgWriteParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jpgWriter.dispose();
	}
//
//	public static boolean CheckDraftAssets(DraftDeck draft){
//		boolean draftAssetsExist = true;
//
//		int cardsPerDeck = 69;
//
//		int regularDecks = (int) Math.ceil((draft.cardList.size() + draft.tokens.size())/(double)cardsPerDeck);
//		int transformDecks = 2 * (int) Math.ceil(draft.transformList.size()/(double)cardsPerDeck);
//		int deckAmt = regularDecks + transformDecks;
//
//		draft.deckFileNames = new String[deckAmt];
//
//		for(int i = 0; i < deckAmt; i++){
//			String cleanSetName = draft.setName.replaceAll("\\s", "_");
//			draft.deckFileNames[i] = Config.setAssetDir + cleanSetName + i + ".jpg";
//			draftAssetsExist = draftAssetsExist && new File(draft.deckFileNames[i]).exists();
//			if(!draftAssetsExist){
//				System.out.println(draft.deckFileNames[i] + " does not exist");
//			}
//		}
//
//		return draftAssetsExist;
//	}
//
	public static void StitchDeck(AbstractDeck deck){
		java.util.List<StitchedImage> stitches = new ArrayList<StitchedImage>();
		int cardsPerDeck = 69;
		
		int regularDecks = (int) Math.ceil((deck.getCardList().size() + deck.getTokenList().size())/(double)cardsPerDeck);
		int transformDecks = (int) Math.ceil(deck.getTranformList().size() / (double)cardsPerDeck);
		int deckAmt = regularDecks + transformDecks;
		
		boolean draftAssetsExist = false; // deck instanceof DraftDeck;
		
		int cardOffsetX = 10;
		int cardOffsetY = 10;
		
//		int cardOffsetX = 0;
//		int cardOffsetY = 0;
		
		int cardWidth = 745 + 2*cardOffsetX;
		int cardHeight = 1040 + 2*cardOffsetY;
		
//		int cardWidth = 223 + 2*cardOffsetX;
//		int cardHeight = 310 + 2*cardOffsetY;
		
//		deck.buffers = new BufferedImage[deckAmt];
//		deck.deckFileNames = new String[deckAmt];
//		deck.deckLinks = new String[deckAmt];
		
		Graphics[] gs = new Graphics[deckAmt];
		
		for(int i = 0; i < deckAmt; i++){
			StitchedImage stitchedImg = new StitchedImage();
			stitchedImg.setImagePath(deck.getDeckFolder().getPath() + File.separator + i + ".jpg");
			//deck.deckLinks[i] = JsonUtils.postImage(Config.hostUrlPrefix + Config.publicDeckDir + deck.deckId + i + ".jpg");
			
//			if(deck instanceof DraftDeck){
//				DraftDeck draft = (DraftDeck)deck;
//				String cleanSetName = draft.setName.replaceAll("\\s", "_");
//				deck.deckFileNames[i] = Config.setAssetDir + cleanSetName + i + ".jpg";
//				deck.deckLinks[i] = Config.hostUrlPrefix + Config.publicSetAssetDir + cleanSetName + i + ".jpg";
//				draftAssetsExist = draftAssetsExist && new File(deck.deckFileNames[i]).exists();
//			}
			BufferedImage buffer = GetBuffer(cardWidth * 10, cardHeight * 7);
			stitchedImg.setBuffer(buffer);
			gs[i] = buffer.getGraphics();
			gs[i].setColor(Color.BLACK);
			gs[i].fillRect(0, 0, cardWidth * 10, cardHeight * 7);
//			if(deck.hiddenImage != null){
//				gs[i].drawImage(deck.hiddenImage, cardWidth * 9, cardHeight * 6, cardWidth, cardHeight, null);
//			}
            stitches.add(stitchedImg);
		}
		
		int cardCount = 0;
		for(Card card : deck.getCardList()){
			int deckNum = cardCount / cardsPerDeck;
			int deckID = cardCount % cardsPerDeck;
			//card.jsonId = 100*(1+deckNum) + deckID;
			if(!draftAssetsExist){
				int gridX = deckID%10;
				int gridY = deckID/10;
	
				int realX = gridX * cardWidth + cardOffsetX;
				int realY = gridY * cardHeight + cardOffsetY;
				try {

                    File f = card.getCardImage();
                    File dir = f.getParentFile();
                    if (dir != null &&!dir.exists()) {
                            dir.mkdirs();
                    }
					BufferedImage cardImage = ImageIO.read(f);
					gs[deckNum].drawImage(cardImage, realX, realY, cardWidth - cardOffsetX*2, cardHeight - cardOffsetY*2, null);
				}catch(Exception e){
					System.out.println("Error loading from file " + card.getCardImage().getName());
					e.printStackTrace();
				}
			}
			cardCount++;
		}

		for(Token token : deck.getTokenList()){
			int deckNum = cardCount / cardsPerDeck;
			int deckID = cardCount % cardsPerDeck;
			//token.jsonId = 100*(1+deckNum) + deckID;
			if(!draftAssetsExist){
				int gridX = deckID%10;
				int gridY = deckID/10;
	
				int realX = gridX * cardWidth + cardOffsetX;
				int realY = gridY * cardHeight + cardOffsetY;
				try{
					File f = token.getCardImage();
					File dir = new File(f.getParent());
					if(!dir.exists()){
						dir.mkdirs();
					}
					BufferedImage cardImage = ImageIO.read(f);
					gs[deckNum].drawImage(cardImage, realX, realY, cardWidth - cardOffsetX*2, cardHeight - cardOffsetY*2, null);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			cardCount++;
		}

		if(cardCount%cardsPerDeck!=0){
			cardCount += cardsPerDeck - (cardCount%cardsPerDeck);
		}
		for(DoubleFacedCard card : deck.getTranformList()){
			int deckNum = cardCount / cardsPerDeck;
			int deckID = cardCount % cardsPerDeck;
			//card.transformJsonId = 100*(1+deckNum) + deckID;
			
			if(!draftAssetsExist){
				int gridX = deckID%10;
				int gridY = deckID/10;
	
				int realX = gridX * cardWidth + cardOffsetX;
				int realY = gridY * cardHeight + cardOffsetY;
				try{
					BufferedImage cardImage = ImageIO.read(card.getBackCardImage());
					gs[deckNum+1].drawImage(cardImage, realX, realY, cardWidth - cardOffsetX*2, cardHeight - cardOffsetY*2, null);
				}catch(Exception e){
					System.out.println("Err with " + card.getBackCardImage().getPath());
					e.printStackTrace();
				}
			}
			cardCount++;
			if(cardCount%cardsPerDeck==0)cardCount+=cardsPerDeck;
		}
		
		for(int i = 0; i < deckAmt; i++){
			gs[i].dispose();
			if(!draftAssetsExist){
				//deck.compressionLevel;
				SaveImage(stitches.get(i), 1);//deck.compressionLevel);
				
			}
		}
	}
}
