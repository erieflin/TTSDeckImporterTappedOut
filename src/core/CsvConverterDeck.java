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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import cardbuddies.Token;
import cardbuddies.Transform;
import utils.FrogUtils;

public class CsvConverterDeck {
	private ArrayList<String> mainBoard = new ArrayList<String>();
	private ArrayList<String> sideBoard = new ArrayList<String>();
	private List<String> commander = new ArrayList<String>();
	private static final String COMMANDERLABEL = "COMMANDER";
	private static final String SIDEBOARDLABEL = "SIDEBOARD";
	private static final String MAINBOARDLABEL = "MAINBOARD";
	private DeckMetadata deckMetadata = new DeckMetadata();

	public CsvConverterDeck() {
		deckMetadata.setCardBackUrl("http://i.imgur.com/P7qYTcI.png");
		deckMetadata.setHiddenCardUrl("http://i.imgur.com/P7qYTcI.png");
		deckMetadata.setDeckName("");
		deckMetadata.setName("1");
		deckMetadata.setId("");
		deckMetadata.setArtify("true");
		deckMetadata.setCoolify("false");
		deckMetadata.setCompression("0.9");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("deck" + "\n");
		sb.append(deckMetadata.toString());
		sb.append(MAINBOARDLABEL + "\n");
		for (String s : mainBoard) {
			sb.append(s + "\n");
		}
		sb.append(SIDEBOARDLABEL + "\n");
		for (String s : sideBoard) {
			sb.append(s + "\n");
		}
		sb.append(COMMANDERLABEL + "\n");
		for(String s: commander){
			sb.append(s + "\n");		
		}
		sb.append("ENDDECK" + "\n");
		sb.append("ENDDECK" + "\n");
		sb.append("\n");
		return sb.toString();
	}
	public void setName(String name){
		deckMetadata.setName(name);
		deckMetadata.setDeckName(name);
		deckMetadata.setId(name);
	}
	public void loadDeckFromText(String input, String commander, String name) {
		if(!commander.equalsIgnoreCase("auto")){
			this.setCommander(commander);
		}
		if(!name.equalsIgnoreCase("auto")){
			setName(name);
		}

		Scanner scan = new Scanner(input);

		boolean main = true;
		boolean isCommander = false;
		while (scan.hasNext()) {
			String csv = scan.nextLine();
			if (!csv.equals("")) {
				if (csv.equalsIgnoreCase("Sideboard: ")) {
					main = false;
				} else {
					String[] data = csv.split("\t");
					for (int i = 0; i < Integer.parseInt(data[0]); ++i) {
						if (main) {
							
							for(String cmdr:this.commander){
								if (data[1].equalsIgnoreCase(cmdr)) {
									isCommander = true;
								}
							}
							if(!isCommander){
								mainBoard.add(data[1]);
							}
							isCommander = false;
						} else {
							if (data[0].equalsIgnoreCase("side")) {
								sideBoard.add(data[1]);
							}
						}
					}
				}
			}
		}
	}
	public void setCommander(String commander){
		this.commander = Arrays.asList(commander.split(":"));
	}
	public void loadDeckFromCsv(String input, String commander, String name) {
		if(this.commander.size()==0){
			this.setCommander(commander);
		}
		
		if(deckMetadata.getName().equals("")){
			setName(name);
		}
		Scanner scan = new Scanner(input);
		if (!scan.hasNext()) {
			return;
		}
		scan.nextLine();
		while (scan.hasNext()) {
			String csv = scan.nextLine();
			
			String[] data = csv.split(",");
			for (int i = 0; i < Integer.parseInt(data[1]); ++i) {
				if (data[0].equalsIgnoreCase("side")) {
					sideBoard.add(data[3]);
				}
				if (data[0].equalsIgnoreCase("main")) {
					if (!data[3].equalsIgnoreCase(commander)) {
						mainBoard.add(data[3]);
					}
				}
			}
		}

	}

	public void handleLocally(String name) {
		handleString(toString(), name);
	}
	public static void handleString(String deckString, String name){
		// convert String into InputStream
		InputStream is = new ByteArrayInputStream(deckString.getBytes());

		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		FileWriter fw;
		File f = new File(name + ".json");
		File imagef = new File("images");
		try {
			if (!imagef.exists()) {
				imagef.mkdirs();
			}

			fw = new FileWriter(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		BufferedWriter bw = new BufferedWriter(fw);
		FrogUtils.gson = new Gson();

		if (!Config.LoadConfig()) {
			return;
		}

		Token.LoadTokenMap();
		Transform.LoadTransformMap();
		DeckMaker.HandleClient(br, bw);
	}

	public void handleRemotly() throws IOException {
		deckMetadata.setId("eTest");
		String rawData = toString();
		Socket socket = null;
		try {
			socket = new Socket("http://www.frogtown.me/newdeck", 80);
			OutputStream outstream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outstream, true);
			Scanner scan = new Scanner(socket.getInputStream());
			out.println(rawData);
			String response = scan.nextLine();
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// writeDataToFile(conn.getInputStream(), "test");
		// System.out.println(EntityUtils.toString(response.getEntity()));
	}

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
		if(listOfFiles == null){
			return new File[0];
		}
		return listOfFiles;

	}

	public static void main(String[] args) {
		CsvConverterDeck deck;
		Scanner in = new Scanner(System.in);
		String url = "";
		String commander="";
		String name="";
		System.out.println("Would you like to read from input files?");
		String answer = in.nextLine();
		Scanner s;
		
		if(answer.equalsIgnoreCase("yes")||answer.equalsIgnoreCase("y")){
			File[] tappedOutFiles = getFilesFromDirectory("./input/tappedOut");
			File cardList = new File("./input/cardList");
			
			if(!cardList.exists()){
				cardList.mkdirs();
			}
			for(File file: tappedOutFiles){
				
				try {
					s = new Scanner(file);
				
				 int i =0;
				 while(s.hasNextLine()){
					 deck = new CsvConverterDeck();
					 String line = s.nextLine();
					 i++;
					 if(i%3 == 1){
						 url = line;
						
					 }
					 if(i%3 == 2){
						 if(deck.commander.size()==0){
							 commander = line;
						 }
					 }
					 if(i%3 == 0){
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
							deck.searchForDefaults(url);
							deck.loadDeckFromText(response, commander, name);
						
							File f = new File(cardList.getPath()+"/"+deck.deckMetadata.getFriendlyDeckName()+".txt");
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
			
			for(File file: cardListFiles){
				try {
				name = file.getName().replaceAll(".txt", "");
			
					s = new Scanner(file);
				
				StringBuilder sb = new StringBuilder();
				while(s.hasNextLine()){
					sb.append(s.nextLine()+"\n");
				}
				handleString(sb.toString(), name);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			deck = new CsvConverterDeck();
		System.out.println("please enter url of tapped out deck list");
		 url = in.nextLine();
		 deck.searchForDefaults(url);
		if (!url.contains("?")) {
			url += "?fmt=txt";
		}
		if(deck.commander.size()==0){
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
		deck.handleLocally(name);
		}
	}
	
	public void searchForDefaults(String url){
		 String printable;
			try {
				if(!url.endsWith("/")){
					url+="/";
				}
				printable = getHTML(url+"?fmt=printable");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			Document doc = Jsoup.parse(printable);
			Elements divsMatched = doc.select("div:has(h2:contains(commander))");
			if(divsMatched.size()==0){
				divsMatched = doc.select("div:has(h2:contains(commanders))");
			}
			StringBuilder commanderBuilder = new StringBuilder("");
			Element divWrapper = divsMatched.last();
			for(TextNode node :divWrapper.textNodes()){
				commanderBuilder.append(node.getWholeText());
			}
			String contents = commanderBuilder.toString();
			commanderBuilder = new StringBuilder();
			Scanner scan = new Scanner(contents);
			while(scan.hasNextLine()){
				String line = scan.nextLine().trim();
				if(line.startsWith("1")){
					line = line.substring(1).trim();
				}
				if(line.startsWith("x")){
					line = line.substring(1).trim();
				}
				if(!line.equals("")){
					commanderBuilder.append(line+":");
				}
			}
			String result = commanderBuilder.toString();
			if(result.endsWith(":")){
				result = result.substring(0, result.length()-1);
			}
			this.setCommander(result);
			divsMatched = doc.select("h2");
			Element nameHeader = divsMatched.first();
			this.setName(nameHeader.text().replaceAll("\"", ""));
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
