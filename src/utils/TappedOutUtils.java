package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import core.CsvConverterDeck;

public class TappedOutUtils {
	public static void searchForDefaults(String url, CsvConverterDeck deck) {
		String printable;
		try {
			if (!url.endsWith("/")) {
				url += "/";
			}
			printable = getHTML(url + "?fmt=printable");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Document doc = Jsoup.parse(printable);
		Elements divsMatched = doc.select("div:has(h2:contains(commander))");
		if (divsMatched.size() == 0) {
			divsMatched = doc.select("div:has(h2:contains(commanders))");
			if (divsMatched.size() == 0) {
				divsMatched = null;
			}
		}
		StringBuilder commanderBuilder = new StringBuilder("");
		
		if(divsMatched != null)
		{
			Element divWrapper = divsMatched.last();
		
			for (TextNode node : divWrapper.textNodes()) {
				commanderBuilder.append(node.getWholeText());
			}
			
			String contents = commanderBuilder.toString();
			commanderBuilder = new StringBuilder();
			Scanner scan = new Scanner(contents);
			while (scan.hasNextLine()) {
				String line = scan.nextLine().trim();
				if (line.startsWith("1")) {
					line = line.substring(1).trim();
				}
				if (line.startsWith("x")) {
					line = line.substring(1).trim();
				}
				if (!line.equals("")) {
					commanderBuilder.append(line + ":");
				}
			}
			String result = commanderBuilder.toString();
			if (result.endsWith(":")) {
				result = result.substring(0, result.length() - 1);
			}
			deck.setCommander(result);
		}
		
		divsMatched = doc.select("h2");
		Element nameHeader = divsMatched.first();
		deck.setName(nameHeader.text().replaceAll("\"", ""));
	}

	public static String getHTML(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line + "\n");
		}
		rd.close();
		return result.toString();
	}
	public static ArrayList<String> searchDeckCollection(String collectionName) {
		String url = "http://tappedout.net/mtg-deck-folders/" + collectionName;
		ArrayList<String> deckUrls = new ArrayList<String>();
		try {
			String html = TappedOutUtils.getHTML(url);
			Document doc = Jsoup.parse(html);
			Elements mediumMatched = doc.getElementsByClass("medium-deck-name");
			Elements longMatched = doc.getElementsByClass("long-deck-name");
			Elements shortMatched = doc.getElementsByClass("short-deck-name");
			mediumMatched.addAll(0, longMatched);
			mediumMatched.addAll(shortMatched);
			Elements linked = mediumMatched.select("a");
			for (Element e : linked) {
				String deckUrl = e.attr("href");
				deckUrls.add(deckUrl);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deckUrls;
	}
	
}
