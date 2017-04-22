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
import java.util.Scanner;

import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import cardbuddies.Token;
import cardbuddies.Transform;
import utils.FrogUtils;

public class CsvConverterDeck {
	private ArrayList<String> mainBoard = new ArrayList<String>();
	private ArrayList<String> sideBoard = new ArrayList<String>();
	private String commander = "";
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
		sb.append(commander + "\n");
		sb.append("ENDDECK" + "\n");
		sb.append("ENDDECK" + "\n");
		sb.append("\n");
		return sb.toString();
	}

	public void loadDeckFromText(String input, String commander, String name) {
		this.commander = commander;
		deckMetadata.setName(name);
		deckMetadata.setDeckName(name);
		deckMetadata.setId(name);

		Scanner scan = new Scanner(input);

		boolean main = true;
		while (scan.hasNext()) {
			String csv = scan.nextLine();
			if (!csv.equals("")) {
				if (csv.equalsIgnoreCase("Sideboard: ")) {
					main = false;
				} else {
					String[] data = csv.split("\t");
					for (int i = 0; i < Integer.parseInt(data[0]); ++i) {
						if (main) {
							if (!data[1].equalsIgnoreCase(commander)) {
							}
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
	
	public void loadDeckFromCsv(String input, String commander, String name) {
		this.commander = commander;
		deckMetadata.setName(name);
		deckMetadata.setDeckName(name);
		deckMetadata.setId(name);
		
		Scanner scan = new Scanner(input);
		if (!scan.hasNext()) {
			return;
		}
		
		while (scan.hasNext()) {
			String csv = scan.nextLine();
			
			final String[] headerList = new String[] {"Board", "Qty", "Name", "Printing", "Foil", "Alter", "Signed", "Condition", "Language"};
			
			if(csv.trim().equalsIgnoreCase("Board,Qty,Name,Printing,Foil,Alter,Signed,Condition,Languange"))
			{
				// Header line, skip.  Can be used to find order of columns if necessary
			}
			else
			{
				//Fix commas in names
				final String replacer = "~";
				csv = csv.replaceAll("\"(.*),(.*)\"", "$1" + replacer + "$2");
				
				String[] data = csv.split(",");
				
				//Replace temporary character with comma
				for(int i = 0; i < data.length; i++)
				{
					data[i] = data[i].replace(replacer, ",");
				}
				
				if(csv.contains(replacer))
				{
					System.out.println("Used replacer, temp version: " + csv);
					System.out.println("\tOriginal version: " + data[2]);
				}
				
				for (int i = 0; i < Integer.parseInt(data[1]); ++i) {
					
					String formattedName = data[2];
					
					if(data.length >= 4)
						formattedName += " [" + data[3] + "]";
					
					if (data[0].equalsIgnoreCase("side")) {
						sideBoard.add(formattedName);
					}
					if (data[0].equalsIgnoreCase("main")) {
						if (data.length >= 3 && !data[2].equalsIgnoreCase(commander)) {
							mainBoard.add(formattedName);
						}
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
					 String line = s.nextLine();
					 i++;
					 if(i%3 == 1){
						 url = line;
						 if (!url.contains("?")) {
								url += "?fmt=csv";
							}
					 }
					 if(i%3 == 2){
						 commander = line;
					 }
					 if(i%3 == 0){
						 name = line;
						 String response;
							try {
								response = getHTML(url);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}
							deck = new CsvConverterDeck();
							deck.loadDeckFromCsv(response, commander, name);
						
							File f = new File(cardList.getPath()+"/"+name+".txt");
							PrintWriter pw = new PrintWriter(f);
							pw.write(deck.toString());
							pw.flush();
							pw.close();
					 }
				 }
			} catch (FileNotFoundException e) {
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
			
		deck= new CsvConverterDeck();
		
		System.out.println("please enter url of tapped out deck list");
		 url = in.nextLine();
		if (!url.contains("?")) {
			url += "?fmt=csv";
		}
		System.out.println("please enter commander name");
		commander = in.nextLine();
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
		deck.handleLocally(name);
		}
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
