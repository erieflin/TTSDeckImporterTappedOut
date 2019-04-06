package importObjects.Scryfall;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.util.UUID;

public class ScryfallCard {
    @SerializedName("arena_id")
    private int arenaId;
    private int id;
    private String lang;
    @SerializedName("mtgo_id")
    private int mtgoId;
    @SerializedName("mtgo_foil_id")
    private int mtgoFoilId;
    @SerializedName("multiverse_ids")
    private int[] multiverseIds;
    @SerializedName("tcgplayer_id")
    private int tcgplayerId;
    private String object;
    @SerializedName("oracle_id")
    private UUID oracleId;
    @SerializedName("prints_search_uri")
    private URI printsSearchUri;
    @SerializedName("rulings_uri")
    private URI rulingsUri;
    private URI uri;

    //gameplay
    @SerializedName("all_parts")
    private List<CardObject> allParts;
    @SerializedName("card_faces")
    private List<CardFaces> cardFaces;
    private double cmc;
    private Colors colors;
    @SerializedName("color_identity")
    private Colors colorIdentity;
    @SerializedName("color_indicator")
    private Colors colorIndicator;
    @SerializedName("edhrec_rank")
    private int edhrecRank;
    private boolean foil;
    @SerializedName("hand_modifier")
    private String handModifer;
    private String layout;
    private Lealities lealities;
}
