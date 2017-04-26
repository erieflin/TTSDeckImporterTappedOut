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
	public ArrayList<String> getMainBoard() {
		return mainBoard;
	}

	public void setMainBoard(ArrayList<String> mainBoard) {
		this.mainBoard = mainBoard;
	}

	public ArrayList<String> getSideBoard() {
		return sideBoard;
	}

	public void setSideBoard(ArrayList<String> sideBoard) {
		this.sideBoard = sideBoard;
	}

	private ArrayList<String> sideBoard = new ArrayList<String>();
	private List<String> commander = new ArrayList<String>();
	private static final String COMMANDERLABEL = "COMMANDER";
	private static final String SIDEBOARDLABEL = "SIDEBOARD";
	private static final String MAINBOARDLABEL = "MAINBOARD";
	private DeckMetadata deckMetadata = new DeckMetadata();

	public CsvConverterDeck() {
		getDeckMetadata().setCardBackUrl("http://i.imgur.com/P7qYTcI.png");
		getDeckMetadata().setHiddenCardUrl("http://i.imgur.com/P7qYTcI.png");
		getDeckMetadata().setDeckName("");
		getDeckMetadata().setName("1");
		getDeckMetadata().setId("");
		getDeckMetadata().setArtify("true");
		getDeckMetadata().setCoolify("false");
		getDeckMetadata().setCompression("0.9");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("deck" + "\n");
		sb.append(getDeckMetadata().toString());
		sb.append(MAINBOARDLABEL + "\n");
		for (String s : mainBoard) {
			sb.append(s + "\n");
		}
		sb.append(SIDEBOARDLABEL + "\n");
		for (String s : sideBoard) {
			sb.append(s + "\n");
		}
		sb.append(COMMANDERLABEL + "\n");
		for (String s : getCommander()) {
			sb.append(s + "\n");
		}
		sb.append("ENDDECK" + "\n");
		sb.append("ENDDECK" + "\n");
		sb.append("\n");
		return sb.toString();
	}

	public void setName(String name) {
		getDeckMetadata().setName(name);
		getDeckMetadata().setDeckName(name);
		getDeckMetadata().setId(name);
	}

	public void loadDeckFromText(String input, String commander, String name) {
		if (!commander.equalsIgnoreCase("auto")) {
			this.setCommander(commander);
		}
		if (!name.equalsIgnoreCase("auto")) {
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

							for (String cmdr : this.getCommander()) {
								if (data[1].equalsIgnoreCase(cmdr)) {
									isCommander = true;
								}
							}
							if (!isCommander) {
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

	public void setCommander(String commander) {
		this.commander = Arrays.asList(commander.split(":"));
	}

	public void handleLocally(String name) {
		handleString(toString(), name);
	}

	public static void handleString(String deckString, String name) {
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
		getDeckMetadata().setId("eTest");
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

	public void loadDeckFromCsv(String input, String commander, String name) {
		if (this.getCommander().size() == 0) {
			this.setCommander(commander);
		}

		if (getDeckMetadata().getName().equals("")) {
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

	public List<String> getCommander() {
		return commander;
	}

	public void setCommander(List<String> commander) {
		this.commander = commander;
	}

	public DeckMetadata getDeckMetadata() {
		return deckMetadata;
	}

	public void setDeckMetadata(DeckMetadata deckMetadata) {
		this.deckMetadata = deckMetadata;
	}
}
