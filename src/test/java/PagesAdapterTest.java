import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exportObjects.ImagePage;
import exportObjects.Pages;
import exportObjects.PagesAdapter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PagesAdapterTest {

    private static final String gsonString = "{\"CustomDeck\":{" +
            "\"1\":{\"FaceURL\":\"front\",\"BackUrl\":\"back\"}," +
            "\"2\":{\"FaceURL\":\"front2\",\"BackUrl\":\"back2\"}" +
            "}}";

    static final GsonBuilder gsonBuilder = new GsonBuilder();
    static Gson gson;
    @BeforeClass
    public static void setUp(){
        gsonBuilder.registerTypeAdapter(Pages.class, new PagesAdapter());
        gson = gsonBuilder.create();
    }
    @Test
    public void testReadPages() {
        TestTemplate template = gson.fromJson(gsonString,TestTemplate.class);
        assertTrue(template != null);
        assertEquals(1, template.CustomDeck.getPageList().get(0).getPageNumber());
        assertEquals(2, template.CustomDeck.getPageList().get(1).getPageNumber());
        assertEquals("front", template.CustomDeck.getPageList().get(0).getFaceUrl());
        assertEquals("front2", template.CustomDeck.getPageList().get(1).getFaceUrl());
    }

    @Test
    public void testWritePages(){
        TestTemplate template= new TestTemplate();
        template.CustomDeck = new Pages();


        ImagePage page1 = new ImagePage();
        page1.setPageNumber(1);
        page1.setBackUrl("back");
        page1.setFaceUrl("front");
        template.CustomDeck.getPageList().add(page1);

        ImagePage page2 = new ImagePage();
        page2.setPageNumber(2);
        page2.setBackUrl("back2");
        page2.setFaceUrl("front2");
        template.CustomDeck.getPageList().add(page2);

        String json = gson.toJson(template);
        assertTrue(json.equalsIgnoreCase(gsonString));
    }

    class TestTemplate{
        Pages CustomDeck;
    }

}