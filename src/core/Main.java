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
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
//import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.ListModel;
import javax.swing.JMenuItem;

public class Main {
	DefaultListModel deckList;
	DefaultListModel stagedList;
    JTextField folder =  new JTextField("",10);
    private JTextField url;
    private JTextField commander;
    private JTextField name;
    private DeckManager dm = new DeckManager();
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
		deckList = new DefaultListModel();
		stagedList = new DefaultListModel();
	    JPanel mainPanel = new JPanel();
	    
	    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    				
	    				JPanel AddDeckPanel = new JPanel();
	    				tabbedPane.addTab("Add Deck", null, AddDeckPanel, null);
	    				
	    				JLabel label = new JLabel("Deck Url:");
	    				
	    				url = new JTextField("", 10);
	    				
	    				JLabel label_1 = new JLabel("Commander/s:");
	    				
	    				commander = new JTextField("", 10);
	    				
	    				JLabel label_2 = new JLabel("Deck Name:");
	    				
	    				name = new JTextField("", 10);
	    				
	   

	    				
	    				JButton button = new JButton("Add deck");
	    				
	    				button.addActionListener(new ActionListener() {
	    					@Override
	    					public void actionPerformed(ActionEvent e) {
	    						DeckLocation loc = new DeckLocation(url.getText(),commander.getText(),name.getText());
	    						
	    						if(!dm.getDecks().contains(loc)){
	    							dm.getDecks().add(loc);
	    							updateListBox();
	    						}
	    					}
	    				});
	    				
	    				GroupLayout gl_AddDeckPanel = new GroupLayout(AddDeckPanel);
	    				gl_AddDeckPanel.setHorizontalGroup(
	    					gl_AddDeckPanel.createParallelGroup(Alignment.LEADING)
	    						.addGroup(gl_AddDeckPanel.createSequentialGroup()
	    							.addGap(23)
	    							.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.LEADING)
	    								.addGroup(gl_AddDeckPanel.createSequentialGroup()
	    									.addPreferredGap(ComponentPlacement.RELATED)
	    									.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.LEADING)
	    										.addComponent(label_1)
	    										.addComponent(label)))
	    								.addComponent(label_2))
	    							.addPreferredGap(ComponentPlacement.UNRELATED)
	    							.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.LEADING, false)
	    								.addComponent(commander)
	    								.addComponent(name)
	    								.addComponent(url))
	    							.addGap(134)
	    							.addComponent(button, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
	    							.addContainerGap(155, Short.MAX_VALUE))
	    				);
	    				gl_AddDeckPanel.setVerticalGroup(
	    					gl_AddDeckPanel.createParallelGroup(Alignment.LEADING)
	    						.addGroup(gl_AddDeckPanel.createSequentialGroup()
	    							.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.TRAILING)
	    								.addGroup(gl_AddDeckPanel.createSequentialGroup()
	    									.addContainerGap()
	    									.addComponent(button))
	    								.addGroup(Alignment.LEADING, gl_AddDeckPanel.createSequentialGroup()
	    									.addGap(108)
	    									.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.BASELINE)
	    										.addComponent(label)
	    										.addComponent(url, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    									.addPreferredGap(ComponentPlacement.UNRELATED)
	    									.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.LEADING)
	    										.addComponent(label_1)
	    										.addComponent(commander, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
	    							.addGap(21)
	    							.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.BASELINE)
	    								.addComponent(label_2)
	    								.addComponent(name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    							.addContainerGap(158, Short.MAX_VALUE))
	    				);
	    				AddDeckPanel.setLayout(gl_AddDeckPanel);
	    				
	    				JPanel collectionFrame = new JPanel();
	    				
	    				JPanel AddFolderPanel = new JPanel();
	    				tabbedPane.addTab("Add From Folder", null, AddFolderPanel, null);
	    				AddFolderPanel.setLayout(new BorderLayout());
	    				AddFolderPanel.add(collectionFrame, BorderLayout.CENTER);
	    						JLabel folderLabel = new JLabel("Folder Url");
	    						
	    								Button collectionButton = new Button("Add all from tapped out folder");
	    								collectionButton.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	    								collectionButton.setPreferredSize(new Dimension(80, 80));
	    								GroupLayout gl_collectionFrame = new GroupLayout(collectionFrame);
	    								gl_collectionFrame.setHorizontalGroup(
	    									gl_collectionFrame.createParallelGroup(Alignment.LEADING)
	    										.addGroup(gl_collectionFrame.createSequentialGroup()
	    											.addContainerGap()
	    											.addGroup(gl_collectionFrame.createParallelGroup(Alignment.TRAILING, false)
	    												.addComponent(folderLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	    												.addComponent(folder, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
	    											.addGap(25)
	    											.addComponent(collectionButton, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
	    											.addGap(22))
	    								);
	    								gl_collectionFrame.setVerticalGroup(
	    									gl_collectionFrame.createParallelGroup(Alignment.LEADING)
	    										.addGroup(gl_collectionFrame.createSequentialGroup()
	    											.addGap(117)
	    											.addGroup(gl_collectionFrame.createParallelGroup(Alignment.LEADING, false)
	    												.addComponent(collectionButton, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
	    												.addGroup(gl_collectionFrame.createSequentialGroup()
	    													.addComponent(folderLabel)
	    													.addPreferredGap(ComponentPlacement.RELATED)
	    													.addComponent(folder, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))))
	    								);
	    								collectionFrame.setLayout(gl_collectionFrame);
	    								
	    								JPanel ProcessPanel = new JPanel();
	    								tabbedPane.addTab("Process", null, ProcessPanel, null);
	    								
	    								JPanel panel_1 = new JPanel();
	    								
	    								JList mainListElement = new JList<DeckLocation>(deckList);
	    								
	    								JScrollPane scrollPane = new JScrollPane(mainListElement);
	    								scrollPane.setPreferredSize(new Dimension(250, 80));
	    								
	    								JPanel panel = new JPanel();
	    								
	    								JList stagedListElement = new JList<DeckLocation>(stagedList);
	    								JScrollPane listScroller = new JScrollPane(stagedListElement);
	    								listScroller.setPreferredSize(new Dimension(250, 80));
	    								
	    								Button process = new Button("Process");
	    								 process.addActionListener(new ActionListener() {
	    										@Override
	    										public void actionPerformed(ActionEvent e) {
	    											for(DeckLocation loc : dm.getStaged()){
	    												try{
	    													CsvConverterDeck deck = makeCsvDeckFromDecLocation(loc);
	    													importDeck(deck);
	    												}catch(Exception error){
	    													error.printStackTrace();
	    												}
	    											}
	    										}
	    									});
	    								JButton btnStage = new JButton(">");
	    								btnStage.addActionListener(new ActionListener() {
	    									public void actionPerformed(ActionEvent e) {
	    										dm.getStaged().add(dm.getDecks().get(mainListElement.getSelectedIndex()));
	    										dm.getDecks().remove(mainListElement.getSelectedIndex());
	    										updateListBox();
	    									}
	    								});
	    								JButton btnUnstage = new JButton("<");
	    								btnUnstage.addActionListener(new ActionListener() {
	    									public void actionPerformed(ActionEvent e) {
	    										dm.getDecks().add(dm.getStaged().get(stagedListElement.getSelectedIndex()));
	    										dm.getStaged().remove(stagedListElement.getSelectedIndex());
	    										updateListBox();
	    									}
	    								});
	    								
	    								JLabel lblAddedDecks = new JLabel("Added Decks");
	    								
	    								JLabel lblStagedDecks = new JLabel("Staged Decks");
	    								GroupLayout gl_ProcessPanel = new GroupLayout(ProcessPanel);
	    								gl_ProcessPanel.setHorizontalGroup(
	    									gl_ProcessPanel.createParallelGroup(Alignment.LEADING)
	    										.addGroup(gl_ProcessPanel.createSequentialGroup()
	    											.addContainerGap()
	    											.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
	    											.addGap(60)
	    											.addGroup(gl_ProcessPanel.createParallelGroup(Alignment.LEADING)
	    												.addComponent(btnUnstage, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
	    												.addComponent(btnStage, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
	    											.addGap(65)
	    											.addComponent(panel, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
	    											.addGap(62))
	    										.addGroup(gl_ProcessPanel.createSequentialGroup()
	    											.addGap(77)
	    											.addComponent(lblAddedDecks)
	    											.addPreferredGap(ComponentPlacement.RELATED, 304, Short.MAX_VALUE)
	    											.addComponent(lblStagedDecks)
	    											.addGap(122))
	    								);
	    								gl_ProcessPanel.setVerticalGroup(
	    									gl_ProcessPanel.createParallelGroup(Alignment.TRAILING)
	    										.addGroup(gl_ProcessPanel.createSequentialGroup()
	    											.addGap(15)
	    											.addGroup(gl_ProcessPanel.createParallelGroup(Alignment.BASELINE)
	    												.addComponent(lblAddedDecks)
	    												.addComponent(lblStagedDecks))
	    											.addGap(18)
	    											.addGroup(gl_ProcessPanel.createParallelGroup(Alignment.LEADING)
	    												.addGroup(gl_ProcessPanel.createParallelGroup(Alignment.TRAILING)
	    													.addGroup(Alignment.LEADING, gl_ProcessPanel.createSequentialGroup()
	    														.addGap(34)
	    														.addComponent(btnStage)
	    														.addGap(34)
	    														.addComponent(btnUnstage))
	    													.addGroup(gl_ProcessPanel.createSequentialGroup()
	    														.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
	    														.addGap(259)))
	    												.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    											.addGap(425))
	    								);
	    								GroupLayout gl_panel = new GroupLayout(panel);
	    								gl_panel.setHorizontalGroup(
	    									gl_panel.createParallelGroup(Alignment.LEADING)
	    										.addGroup(gl_panel.createSequentialGroup()
	    											.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    												.addComponent(stagedListElement)
	    												.addComponent(listScroller, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE))
	    											.addContainerGap(175, Short.MAX_VALUE))
	    										.addComponent(process, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
	    								);
	    								gl_panel.setVerticalGroup(
	    									gl_panel.createParallelGroup(Alignment.LEADING)
	    										.addGroup(gl_panel.createSequentialGroup()
	    											.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    												.addComponent(stagedListElement)
	    												.addComponent(listScroller, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
	    											.addPreferredGap(ComponentPlacement.RELATED)
	    											.addComponent(process, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	    											.addGap(4))
	    								);
	    								panel.setLayout(gl_panel);
	    								panel_1.setLayout(new BorderLayout(0, 0));
	    								panel_1.add(scrollPane);
	    								ProcessPanel.setLayout(gl_ProcessPanel);
	    	
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
	    frame.getContentPane().add(mainPanel);
	    GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
	    gl_mainPanel.setHorizontalGroup(
	    	gl_mainPanel.createParallelGroup(Alignment.LEADING)
	    		.addGroup(gl_mainPanel.createSequentialGroup()
	    			.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 592, GroupLayout.PREFERRED_SIZE)
	    			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	    );
	    gl_mainPanel.setVerticalGroup(
	    	gl_mainPanel.createParallelGroup(Alignment.TRAILING)
	    		.addGroup(Alignment.LEADING, gl_mainPanel.createSequentialGroup()
	    			.addContainerGap()
	    			.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 335, Short.MAX_VALUE)
	    			.addContainerGap())
	    );
	    mainPanel.setLayout(gl_mainPanel);
	    
	    JMenuBar menuBar = new JMenuBar();
	    frame.getContentPane().add(menuBar, BorderLayout.NORTH);
	    
	    JMenu menu = new JMenu("Menu");
	    menuBar.add(menu);
	    
	    JMenuItem mntmNewMenuItem = new JMenuItem("Add Decks from input dir");

	    mntmNewMenuItem.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		loadFromImportFolder();
	    		updateListBox();
	    	}
	    });
	    menu.add(mntmNewMenuItem);
	    
	    JLabel lblTest = new JLabel("");
	    menuBar.add(lblTest);
	    frame.setPreferredSize(new Dimension(600, 400));
	    frame.setResizable(false);
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private void updateListBox() {
		deckList.clear();
		for (DeckLocation loc : dm.getDecks()) {
			deckList.addElement(loc);
		}
		stagedList.clear();
		for(DeckLocation loc: dm.getStaged()){
			stagedList.addElement(loc);
		}
	}
	public CsvConverterDeck makeCsvDeckFromDecLocation(DeckLocation deckLocation){
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
			return null;
		}

		searchForDefaults(url, deck);
		deck.loadDeckFromCsv(response, deckLocation.getCommander(), deckLocation.getName());
		return deck;
	}
	public void importDeck(CsvConverterDeck deck) {
		File cardList = new File("./input/cardList/generated");
		
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
						if (!dm.getDecks().contains(location)) {
							dm.getDecks().add(location);
							updateListBox();
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
			if(!dm.getDecks().contains(location)){
				dm.getDecks().add(location);	
				updateListBox();
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
	public String toString(){
		return this.getName();
	}
}
