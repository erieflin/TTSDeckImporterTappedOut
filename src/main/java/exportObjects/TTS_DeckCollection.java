package exportObjects;

import java.util.ArrayList;
import java.util.List;

public class TTS_DeckCollection {
    private String SaveName="";
    private String GameMode = "";
    private String Date = "";
    private String Table = "";
    private String Sky = "";
    private String Note = "";
    private String Rules = "";
    private String PlayerTurn = "";
    private List<TTS_Deck> ObjectStates = new ArrayList<TTS_Deck>();

    public List<TTS_Deck> getObjectStates() {
        return ObjectStates;
    }

    public void setObjectStates(List<TTS_Deck> objectStates) {
        ObjectStates = objectStates;
    }
}
