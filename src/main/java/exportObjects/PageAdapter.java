package exportObjects;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class PageAdapter extends TypeAdapter<ImagePage> {

    @Override
    public void write(JsonWriter out, ImagePage value) throws IOException {
        out.beginObject();
        out.name("FaceURL").value(value.getFaceURL());
        out.name("BackUrl").value(value.getBackUrl());
        out.endObject();
    }


    @Override
    public ImagePage read(JsonReader in) throws IOException {
        ImagePage page = new ImagePage();
        in.beginObject();
        switch (in.nextName()) {
            case "FaceURL":
                page.setFaceURL(in.nextString());
                break;
            case "BackURL":
                page.setBackUrl(in.nextString());
        }
        return page;
    }
}

