package images;

import core.Util;
import importObjects.Card;
import importObjects.CardParams;

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


    private static ImageCache instance = null;


    private final Connection connection;;
    private final PreparedStatement queryCardWithoutSet;
    private final PreparedStatement queryCardWithSet;

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
            " {2} text NOT NULL\n" +
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

        String queryWithoutSet = MessageFormat.format(
    "SELECT *" +
            " FROM {0}" +
            " INNER JOIN {1} as {6} on {6}.{2} = {3}.{4}" +
            " LEFT JOIN {1} as {7} on {7}.{2} = {3}.{5}" +
            " WHERE {8} = ?",
    CARD_TN, FILE_TN, FILE_ID, CARD_TN, CARD_FRONT_IMAGE_FILE, CARD_BACK_IMAGE_FILE, FILE_TN + "1", FILE_TN + "2", CARD_NAME);

        String queryWithSet = queryWithoutSet + " AND " + CARD_SET_ACRONYM + " = ?";

        this.queryCardWithoutSet =  connection.prepareStatement(queryWithoutSet);
        this.queryCardWithSet =     connection.prepareStatement(queryWithSet);
    }

    public Card getMatchingCards(CardParams params) throws SQLException
    {
        PreparedStatement query;

        if(Util.NullOrWhitespace(params.set))
            query = queryCardWithoutSet;
        else
            query = queryCardWithSet;

        ResultSet results = query.executeQuery();

        Card card = null;
        if(results.next())
        {
            String name = results.getString(CARD_NAME);
            File frontImage = new File(results.getString(FILE_TN + "1." + FILE_ID));

            if(!results.getBoolean(CARD_IS_DOUBLE_SIDED))
            {
                //TODO finish after expanding on card creation
            }
            else
            {
                File backImage = new File(results.getString(FILE_TN + "2." + FILE_ID));
//                new DoubleFacedCard();
            }
        }

        return card;
    }
}
