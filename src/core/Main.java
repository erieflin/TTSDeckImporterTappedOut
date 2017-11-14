package core;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class Main {
	DefaultListModel list;
	ArrayList<DeckLocation> decksToImport = new ArrayList<DeckLocation>();
    JTextField url = new JTextField("",10);
    JTextField commander =  new JTextField("",10);
    JTextField name =  new JTextField("",10);
    JTextField folder =  new JTextField("",10);
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

		Main main = new Main();
		// main.doStuff();
	}

	public Main() {
		JFrame frame = new JFrame("TappedOut->TTS Deck Importer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		list = new DefaultListModel();
		// Add the ubiquitous "Hello World" label.
		Button inputButton = new Button("Stage Decks from input dir");
		inputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadFromImportFolder();
				updateListBox();
			}
		});

		Button addUrl = new Button("Stage deck");
		addUrl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DeckLocation loc = new DeckLocation(url.getText(),commander.getText(),name.getText());
				if(!decksToImport.contains(loc)){
					decksToImport.add(loc);
				}
			}
		});

		Button collectionButton = new Button("Stage button from tappedout folder");
		collectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String folderStr = folder.getText();
				if(!folderStr.equals("")){
					loadDecksFromTappedOutFolder(folderStr);
					updateListBox();
				}
			}
		});
		
		Button process = new Button("Process");
	    process.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(DeckLocation loc : decksToImport){
					try{
						importDeck(loc);
					}catch(Exception error){
						error.printStackTrace();
					}
				}
			}
		});
	    
	    JLabel urlLabel = new JLabel("Deck Url:");
	    JLabel commanderLabel = new JLabel("Commander/s:");
	    JLabel nameLabel = new JLabel("Deck Name:");
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new GridLayout(0, 3));
	    
	    JPanel manualDeckText = new JPanel();
	    manualDeckText.setLayout(new GridLayout(0,2));
	    manualDeckText.add(urlLabel);
	    manualDeckText.add(url);
	    manualDeckText.add(commanderLabel);
	    manualDeckText.add(commander);
	    manualDeckText.add(nameLabel);
	    manualDeckText.add(name);
	    
	    JPanel manualDeckFrame = new JPanel();
	    manualDeckFrame.setLayout(new GridLayout(1,2));
	    manualDeckFrame.add(manualDeckText);
	    manualDeckFrame.add(addUrl);
	    
	    JPanel collectionFrame = new JPanel();
	    JPanel folderPanel = new JPanel();
	    JLabel folderLabel = new JLabel("Folder:");
	    
	    folderPanel.setLayout(new GridLayout(0,2));
	    folderPanel.add(folderLabel);
	    folderPanel.add(folder);
	    
	    collectionFrame.add(folderPanel);
	    collectionFrame.setLayout(new GridLayout(0,2));
	    collectionFrame.add(collectionButton);
	    
	    
	    JList jlist = new JList(list);
	    JScrollPane listScroller = new JScrollPane(jlist);
	    listScroller.setPreferredSize(new Dimension(250, 80));
	    
	    JPanel borderPanel1 = new JPanel();
	    borderPanel1.setLayout(new BorderLayout());
	    borderPanel1.add(manualDeckFrame, BorderLayout.CENTER);
	    
	    JPanel borderPanel2 = new JPanel();
	    borderPanel2.setLayout(new BorderLayout());
	    borderPanel2.add(collectionFrame, BorderLayout.CENTER);
	    
	    mainPanel.add(borderPanel1);
	    mainPanel.add(borderPanel2);
	    mainPanel.add(inputButton);
	    
        mainPanel.add(listScroller);
	    mainPanel.add(process);
	    frame.add(mainPanel);
	    frame.setPreferredSize(new Dimension(1200, 400));
	    frame.setResizable(false);
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private void updateListBox() {
		list.clear();
		for (DeckLocation loc : decksToImport) {
			list.addElement(loc.getName());
		}
	}

	public void importDeck(DeckLocation deckLocation) {
		File cardList = new File("./input/cardList/generated");
		CsvConverterDeck deck = new CsvConverterDeck();
		String url = deckLocation.getUrl();
		String response = "";
		try {
			String textUrl = url;
			if (!textUrl.contains("?")) {
				textUrl += "?fmt=csv";
			}
			response = getHTML(textUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		searchForDefaults(url, deck);
		deck.loadDeckFromCsv(response, deckLocation.getCommander(), deckLocation.getName());
		DeckMaker.createDeckFromCsvConverter(deck);
		File f = new File(cardList.getPath() + "/" + deck.getDeckMetadata().getFriendlyDeckName() + ".txt");
		PrintWriter pw;
		try {
			pw = new PrintWriter(f);
			pw.write(deck.toString());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// File[] cardListFiles = getFilesFromDirectory("./input/cardList");
		// for (File file : cardListFiles) {
		// try {
		// if (!file.isDirectory()) {
		//
		// name = file.getName().replaceAll(".csv", "");
		//
		// s = new Scanner(file);
		//
		// StringBuilder sb = new StringBuilder();
		// while (s.hasNextLine()) {
		// sb.append(s.nextLine() + "\n");
		// }
		// CsvConverterDeck.handleString(sb.toString(), name);
		// }
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	public void loadFromImportFolder() {
		String url = "";
		String commander = "";
		String name = "";
		File[] tappedOutFiles = getFilesFromDirectory("./input/tappedOut");
		File cardList = new File("./input/cardList/generated");
		CsvConverterDeck deck;
		Scanner s;
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
						commander = line;
					}
					if (i % 3 == 0) {
						name = line;
						searchForDefaults(url, deck);
						String autoName = deck.getDeckMetadata().getDeckName();
						String autoCommander = deck.getCommanderStr();
						if(!autoName.equals("")&& !autoName.equals("auto") && name.equals("auto")){
							name = autoName;
						}
						if(!autoCommander.equals("")&& !autoCommander.equals("auto") && commander.equals("auto")){
							commander = autoCommander;
						}
						DeckLocation location = new DeckLocation(url,commander , name );
						if (!decksToImport.contains(location)) {
							decksToImport.add(location);
						}
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

	}

	public void loadDecksFromTappedOutFolder(String folderName){
		ArrayList<String> deckUrls = searchDeckCollection(folderName);
		for (String deckUrl : deckUrls) {
			CsvConverterDeck deck = new CsvConverterDeck();
			deckUrl = "http://tappedout.net" + deckUrl;
			searchForDefaults(deckUrl, deck);
			DeckLocation location = new DeckLocation(deckUrl,deck.getCommanderStr(),deck.getDeckMetadata().getDeckName());
			if(!decksToImport.contains(location)){
				decksToImport.add(location);			
			}
		}
	}

	public void commandLineMain() {
		CsvConverterDeck deck;
		Scanner in = new Scanner(System.in);
		String url = "";
		String commander = "";
		String name = "";
		System.out.println("Would you like to read from input files?");
		String answer = in.nextLine();
		Scanner s;

		if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
			loadFromImportFolder();
		} else {
			System.out.println("Would you like to load from a collection");
			String input = in.nextLine();
			if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
				System.out.println("please input folder name");

			} else {
				deck = new CsvConverterDeck();
				System.out.println("please enter url of tapped out deck list");
				url = in.nextLine();
				searchForDefaults(url, deck);
				if (!url.contains("?")) {
					url += "?fmt=csv";
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
				deck.loadDeckFromCsv(response, commander, name);
				DeckMaker.createDeckFromCsvConverter(deck);
			}
		}
	}

	public static ArrayList<String> searchDeckCollection(String collectionName) {
		String url = "http://tappedout.net/mtg-deck-folders/" + collectionName;
		ArrayList<String> deckUrls = new ArrayList<String>();
		try {
			String html = getHTML(url);
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
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
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

class DeckLocation {
	private String url = "";
	private String commander = "auto";
	private String name = "auto";

	public DeckLocation() {

	}

	public DeckLocation(String url, String commander, String name) {
		this.setUrl(url);
		this.setCommander(commander);
		this.setName(name);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name.equals("")) {
			name = "auto";
		}
		this.name = name;
	}

	public String getCommander() {
		return commander;
	}

	public void setCommander(String commander) {
		if (commander.equals("")) {
			commander = "auto";
		}
		this.commander = commander;
	}
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		if( o == this){
			return true;
		}
		if(!(o instanceof DeckLocation)){
			return false;
		}
		DeckLocation test = (DeckLocation) o;
		return test.getUrl().equals(this.getUrl()) && test.getName().equals(this.getName()) && test.getCommander().equals(this.getCommander());
	}
}
