package exportObjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TTS_SavedObject {
    // unpopulated, default all these to blank
    private String SaveName = "";
    private String GameMode;
    private String Date;
    private String Table;
    private String Sky;
    private String Note;
    private String Rules;
    private String PlayerTurn;
    @SerializedName("ObjectStates")
    private List<TTS_Deck> Decks = new ArrayList<TTS_Deck>();

}
