package images;

import core.Util;
import core.Utils.SelectBuilder;
import importObjects.Card;
import importObjects.CardParams;
import importObjects.DoubleFacedCard;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.MessageFormat;

public class ImageCache
{
    private final static String DB_DIRECTORY_PATH = "C:/sqlite/db/";
    private final static String DB_FILE_NAME = "DeckImporter.db";
    private final static String DB_FILE_PATH = DB_DIRECTORY_PATH + DB_FILE_NAME;

    private final static String IMAGE_SOURCE_TN = "IMAGE_SOURCE";
    private final static String FILE_TN = "FILE";
    private final static String CARD_TN = "CARD";

    private final static String CARD_ID = "CARD_ID";
    private final static String CARD_NAME = "CARD_NAME";
    private final static String CARD_SET_ACRONYM = "CARD_SET_ACRONYM";
    private final static String CARD_IS_DOUBLE_SIDED = "CARD_IS_DOUBLE_SIDED";
    private final static String CARD_FRONT_IMAGE_FILE = "CARD_FRONT_IMAGE_FILE";
    private final static String CARD_BACK_IMAGE_FILE = "CARD_BACK_IMAGE_FILE";

    private final static String IMAGE_SOURCE_ID = "IMAGE_SOURCE_ID";
    private final static String IMAGE_SOURCE_NAME = "IMAGE_SOURCE_NAME";

    private final static String FILE_ID = "FILE_ID";
    private final static String FILE_IMAGE_SOURCE = "FILE_IMAGE_SOURCE";
    private final static String FILE_LOCAL_PATH = "FILE_LOCAL_PATH";

    private final static String FRONT_PREFIX = "FRONT_";
    private final static String BACK_PREFIX = "BACK_";

    private static ImageCache instance = null;


    private final Connection connection;;
    private final PreparedStatement queryCardWithoutSet;
    private final PreparedStatement queryCardWithSet;
    private final PreparedStatement queryImageSource;
    private final PreparedStatement queryFile;
    private final PreparedStatement insertImageSource;
    private final PreparedStatement insertFile;
    private final PreparedStatement insertCard;
    private final PreparedStatement insertDoubleFacedCard;

    public static ImageCache getInstance() throws IOException, SQLException
    {
        if(instance == null)
            instance = new ImageCache();

        return instance;
    }

    public ImageCache resetCache() throws IOException, SQLException
    {
        connection.close();

        File db_file = new File(DB_FILE_PATH);

        if(db_file.exists()) db_file.delete();

        instance = null;

        return getInstance();
    }

    private ImageCache() throws IOException, SQLException
    {
        File directory = new File(DB_DIRECTORY_PATH);
        if (!directory.exists()) directory.mkdirs();

        String url = "jdbc:sqlite:" + DB_FILE_PATH;

        connection = DriverManager.getConnection(url);

        if(connection == null)
            throw new IOException("Unable to access/create SQLite file");

        Statement createTable = connection.createStatement();

        createTable.execute(MessageFormat.format(
    "CREATE TABLE IF NOT EXISTS {0} (\n" +
            " {1} integer PRIMARY KEY,\n" +
            " {2} text UNIQUE NOT NULL\n" +
            ");",
            IMAGE_SOURCE_TN, IMAGE_SOURCE_ID, IMAGE_SOURCE_NAME));

        createTable.execute(MessageFormat.format(
    "CREATE TABLE IF NOT EXISTS {0} (\n" +
            " {1} integer PRIMARY KEY,\n" +
            " {2} integer NOT NULL,\n" +
            " {3} text NOT NULL,\n" +
            " FOREIGN KEY({2}) REFERENCES {4}({5})\n" +
            ");",
            FILE_TN, FILE_ID, FILE_IMAGE_SOURCE, FILE_LOCAL_PATH, IMAGE_SOURCE_TN, IMAGE_SOURCE_ID));

        createTable.execute(MessageFormat.format(
    "CREATE TABLE IF NOT EXISTS {0} (\n" +
            " {1} integer PRIMARY KEY,\n" +
            " {2} text NOT NULL,\n" +
            " {3} text NOT NULL,\n" +
            " {4} integer NOT NULL,\n" +
            " {5} integer NOT NULL,\n" +
            " {6} integer,\n" +
            " FOREIGN KEY({5}) REFERENCES {7}({8}),\n" +
            " FOREIGN KEY({6}) REFERENCES {7}({8}) \n" +
            ");",
            CARD_TN, CARD_ID, CARD_NAME, CARD_SET_ACRONYM, CARD_IS_DOUBLE_SIDED, CARD_FRONT_IMAGE_FILE, CARD_BACK_IMAGE_FILE, FILE_TN, FILE_ID));
        SelectBuilder withoutSetSelectBuilder = new SelectBuilder();
        String queryCardSelectStmt = withoutSetSelectBuilder
                .addColumn(CARD_ID)
                .addColumn(CARD_NAME)
                .addColumn(CARD_SET_ACRONYM)
                .addColumn(CARD_IS_DOUBLE_SIDED)
                .addColumn(CARD_FRONT_IMAGE_FILE)
                .addColumn(CARD_BACK_IMAGE_FILE)
                .addColumnWithTableNameAndAlias(FILE_TN + "1", FILE_ID,FRONT_PREFIX + FILE_ID)
                .addColumnWithTableNameAndAlias(FILE_TN + "1", FILE_IMAGE_SOURCE , FRONT_PREFIX + FILE_IMAGE_SOURCE)
                .addColumnWithTableNameAndAlias(FILE_TN + "1", FILE_LOCAL_PATH, FRONT_PREFIX + FILE_LOCAL_PATH)
                .addColumnWithTableNameAndAlias(FILE_TN + "2", FILE_ID, BACK_PREFIX + FILE_ID)
                .addColumnWithTableNameAndAlias(FILE_TN + "2", FILE_IMAGE_SOURCE, BACK_PREFIX + FILE_IMAGE_SOURCE)
                .addColumnWithTableNameAndAlias(FILE_TN + "2", FILE_LOCAL_PATH, BACK_PREFIX + FILE_LOCAL_PATH)
                .addColumn(IMAGE_SOURCE_NAME)
                .addColumn(IMAGE_SOURCE_ID).toString();
        String queryCardWithoutSetStr = MessageFormat.format(
                queryCardSelectStmt +
                        " FROM {0}" +
                        " INNER JOIN {1} as {6} on {6}.{2} = {3}.{4}" +
                        " LEFT JOIN {1} as {7} on {7}.{2} = {3}.{5}" +
                        " INNER JOIN {9} on {9}.{10} = {6}.{11}" +
                        " WHERE {8} = ? AND {12} = ?",
    CARD_TN, FILE_TN, FILE_ID, CARD_TN, CARD_FRONT_IMAGE_FILE, CARD_BACK_IMAGE_FILE, FILE_TN + "1", FILE_TN + "2", CARD_NAME, IMAGE_SOURCE_TN, IMAGE_SOURCE_ID, FILE_IMAGE_SOURCE, IMAGE_SOURCE_NAME);

        String queryCardWithSetStr = queryCardWithoutSetStr + " AND " + CARD_SET_ACRONYM + " = ?";

        String queryImageSourceStr = MessageFormat.format(
    "SELECT *" +
            " FROM {0}" +
            " WHERE {1} = ?",
            IMAGE_SOURCE_TN, IMAGE_SOURCE_NAME
        );

        String queryFileStr = MessageFormat.format(
    "SELECT *" +
            " FROM {0}" +
            " WHERE {1} = ? AND {2} = ?",
            FILE_TN, FILE_IMAGE_SOURCE, FILE_LOCAL_PATH
        );

        String insertImageSourceStr = MessageFormat.format(
    "INSERT OR IGNORE INTO {0}({1})\n" +
            " VALUES(?)",
            IMAGE_SOURCE_TN , IMAGE_SOURCE_NAME);

        String insertFileStr = MessageFormat.format(
    "INSERT INTO {0}({1},{2})\n" +
            " VALUES(?, ?)",
            FILE_TN, FILE_IMAGE_SOURCE, FILE_LOCAL_PATH
        );

        String insertCardStr = MessageFormat.format(
    "INSERT INTO {0}({1}, {2}, {3}, {4})\n" +
            " VALUES(?, ?, 0, ?)",
            CARD_TN, CARD_NAME, CARD_SET_ACRONYM, CARD_IS_DOUBLE_SIDED, CARD_FRONT_IMAGE_FILE
        );

        String insertDoubleFacedCardStr = MessageFormat.format(
    "INSERT INTO {0}({1}, {2}, {3}, {4}, {5})\n" +
            " VALUES(?, ?, 1, ?, ?)",
            CARD_TN, CARD_NAME, CARD_SET_ACRONYM, CARD_IS_DOUBLE_SIDED, CARD_FRONT_IMAGE_FILE, CARD_BACK_IMAGE_FILE
        );

        this.queryCardWithoutSet =      connection.prepareStatement(queryCardWithoutSetStr);
        this.queryCardWithSet =         connection.prepareStatement(queryCardWithSetStr);
        this.queryImageSource =         connection.prepareStatement(queryImageSourceStr);
        this.queryFile =                connection.prepareStatement(queryFileStr);
        this.insertImageSource =        connection.prepareStatement(insertImageSourceStr);
        this.insertFile =               connection.prepareStatement(insertFileStr);
        this.insertCard =               connection.prepareStatement(insertCardStr);
        this.insertDoubleFacedCard =    connection.prepareStatement(insertDoubleFacedCardStr);
    }

    public void saveCardtoCache(Card card, String sourceName) throws SQLException
    {
        PreparedStatement insertSource = insertImageSource;
        insertSource.setString(1, sourceName);
        insertSource.execute();

        PreparedStatement getSource = queryImageSource;
        getSource.setString(1, sourceName);
        ResultSet imageSource = getSource.executeQuery();

        if(imageSource.next())
        {
            int sourceId = imageSource.getInt(IMAGE_SOURCE_ID);

            int frontCardImage = insertFile(card.getCardImage(), sourceId);

            if(card instanceof DoubleFacedCard) //is double faced
            {
                DoubleFacedCard doubleFacedCard = (DoubleFacedCard)card;
                int backCardImage = insertFile(doubleFacedCard.getBackCardImage(), sourceId);

                PreparedStatement insertDoubleCard = insertDoubleFacedCard;
                insertDoubleCard.setString(1, doubleFacedCard.getCardName());
                insertDoubleCard.setString(2, doubleFacedCard.getSet());
                insertDoubleCard.setInt(3, frontCardImage);
                insertDoubleCard.setInt(4, backCardImage);
                insertDoubleCard.execute();
            }
            else
            {
                PreparedStatement insertACard = insertCard;
                insertACard.setString(1, card.getCardName());
                insertACard.setString(2, card.getSet());
                insertACard.setInt(3, frontCardImage);
                insertACard.execute();
            }
        }
        else
            throw new SQLException("Could not locate image source after inserting it");
    }

    private int insertFile(File file, int sourceId) throws SQLException
    {
        String path = file.getAbsolutePath();

        PreparedStatement insertLocalFile = insertFile;
        insertLocalFile.setInt(1, sourceId);
        insertLocalFile.setString(2, path);
        insertLocalFile.execute();

        PreparedStatement queryLocalFile = queryFile;
        queryLocalFile.setInt(1, sourceId);
        queryLocalFile.setString(2, path);
        ResultSet localFile = queryLocalFile.executeQuery();

        if(localFile.next())
        {
            return localFile.getInt(FILE_ID);
        }
        else
            throw new SQLException("Could not locate file record after inserting it");
    }

    public Card getMatchingCards(CardParams params, String sourceName) throws SQLException
    {
        PreparedStatement query;

        if(Util.NullOrWhitespace(params.set))
        {
            query = queryCardWithoutSet;
            query.setString(1, params.cardName);
            query.setString(2, sourceName);
        }
        else
        {
            query = queryCardWithSet;
            query.setString(1, params.cardName);
            query.setString(2, sourceName);
            query.setString(3, params.set);
        }

        ResultSet results = query.executeQuery();

        Card card = null;
        if(results.next())
        {
            String name = results.getString(CARD_NAME);
            File frontImage = new File(results.getString(FRONT_PREFIX + FILE_ID));

            if(!results.getBoolean(CARD_IS_DOUBLE_SIDED))
            {
                //TODO finish after expanding on card creation
            }
            else
            {
                File backImage = new File(results.getString(BACK_PREFIX+ FILE_ID));
//                new DoubleFacedCard();
            }
        }

        return card;
    }
}
