package core;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
//import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;

import utils.TappedOutUtils;
import java.awt.Component;
import javax.swing.JTextPane;

public class Main {
	static DefaultListModel<String> deckList;
	static DefaultListModel<String> stagedList;
	JTextField folder = new JTextField("", 10);
	private JTextField url;
	private JTextField commander;
	private JTextField name;
	private int step = 0;
	private static DeckManager dm = DeckManager.getInstance();
	private boolean processing = true;

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
	}

	public Main() {
		JFrame frame = new JFrame("TappedOut->TTS Deck Importer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		deckList = new DefaultListModel();
		stagedList = new DefaultListModel();
		
		BufferedImage bi;
		try {
			bi = ImageIO.read(new File("resources/mtgSpinner.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		JTextArea progressLabel = new JTextArea();
		progressLabel.setEditable(false);  
		progressLabel.setCursor(null);  
		progressLabel.setOpaque(false);  
		progressLabel.setFocusable(false);
		progressLabel.setLineWrap(true);
		progressLabel.setWrapStyleWord(true);
		JPanel spinnerPanel = new JPanel() {
			int i = 0;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(bi.getWidth(), bi.getHeight());
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;

				//g2.rotate(Math.toRadians(45 * step), bi.getWidth(), bi.getHeight());
				g2.rotate(Math.toRadians(9* step), bi.getWidth() / 2, bi.getHeight() / 2);

				g2.drawImage(bi, 0, 0, null);
			}
		};
		spinnerPanel.setVisible(false);
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
				new Thread(new Runnable() {
					public void run() {
						CsvConverterDeck deck = new CsvConverterDeck();
						deck.setCommander(commander.getText());
						deck.setName(commander.getName());
						deck.setCardListUrl(url.getText());

						if (!dm.getDecks().contains(deck)) {
							dm.addDeck(deck);
							updateListBox();
						}
					}
				}).start();
			}
		});

		GroupLayout gl_AddDeckPanel = new GroupLayout(AddDeckPanel);
		gl_AddDeckPanel.setHorizontalGroup(gl_AddDeckPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_AddDeckPanel.createSequentialGroup().addGap(23).addGroup(gl_AddDeckPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_AddDeckPanel.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.LEADING).addComponent(label_1)
										.addComponent(label)))
						.addComponent(label_2))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.LEADING, false).addComponent(commander)
								.addComponent(name).addComponent(url))
						.addGap(134).addComponent(button, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(155, Short.MAX_VALUE)));
		gl_AddDeckPanel.setVerticalGroup(gl_AddDeckPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_AddDeckPanel
				.createSequentialGroup()
				.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_AddDeckPanel.createSequentialGroup().addContainerGap().addComponent(button))
						.addGroup(Alignment.LEADING, gl_AddDeckPanel.createSequentialGroup().addGap(108)
								.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.BASELINE).addComponent(label)
										.addComponent(url, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(label_1).addComponent(commander, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
				.addGap(21)
				.addGroup(gl_AddDeckPanel.createParallelGroup(Alignment.BASELINE).addComponent(label_2).addComponent(
						name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(158, Short.MAX_VALUE)));
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
		gl_collectionFrame.setHorizontalGroup(gl_collectionFrame.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_collectionFrame.createSequentialGroup().addContainerGap()
						.addGroup(gl_collectionFrame.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(folderLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(folder, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
						.addGap(25)
						.addComponent(collectionButton, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
						.addGap(22)));
		gl_collectionFrame.setVerticalGroup(gl_collectionFrame.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_collectionFrame.createSequentialGroup().addGap(117)
						.addGroup(gl_collectionFrame.createParallelGroup(Alignment.LEADING, false)
								.addComponent(collectionButton, Alignment.TRAILING, 0, 0, Short.MAX_VALUE).addGroup(
										gl_collectionFrame.createSequentialGroup().addComponent(folderLabel)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(folder,
														GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))));
		collectionFrame.setLayout(gl_collectionFrame);

		JPanel ProcessPanel = new JPanel();
		tabbedPane.addTab("Process", null, ProcessPanel, null);

		JPanel deckPanel = new JPanel();

		JList mainListElement = new JList<String>(deckList);
		mainListElement.setPreferredSize(new Dimension(10, 10));
		mainListElement.setRequestFocusEnabled(false);

		JScrollPane scrollPane = new JScrollPane(mainListElement);
		scrollPane.setPreferredSize(new Dimension(250, 60));

		JLabel lblAddedDecks = new JLabel("Added Decks");

		JLabel lblStagedDecks = new JLabel("Staged Decks");

		JPanel ActionsPanel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JList stagedListElement = new JList<String>(stagedList);
		stagedListElement.setPreferredSize(new Dimension(10, 10));
		stagedListElement.setRequestFocusEnabled(false);
		
		JScrollPane scrollPane_2 = new JScrollPane(stagedListElement);
		scrollPane_2.setPreferredSize(new Dimension(250, 60));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
		);
		panel_1.setLayout(gl_panel_1);
		dm.loadDecks();
		JButton process = new JButton("Process");
		 process.addActionListener(new ActionListener() {
			                        @Override
			                        public void actionPerformed(ActionEvent e) {
			 
			                                new Thread(new Runnable() {
			                                        public void run() {
			                                                processing=true;
			                                                spinnerPanel.setVisible(true);
			                                                String deckNames = "";
			 
			                                                @SuppressWarnings("unchecked")
			                                                ArrayList<CsvConverterDeck> newStaged = (ArrayList<CsvConverterDeck>) dm.getStaged().clone();
			                                                int index = 0;
			                                                String seperator = "";
			                                                for (CsvConverterDeck deck : dm.getStaged()) {
			                                                        try {
			                                                                progressLabel.setText("Importing deck "+ deck.getDeckMetadata().getName());
			                                                                importDeck(deck);
			                                                                dm.addDeck(dm.getStaged().get(0));
			                                                                newStaged.remove(index);
			                                                                updateListBox();
			                                                                deckNames= deckNames+seperator + deck.getDeckMetadata().getName();
			                                                        } catch (Exception error) {
			                                                                error.printStackTrace();
			                                                        }
			                                                        index++;
			                                                }
			                                                processing = false;
			                                                spinnerPanel.setVisible(false);
			                                                progressLabel.setText("finished Processing decks "+ deckNames);
			                                                dm.setStaged(newStaged);
			                                        }
			                                }).start();
			 
			                        }
			                });
		
	

		GroupLayout gl_ProcessActions = new GroupLayout(ProcessPanel);
		gl_ProcessActions.setHorizontalGroup(
			gl_ProcessActions.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_ProcessActions.createSequentialGroup()
					.addGroup(gl_ProcessActions.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_ProcessActions.createSequentialGroup()
							.addGap(75)
							.addComponent(lblAddedDecks))
						.addGroup(gl_ProcessActions.createSequentialGroup()
							.addGap(21)
							.addGroup(gl_ProcessActions.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_ProcessActions.createSequentialGroup()
									.addComponent(deckPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(30)
									.addComponent(ActionsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(40)
									.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_ProcessActions.createSequentialGroup()
									.addGap(425)
									.addComponent(lblStagedDecks)))))
					.addContainerGap(34, Short.MAX_VALUE))
				.addGroup(gl_ProcessActions.createSequentialGroup()
					.addContainerGap(435, Short.MAX_VALUE)
					.addComponent(process)
					.addGap(105))
				.addGroup(Alignment.LEADING, gl_ProcessActions.createSequentialGroup()
					.addGap(219)
					.addComponent(progressLabel, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(212, Short.MAX_VALUE))
		);
		gl_ProcessActions.setVerticalGroup(
			gl_ProcessActions.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_ProcessActions.createSequentialGroup()
					.addComponent(progressLabel, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ProcessActions.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAddedDecks)
						.addComponent(lblStagedDecks))
					.addGap(18)
					.addGroup(gl_ProcessActions.createParallelGroup(Alignment.LEADING, false)
						.addComponent(ActionsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
						.addComponent(deckPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(process)
					.addGap(41))
		);
		JButton btnStage = new JButton(">");
		JButton btnUnstage = new JButton("<");

		

	

		Timer timer = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (processing) {	
					step++;
					if (step > 40) {
						step = 0;
					}
				} else {
					step = 0;
				}
				spinnerPanel.repaint();
			}

		});
		timer.start();
		GroupLayout gl_ActionsPanel = new GroupLayout(ActionsPanel);
		gl_ActionsPanel.setHorizontalGroup(
			gl_ActionsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ActionsPanel.createSequentialGroup()
					.addGap(31)
					.addGroup(gl_ActionsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(spinnerPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnUnstage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnStage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(40))
		);
		gl_ActionsPanel.setVerticalGroup(
			gl_ActionsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ActionsPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnStage)
					.addGap(18)
					.addComponent(btnUnstage)
					.addGap(37)
					.addComponent(spinnerPanel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(21, Short.MAX_VALUE))
		);
		ActionsPanel.setLayout(gl_ActionsPanel);
		btnUnstage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						dm.addDeck(dm.getStaged().get(stagedListElement.getSelectedIndex()));
						dm.getStaged().remove(stagedListElement.getSelectedIndex());
						updateListBox();
					}
				}).start();
			}
		});
		btnStage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						dm.getStaged().add(dm.getDecks().get(mainListElement.getSelectedIndex()));
						dm.getDecks().remove(mainListElement.getSelectedIndex());
						updateListBox();
					}
				}).start();
			}
		});
		GroupLayout gl_deckPanel = new GroupLayout(deckPanel);
		gl_deckPanel.setHorizontalGroup(
			gl_deckPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
		);
		gl_deckPanel.setVerticalGroup(
			gl_deckPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
		);
		deckPanel.setLayout(gl_deckPanel);
		ProcessPanel.setLayout(gl_ProcessActions);

		collectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						String folderStr = folder.getText();
						if (!folderStr.equals("")) {
							loadDecksFromTappedOutFolder(folderStr);
							updateListBox();
						}
					}
				}).start();
			}
		});
		frame.getContentPane().add(mainPanel);
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.PREFERRED_SIZE, 614, Short.MAX_VALUE));
		gl_mainPanel.setVerticalGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_mainPanel.createSequentialGroup()
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE).addContainerGap()));
		
		JScrollPane scrollPane_1 = new JScrollPane((Component) null);
		scrollPane_1.setPreferredSize(new Dimension(250, 40));
		
		Button button_1 = new Button("Process");
		mainPanel.setLayout(gl_mainPanel);

		JMenuBar menuBar = new JMenuBar();
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);

		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Add Decks from input dir");

		mntmNewMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						loadFromImportFolder();
						updateListBox();
					}
				}).start();
			}
		});
		menu.add(mntmNewMenuItem);

		JLabel lblTest = new JLabel("");
		menuBar.add(lblTest);
		frame.setPreferredSize(new Dimension(620, 400));
		frame.setResizable(false);
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void updateListBox() {
		deckList.clear();
		for (CsvConverterDeck deck : dm.getDecks()) {
			deckList.addElement(deck.getDeckMetadata().getDeckName());
		}
		stagedList.clear();
		for (CsvConverterDeck deck : dm.getStaged()) {
			stagedList.addElement(deck.getDeckMetadata().getDeckName());
		}
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
						TappedOutUtils.searchForDefaults(url, deck);
						String autoName = deck.getDeckMetadata().getDeckName();
						String autoCommander = deck.getCommanderStr();
						if (!autoName.equals("") && !autoName.equals("auto") && name.equals("auto")) {
							name = autoName;
						}
						if (!autoCommander.equals("") && !autoCommander.equals("auto") && commander.equals("auto")) {
							commander = autoCommander;
						}
						dm.addDeck(url, commander, name);
						updateListBox();

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

	public void loadDecksFromTappedOutFolder(String folderName) {
		ArrayList<String> deckUrls = TappedOutUtils.searchDeckCollection(folderName);
		for (String deckUrl : deckUrls) {
			deckUrl = "http://tappedout.net" + deckUrl;
			dm.addDeck(deckUrl, "auto", "auto");
			updateListBox();
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
				TappedOutUtils.searchForDefaults(url, deck);
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
					response = TappedOutUtils.getHTML(url);
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
}
