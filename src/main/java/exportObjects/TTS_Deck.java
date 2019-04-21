package exportObjects;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TTS_Deck {
    private String Name;
    private String Nickname;
    private String Description;
    private boolean Grid;
    private boolean Locked;
    private boolean SidewaysCard;
    private String GUID;
    private ColorDiffuseObj ColorDiffuse;
    private List<Integer> DeckIds = new ArrayList<Integer>();  // list of all card ids in deck
    @SerializedName("ContainedObjects")
    private List<TTS_Card> Cards = new ArrayList<TTS_Card>();
    private TransformObj Transform;
    /* list of pages of deck, including front and back image sources. card id is the custom deck num + 1
     * e.g card 15 on page 1 has id 115
     * is 1 indexed, not zero. Make sure to register type addaptor when using gson
     */
    private Pages CustomDeck;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isGrid() {
        return Grid;
    }

    public void setGrid(boolean grid) {
        Grid = grid;
    }

    public boolean isLocked() {
        return Locked;
    }

    public void setLocked(boolean locked) {
        Locked = locked;
    }

    public boolean isSidewaysCard() {
        return SidewaysCard;
    }

    public void setSidewaysCard(boolean sidewaysCard) {
        SidewaysCard = sidewaysCard;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public ColorDiffuseObj getColorDiffuse() {
        return ColorDiffuse;
    }

    public void setColorDiffuse(ColorDiffuseObj colorDiffuse) {
        ColorDiffuse = colorDiffuse;
    }

    public List<Integer> getDeckIds() {
        return DeckIds;
    }

    public void setDeckIds(List<Integer> deckIds) {
        DeckIds = deckIds;
    }

    public List<TTS_Card> getCards() {
        return Cards;
    }

    public void setCards(List<TTS_Card> cards) {
        Cards = cards;
    }

    public TransformObj getTransform() {
        return Transform;
    }

    public void setTransform(TransformObj transform) {
        Transform = transform;
    }

    public Pages getCustomDeck() {
        return CustomDeck;
    }

    public void setCustomDeck(Pages customDeck) {
        CustomDeck = customDeck;
    }
}
