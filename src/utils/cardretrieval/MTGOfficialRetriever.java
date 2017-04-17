package utils.cardretrieval;

import java.util.Arrays;
import java.util.List;

import core.Config;
import core.FrogCard;
import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;
import utils.ImageUtils;

public class MTGOfficialRetriever extends CardRetriever
{
//	public static void main(String[] args)
//	{
//		final String header = "name=";
//		String name = header+"Archangel Avacyn";
//		List<Card> cards = CardAPI.getAllCards(Arrays.asList(new String [] {name}));
//		System.out.println(cards.get(0).getImageUrl());
//		String name2 = header +"Avacyn, the Purifier";
//		List<Card> cards2 = CardAPI.getAllCards(Arrays.asList(new String [] {name2}));
//		System.out.println(cards2.get(0).getImageUrl());
//		
////		if(ImageUtils.SaveImage(url, imgname, 1.0)){
////			return true;
////		}else{
////			System.out.println("Couldn't load from " + url);
////		}
//	}

	@Override
	public boolean LoadCard(FrogCard card)
	{
		boolean success = true;
		success = LoadCard(card, false) && success;
		if(card.transformName != null) success = LoadCard(card, true) && success;
		return success;
	}
	
	public boolean LoadCard(FrogCard card, boolean isBack)
	{
		final String nameFilterHeader = "name=";
		String name = isBack ? card.transformName : card.name;
		String imgname = Config.imageDir + "MTG_" + name + ".jpg";
		
		String setString = "";
		
		if(card.set != "" && card.set != null)
		{
			final String setFilterHeader = "set=";
			String set = card.set;
			
			System.out.println("Loading " + name + " from set " + set + ", saving to " + imgname + "...");
			
			setString = " " + setFilterHeader + set;
		}
		else
			System.out.println("Loading " + name + ", saving to " + imgname + "...");
		
		List<Card> cards = CardAPI.getAllCards(Arrays.asList(new String [] {nameFilterHeader + name + setString}));
		
		while(cards.size() == 0)
		{
			if(setString != "")
			{
				System.out.println("Failed to load card from specified set, trying all sets.");
				cards = CardAPI.getAllCards(Arrays.asList(new String [] {nameFilterHeader + name}));
				setString = "";
			}
			else
			{
				throw new InternalError("ERROR, could not find card");
			}
		}
		
		if(isBack) card.transformImageFileName = imgname;
		else card.imageFileName = imgname;
		
		int counter = cards.size() - 1;
		String url = cards.get(counter).getImageUrl();
		
		while(url == null)
		{
			System.out.println("Card element " + counter + " failed...");
			if(counter > 0)
			{
				System.out.println("Trying next card...");
				url = cards.get(--counter).getImageUrl();
			}
			else
			{
				System.out.println("Ran out of cards to try");
				return false;
			}
		}
		
		if(ImageUtils.SaveImage(url, imgname, 1.0))
		{
			System.out.println("Card element " + counter + " succeeded!");
			return true;
		}
		else
		{
			System.out.println("Couldn't load from " + url);
		}
		
		return true;
	}
}
