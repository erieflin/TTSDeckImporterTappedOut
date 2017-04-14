package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
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
		sb.append("ENDDECK"+"\n");
		sb.append("\n");
		return sb.toString();
	}

	public void loadDeckFromText(String input, String commander, String name) {
		this.commander = commander;
		deckMetadata.setName(name);
		deckMetadata.setDeckName(name);
		deckMetadata.setId(name);

		Scanner scan = new Scanner(input);
		if (!scan.hasNext()) {
			return;
		}
		scan.nextLine();
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
								mainBoard.add(data[1]);
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

	public void handleLocally(CsvConverterDeck deck, String name) {
		System.out.println(deck);
		// convert String into InputStream
		InputStream is = new ByteArrayInputStream(deck.toString().getBytes());

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
		        OutputStream outstream = socket .getOutputStream(); 
		        PrintWriter out = new PrintWriter(outstream,true);
		        Scanner scan = new Scanner(socket.getInputStream());
		        out.println(rawData);
		        String response = scan.nextLine();
		        System.out.println(response);
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		// writeDataToFile(conn.getInputStream(), "test");
		//System.out.println(EntityUtils.toString(response.getEntity()));
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

	public static void main(String[] args) {
		CsvConverterDeck deck = new CsvConverterDeck();
		Scanner in = new Scanner(System.in);
		System.out.println("please enter url of tapped out deck list");
		String url = in.nextLine();
		if (!url.contains("?")) {
			url += "?fmt=txt";
		}
		System.out.println("please enter commander name");
		String commander = in.nextLine();
		System.out.println("please enter deck name");
		String name = in.nextLine();
		String response;
		try {
			response = getHTML(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		deck.loadDeckFromText(response, commander, name);
		deck.handleLocally(deck, name);
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
