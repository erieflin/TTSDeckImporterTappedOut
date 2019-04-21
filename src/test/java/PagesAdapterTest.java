import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exportObjects.ImagePage;
import exportObjects.Pages;
import exportObjects.PagesAdapter;
import org.junit.BeforeClass;
import org.junit.Test;

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
    }

    @Test
    public void testWritePages(){
        TestTemplate template= new TestTemplate();
        template.CustomDeck = new Pages();


        ImagePage page1 = new ImagePage();
        page1.setPageNumber(1);
        page1.setBackUrl("back");
        page1.setFaceURL("front");
        template.CustomDeck.getPageList().add(page1);

        ImagePage page2 = new ImagePage();
        page2.setPageNumber(2);
        page2.setBackUrl("back2");
        page2.setFaceURL("front2");
        template.CustomDeck.getPageList().add(page2);

        String json = gson.toJson(template);
        assertTrue(json!= null && json.length() > 0);
    }

    class TestTemplate{
        Pages CustomDeck;
    }

}