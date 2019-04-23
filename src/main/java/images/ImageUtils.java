package images;

import core.CardUtils;
import core.Constants;
import core.TTS_MathUtils;
import exportObjects.TTS_Card;
import importObjects.*;
import importObjects.deck.AbstractDeck;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
	public static void FreeAllBuffers(){
		for(int i = occupiedBuffers.size()-1; i >= 0; i--){
			freeBuffers.add(occupiedBuffers.remove(i));
		}
	}
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

	/***
	 * Saves a stitched image at a given compression level,
	 * @param image
	 * @param compressionLevel
	 * @return
	 */
	public static void SaveImage(StitchedImage image, double compressionLevel){
		BufferedImage source = image.getBuffer();
		String dest = image.getImagePath();
		ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
		ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
		jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpgWriteParam.setCompressionQuality((float)compressionLevel);
		File f = null;
		try {
			f = new File(dest);
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

	public static void addCardToGraphics(int cardNum, File cardFile,  Graphics gs ){
		int gridX = cardNum%10;
		int gridY = cardNum/10;

		int realX = gridX * TTS_MathUtils.CARDWIDTH + TTS_MathUtils.CARDOFFSETX;
		int realY = gridY * TTS_MathUtils.CARDHEIGHT + TTS_MathUtils.CARDOFFSETY;
		try {
			File dir = cardFile.getParentFile();
			if (dir != null &&!dir.exists()) {
				dir.mkdirs();
			}
			BufferedImage cardImage = ImageIO.read(cardFile);
			gs.drawImage(
					cardImage, realX, realY,
					TTS_MathUtils.CARDWIDTH - TTS_MathUtils.CARDOFFSETX*2,
					TTS_MathUtils.CARDHEIGHT - TTS_MathUtils.CARDOFFSETY*2,
					null
			);
		}catch(Exception e){
			System.out.println("Error loading from file " + cardFile.getName());
			e.printStackTrace();
		}
	}

	public static int stitchCards(AbstractDeck deck, List<? extends BaseCard> cards, int startCount, Graphics[] gs){
		for(BaseCard card : cards){
			int pageId = TTS_MathUtils.getPageIdByCardIndex(startCount);
			int cardNum = TTS_MathUtils.getCardNumInPage(startCount);
			int cardId = TTS_MathUtils.getCardIdByCardIndex(startCount);

			TTS_Card ttsCard = new TTS_Card();
			ttsCard.setNickname(card.getCardName());
			ttsCard.setCardId(cardId);
			ttsCard.setPageId(pageId);

			String board = CardDetails.Board.SIDEBOARD.toString();
			if(CardUtils.checkCardHasTag(card, CardDetails.Tag.COMMANDER)){
				board = Constants.COMMANDER;
			}

			if(card instanceof  DoubleFacedCard) {
				DoubleFacedCard dfCard = ((DoubleFacedCard) card);
				deck.addCardToTTSDeckMap(board, ttsCard);
				deck.addCardToTTSDeckMap(Constants.TRANSFORMKEY, ttsCard);
				addCardToGraphics(cardNum, dfCard.getCardImage(), gs[pageId-1]);
				addCardToGraphics(cardNum, dfCard.getBackCardImage(), gs[pageId]);
				startCount++;
			} else {
				if( card instanceof  Card){
					Card cardObj = (Card) card;
					board = cardObj.getBoard().toString();
					deck.addCardToTTSDeckMap(board, ttsCard);
				}
				else if (card instanceof Token) {
					deck.addCardToTTSDeckMap(Constants.TOKENKEY, ttsCard);
				} else {
					deck.addCardToTTSDeckMap(board, ttsCard);
				}

				addCardToGraphics(cardNum, card.getCardImage(), gs[pageId - 1]);
				startCount++;
			}
		}
		return startCount;
	}

	public static Graphics[] initGraphics(AbstractDeck deck, List<StitchedImage> stitches){
		int deckAmt = TTS_MathUtils.getNumRequiredDeckPages(deck);
		Graphics[] gs = new Graphics[deckAmt];

		for(int i = 0; i < deckAmt; i++)

		{
		StitchedImage stitchedImg = new StitchedImage();
		stitchedImg.setImagePath(deck.getDeckFolder().getPath() + File.separator + i + ".jpg");

		BufferedImage buffer = GetBuffer(TTS_MathUtils.CARDWIDTH * 10, TTS_MathUtils.CARDHEIGHT * 7);
		stitchedImg.setBuffer(buffer);
		gs[i] = buffer.getGraphics();
		gs[i].setColor(Color.BLACK);
		gs[i].fillRect(0, 0, TTS_MathUtils.CARDWIDTH * 10, TTS_MathUtils.CARDHEIGHT * 7);
		// TODO: Add card back here
		if (deck.getBackImage() != null) {
			try {
				BufferedImage cardImage = ImageIO.read(deck.getBackImage());
				gs[i].drawImage(cardImage, TTS_MathUtils.CARDWIDTH * 9, TTS_MathUtils.CARDHEIGHT * 6,
						TTS_MathUtils.CARDWIDTH, TTS_MathUtils.CARDHEIGHT, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		stitches.add(stitchedImg);
		}
		return gs;
	}

	public static void StitchDeck(AbstractDeck deck){
		List<StitchedImage> stitches = new ArrayList<StitchedImage>();

		Graphics[] gs = initGraphics(deck,stitches);

		int cardCount = 0;
		cardCount = stitchCards(deck, deck.getCardListWithoutTransforms(), cardCount, gs);

		cardCount = TTS_MathUtils.getStartOfNextPage(cardCount);

		cardCount = stitchCards(deck, deck.getTokenList(), cardCount, gs);

		cardCount = TTS_MathUtils.getStartOfNextPage(cardCount);

		cardCount =  stitchCards(deck, deck.getTransformList(), cardCount, gs);

		for(int i = 0; i < gs.length; i++) {
			gs[i].dispose();
		}

		for(int i = 0; i < stitches.size(); i++) {
			SaveImage(stitches.get(i), deck.getImageOutCompressionLevel());
			String resultUrl = postImage(stitches.get(i).getImagePath());
			System.out.println("uploaded to " + resultUrl);
			deck.getHostedImageUrls().add(resultUrl);
		}

		FreeAllBuffers();
	}

	public static String postImage(String f){
		if(f.startsWith(".")){
			f = f.substring(1);
		}
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {


			URIBuilder builder = new URIBuilder("https://imagebin.ca/upload.php");
				//builder.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			URI uri = builder.build();
			HttpPost httpPost = new HttpPost(uri);
			File file = new File(f);

			ContentBody cbFile = new FileBody(file, ContentType.create("image/jpeg"));

			MultipartEntityBuilder mpBuilder = MultipartEntityBuilder.create();
			mpBuilder.addPart("file", cbFile);

			HttpEntity mpEntity = mpBuilder.build();
			httpPost.setEntity(mpEntity);
			System.out.println("executing request " + httpPost.getRequestLine());

			HttpResponse response;
			try {
				response = httpClient.execute(httpPost);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}

			HttpEntity resEntity = response.getEntity();
			System.out.println(response.getStatusLine());

			if (resEntity != null) {
				try {
					String res = EntityUtils.toString(resEntity);
					String[] resSplit = res.split("\n");
					return resSplit[1].substring(resSplit[1].indexOf(':')+1);
				} catch (ParseException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (resEntity != null) {
				try {
					StringWriter writer = new StringWriter();
					IOUtils.copy(resEntity.getContent(), writer, "utf8");
					return  writer.toString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "";
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}






		return "";
	}

	public static boolean downloadCardImageToFile(File destFile, String uri){
		try(InputStream in = new URL(uri).openStream()){
			Files.copy(in, destFile.toPath());
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
