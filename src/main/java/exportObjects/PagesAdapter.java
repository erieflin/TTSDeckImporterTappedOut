package exportObjects;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.*;
import java.io.IOException;

public class PagesAdapter extends TypeAdapter<Pages> {

        @Override
        public Pages read(JsonReader in) throws IOException {
            Pages pages = new Pages();
            in.beginObject();
            ImagePage currentPage;
            String currentName = "1";
            while (in.hasNext()) {
                JsonToken nextToken = in.peek();
                if (JsonToken.BEGIN_OBJECT.equals(nextToken)) {
                    TypeAdapter<ImagePage> pageAdapter = new Gson().getAdapter(ImagePage.class);
                    currentPage = pageAdapter.read(in);
                    currentPage.setPageNumber(Integer.parseInt(currentName));
                    pages.getPageList().add(currentPage);
                } else if (JsonToken.NAME.equals(nextToken)) {
                    currentName = in.nextName();
                }
            }
            in.endObject();
            return pages;
        }

        @Override
        public void write(JsonWriter out, Pages pages) throws IOException {
            out.beginObject();

            for(ImagePage page: pages.getPageList()) {
                out.name("" + page.getPageNumber());
                TypeAdapter<ImagePage> pageAdapter = new Gson().getAdapter(ImagePage.class);
                pageAdapter.write(out, page);
            }
            out.endObject();
        }



}


