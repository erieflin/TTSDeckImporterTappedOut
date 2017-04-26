package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import cardbuddies.Token;
import cardbuddies.Transform;
import utils.FrogUtils;

public class Main {

	private void writeDataToFile(InputStream inputStream, String filename) {
		OutputStream os = null;

		try {

			os = new FileOutputStream(filename);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}

		} catch (Exception e) {

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static File[] getFilesFromDirectory(String directory) {
		File[] listOfFiles;
		File folder = new File(directory);
		if (!folder.exists()) {
			folder.mkdir();

		}
		listOfFiles = folder.listFiles();
		if (listOfFiles == null) {
			return new File[0];
		}
		return listOfFiles;

	}

	public static void main(String[] args) {
		CsvConverterDeck deck;
		Scanner in = new Scanner(System.in);
		String url = "";
		String commander = "";
		String name = "";
		System.out.println("Would you like to read from input files?");
		String answer = in.nextLine();
		Scanner s;

		if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
			File[] tappedOutFiles = getFilesFromDirectory("./input/tappedOut");
			File cardList = new File("./input/cardList/generated");

			if (!cardList.exists()) {
				cardList.mkdirs();
			}
			for (File file : tappedOutFiles) {

				try {
					s = new Scanner(file);

					int i = 0;
					while (s.hasNextLine()) {
						deck = new CsvConverterDeck();
						String line = s.nextLine();
						i++;
						if (i % 3 == 1) {
							url = line;

						}
						if (i % 3 == 2) {
							if (deck.getCommander().size() == 0) {
								commander = line;
							}
						}
						if (i % 3 == 0) {
							name = line;
							String response;
							try {
								String textUrl = url;
								if (!url.contains("?")) {
									textUrl += "?fmt=txt";
								}
								response = getHTML(textUrl);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}
							searchForDefaults(url, deck);
							deck.loadDeckFromText(response, commander, name);
							DeckMaker.createDeckFromCsvConverter(deck);
							File f = new File(
									cardList.getPath() + "/" + deck.getDeckMetadata().getFriendlyDeckName() + ".txt");
							PrintWriter pw = new PrintWriter(f);
							pw.write(deck.toString());
							pw.flush();
							pw.close();
						}
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			File[] cardListFiles = getFilesFromDirectory("./input/cardList");

			for (File file : cardListFiles) {
				try {
					name = file.getName().replaceAll(".txt", "");

					s = new Scanner(file);

					StringBuilder sb = new StringBuilder();
					while (s.hasNextLine()) {
						sb.append(s.nextLine() + "\n");
					}
					CsvConverterDeck.handleString(sb.toString(), name);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			deck = new CsvConverterDeck();
			System.out.println("please enter url of tapped out deck list");
			url = in.nextLine();
			searchForDefaults(url, deck);
			if (!url.contains("?")) {
				url += "?fmt=txt";
			}
			if (deck.getCommander().size() == 0) {
				System.out.println("please enter commander name");
				commander = in.nextLine();
			}
			System.out.println("please enter deck name");
			name = in.nextLine();
			String response;
			try {
				response = getHTML(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			deck.loadDeckFromText(response, commander, name);
			
		}
	}

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
		}
		StringBuilder commanderBuilder = new StringBuilder("");
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
		divsMatched = doc.select("h2");
		Element nameHeader = divsMatched.first();
		deck.setName(nameHeader.text().replaceAll("\"", ""));
	}

	public static String getHTML(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line + "\n");
		}
		rd.close();
		return result.toString();
	}
}
