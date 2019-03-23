package core;

import images.ImageCache;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseTesting
{
    public static void main(String[] args) throws IOException, SQLException
    {
        ImageCache imageCache = ImageCache.getInstance();
    }
}
