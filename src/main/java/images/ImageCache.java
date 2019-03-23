package images;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class ImageCache
{
    private final String DB_DIRECTORY_PATH = "C:/sqlite/db/";
    private final String DB_FILE_NAME = "DeckImporter.db";

    private final String IMAGE_SOURCE_TABLE_NAME = "IMAGE_SOURCE";
    private final String FILE_TABLE_NAME = "file";
    private final String CARD_TABLE_NAME = "card";

    private static ImageCache instance = null;

    private final Connection connection;
    private final ImageSourceTable imageSourceTable;
    private final FileTable fileTable;
    private final CardTable cardTable;

    public static ImageCache getInstance() throws IOException, SQLException
    {
        if(instance == null)
            instance = new ImageCache();

        return instance;
    }

    public static ImageCache resetCache()
    {
        //TODO delete cache if it exists, then return new instance

        return null;
    }

    private ImageCache() throws IOException, SQLException
    {
        File directory = new File(DB_DIRECTORY_PATH);
        if (!directory.exists()) directory.mkdirs();

        String url = "jdbc:sqlite:" + DB_DIRECTORY_PATH + DB_FILE_NAME;

        connection = DriverManager.getConnection(url);

        if(connection == null)
            throw new IOException("Unable to access/create SQLite file");

        imageSourceTable = new ImageSourceTable(connection);
        fileTable = new FileTable(connection);
        cardTable = new CardTable(connection);
    }

    private class ImageSourceTable
    {
        private ImageSourceTable(Connection connection) throws SQLException
        {
            Statement createTable = connection.createStatement();
            createTable.execute(
            "CREATE TABLE IF NOT EXISTS " + IMAGE_SOURCE_TABLE_NAME + " (\n" +
                " image_source_id integer PRIMARY KEY,\n" +
                " image_source_name text NOT NULL\n" +
                ");");
        }
    }

    private class FileTable
    {
        private FileTable(Connection connection) throws SQLException
        {
            Statement createTable = connection.createStatement();
            createTable.execute(
            "CREATE TABLE IF NOT EXISTS " + FILE_TABLE_NAME + "(\n" +
                " file_id integer PRIMARY KEY,\n" +
                " file_image_source integer NOT NULL,\n" +
                " file_local_path text NOT NULL,\n" +
                " FOREIGN KEY(file_image_source) REFERENCES " + IMAGE_SOURCE_TABLE_NAME + "(image_source_id)\n" +
                ");");
        }
    }

    private class CardTable
    {
        private CardTable(Connection connection) throws SQLException
        {
            Statement createTable = connection.createStatement();
            createTable.execute(
            "CREATE TABLE IF NOT EXISTS " + CARD_TABLE_NAME + "(\n" +
                " card_id integer PRIMARY KEY,\n" +
                " card_name text NOT NULL,\n" +
                " card_set_acronym text NOT NULL,\n" +
                " card_is_double_sided integer NOT NULL,\n" +
                " card_front_image_file integer NOT NULL,\n" +
                " card_back_image_file integer,\n" +
                " FOREIGN KEY(card_front_image_file) REFERENCES " + FILE_TABLE_NAME + "(file_id),\n" +
                " FOREIGN KEY(card_back_image_file) REFERENCES " + FILE_TABLE_NAME + "(file_id)\n" +
                ");");
        }
    }
}
