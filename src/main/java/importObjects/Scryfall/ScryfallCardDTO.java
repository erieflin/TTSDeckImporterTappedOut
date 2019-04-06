package importObjects.Scryfall;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
@SuppressWarnings("unused")
public class ScryfallCardDTO {
    @SerializedName("arena_id")
    private int arenaId;
    private UUID id;
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
    private List<RelatedCardObjectDTO> allParts;
    @SerializedName("card_faces")
    private List<CardFaceDTO> cardFaces;
    private double cmc;
    private List<String> colors;
    @SerializedName("color_identity")
    private List<String> colorIdentity;
    @SerializedName("color_indicator")
    private List<String> colorIndicator;
    @SerializedName("edhrec_rank")
    private int edhrecRank;
    private boolean foil;
    @SerializedName("hand_modifier")
    private String handModifer;
    private String layout;
    private HashMap<String,String> legalities;
    @SerializedName("life_modifier")
    private String lifeModifier;
    private String loyalty;
    @SerializedName("mana_cost")
    private String manaCost;
    private String name;
    @SerializedName("nonfoil")
    private boolean nonFoil;
    @SerializedName("oracle_text")
    private String oracleText;
    private boolean oversized;
    private String power;
    private boolean reserved;
    private String toughness;
    @SerializedName("type_line")
    private String typeLine;

    // print fields
    private String artist;
    @SerializedName("border_color")
    private String borderColor;
    @SerializedName("collector_number")
    private String collectorNumber;
    private boolean digital;
    @SerializedName("flavor_text")
    private String flavorText;
    @SerializedName("frame_effect")
    private String frameEffect;
    private String frame;
    @SerializedName("full_art")
    private boolean fullArt;
    private List<String> games;
    @SerializedName("highres_image")
    private boolean highresImage;
    @SerializedName("illustration_id")
    private UUID illustrationId;
    @SerializedName("image_uris")
    private HashMap<String,String> imageUris;
    private HashMap<String,String> prices;
    @SerializedName("printed_name")
    private String printedName;
    @SerializedName("printed_text")
    private String printedText;
    @SerializedName("printed_type_line")
    private String printedTypeLine;
    private boolean promo;
    @SerializedName("purchase_uris")
    private HashMap<String, String> purchaseUris;
    private String rarity;
    @SerializedName("related_uris")
    private HashMap<String, String> relatedUris;
    @SerializedName("release_at")
    private Date releasedAt;
    private boolean reprint;
    @SerializedName("scryfall_set_uri")
    private URI scryfallSetUri;
    @SerializedName("set_name")
    private String setName;
    @SerializedName("set_search_uri")
    private URI setSearchUri;
    @SerializedName("set_uri")
    private URI setUri;
    private String set;
    @SerializedName("story_spotlight")
    private boolean storySpotlight;
    private String watermark;

    public int getArenaId() {
        return arenaId;
    }

    public void setArenaId(int arenaId) {
        this.arenaId = arenaId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getMtgoId() {
        return mtgoId;
    }

    public void setMtgoId(int mtgoId) {
        this.mtgoId = mtgoId;
    }

    public int getMtgoFoilId() {
        return mtgoFoilId;
    }

    public void setMtgoFoilId(int mtgoFoilId) {
        this.mtgoFoilId = mtgoFoilId;
    }

    public int[] getMultiverseIds() {
        return multiverseIds;
    }

    public void setMultiverseIds(int[] multiverseIds) {
        this.multiverseIds = multiverseIds;
    }

    public int getTcgplayerId() {
        return tcgplayerId;
    }

    public void setTcgplayerId(int tcgplayerId) {
        this.tcgplayerId = tcgplayerId;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public UUID getOracleId() {
        return oracleId;
    }

    public void setOracleId(UUID oracleId) {
        this.oracleId = oracleId;
    }

    public URI getPrintsSearchUri() {
        return printsSearchUri;
    }

    public void setPrintsSearchUri(URI printsSearchUri) {
        this.printsSearchUri = printsSearchUri;
    }

    public URI getRulingsUri() {
        return rulingsUri;
    }

    public void setRulingsUri(URI rulingsUri) {
        this.rulingsUri = rulingsUri;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public List<RelatedCardObjectDTO> getAllParts() {
        return allParts;
    }

    public void setAllParts(List<RelatedCardObjectDTO> allParts) {
        this.allParts = allParts;
    }

    public List<CardFaceDTO> getCardFaces() {
        return cardFaces;
    }

    public void setCardFaces(List<CardFaceDTO> cardFaces) {
        this.cardFaces = cardFaces;
    }

    public double getCmc() {
        return cmc;
    }

    public void setCmc(double cmc) {
        this.cmc = cmc;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getColorIdentity() {
        return colorIdentity;
    }

    public void setColorIdentity(List<String> colorIdentity) {
        this.colorIdentity = colorIdentity;
    }

    public List<String> getColorIndicator() {
        return colorIndicator;
    }

    public void setColorIndicator(List<String> colorIndicator) {
        this.colorIndicator = colorIndicator;
    }

    public int getEdhrecRank() {
        return edhrecRank;
    }

    public void setEdhrecRank(int edhrecRank) {
        this.edhrecRank = edhrecRank;
    }

    public boolean isFoil() {
        return foil;
    }

    public void setFoil(boolean foil) {
        this.foil = foil;
    }

    public String getHandModifer() {
        return handModifer;
    }

    public void setHandModifer(String handModifer) {
        this.handModifer = handModifer;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public HashMap<String, String> getLegalities() {
        return legalities;
    }

    public void setLegalities(HashMap<String, String> legalities) {
        this.legalities = legalities;
    }

    public String getLifeModifier() {
        return lifeModifier;
    }

    public void setLifeModifier(String lifeModifier) {
        this.lifeModifier = lifeModifier;
    }

    public String getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(String loyalty) {
        this.loyalty = loyalty;
    }

    public String getManaCost() {
        return manaCost;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNonFoil() {
        return nonFoil;
    }

    public void setNonFoil(boolean nonFoil) {
        this.nonFoil = nonFoil;
    }

    public String getOracleText() {
        return oracleText;
    }

    public void setOracleText(String oracleText) {
        this.oracleText = oracleText;
    }

    public boolean isOversized() {
        return oversized;
    }

    public void setOversized(boolean oversized) {
        this.oversized = oversized;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String getToughness() {
        return toughness;
    }

    public void setToughness(String toughness) {
        this.toughness = toughness;
    }

    public String getTypeLine() {
        return typeLine;
    }

    public void setTypeLine(String typeLine) {
        this.typeLine = typeLine;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getCollectorNumber() {
        return collectorNumber;
    }

    public void setCollectorNumber(String collectorNumber) {
        this.collectorNumber = collectorNumber;
    }

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }

    public String getFrameEffect() {
        return frameEffect;
    }

    public void setFrameEffect(String frameEffect) {
        this.frameEffect = frameEffect;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public boolean isFullArt() {
        return fullArt;
    }

    public void setFullArt(boolean fullArt) {
        this.fullArt = fullArt;
    }

    public List<String> getGames() {
        return games;
    }

    public void setGames(List<String> games) {
        this.games = games;
    }

    public boolean isHighresImage() {
        return highresImage;
    }

    public void setHighresImage(boolean highresImage) {
        this.highresImage = highresImage;
    }

    public UUID getIllustrationId() {
        return illustrationId;
    }

    public void setIllustrationId(UUID illustrationId) {
        this.illustrationId = illustrationId;
    }

    public HashMap<String, String> getImageUris() {
        return imageUris;
    }

    public void setImageUris(HashMap<String, String> imageUris) {
        this.imageUris = imageUris;
    }

    public HashMap<String, String> getPrices() {
        return prices;
    }

    public void setPrices(HashMap<String, String> prices) {
        this.prices = prices;
    }

    public String getPrintedName() {
        return printedName;
    }

    public void setPrintedName(String printedName) {
        this.printedName = printedName;
    }

    public String getPrintedText() {
        return printedText;
    }

    public void setPrintedText(String printedText) {
        this.printedText = printedText;
    }

    public String getPrintedTypeLine() {
        return printedTypeLine;
    }

    public void setPrintedTypeLine(String printedTypeLine) {
        this.printedTypeLine = printedTypeLine;
    }

    public boolean isPromo() {
        return promo;
    }

    public void setPromo(boolean promo) {
        this.promo = promo;
    }

    public HashMap<String, String> getPurchaseUris() {
        return purchaseUris;
    }

    public void setPurchaseUris(HashMap<String, String> purchaseUris) {
        this.purchaseUris = purchaseUris;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public HashMap<String, String> getRelatedUris() {
        return relatedUris;
    }

    public void setRelatedUris(HashMap<String, String> relatedUris) {
        this.relatedUris = relatedUris;
    }

    public Date getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(Date releasedAt) {
        this.releasedAt = releasedAt;
    }

    public boolean isReprint() {
        return reprint;
    }

    public void setReprint(boolean reprint) {
        this.reprint = reprint;
    }

    public URI getScryfallSetUri() {
        return scryfallSetUri;
    }

    public void setScryfallSetUri(URI scryfallSetUri) {
        this.scryfallSetUri = scryfallSetUri;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public URI getSetSearchUri() {
        return setSearchUri;
    }

    public void setSetSearchUri(URI setSearchUri) {
        this.setSearchUri = setSearchUri;
    }

    public URI getSetUri() {
        return setUri;
    }

    public void setSetUri(URI setUri) {
        this.setUri = setUri;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public boolean isStorySpotlight() {
        return storySpotlight;
    }

    public void setStorySpotlight(boolean storySpotlight) {
        this.storySpotlight = storySpotlight;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }
}
