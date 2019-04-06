package importObjects.Scryfall;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
@SuppressWarnings("unused")
public class CardFaceDTO {
    private String artist;
    @SerializedName("color_indicator")
    private List<String> colorIndicator;
    private List<String> colors;
    @SerializedName("flavor_text")
    private String flavorText;
    @SerializedName("illustration_id")
    private UUID illustrationId;
    @SerializedName("image_uris")
    private HashMap<String,String> imageUris;
    private String loyalty;
    @SerializedName("mana_cost")
    private String manaCost;
    private String name;
    private String object;
    @SerializedName("oracle_text")
    private String oracleText;
    private String power;
    @SerializedName("printed_name")
    private String printedName;
    @SerializedName("printed_text")
    private String printedText;
    @SerializedName("printed_type_line")
    private String printedTypeLine;
    private String toughness;
    @SerializedName("type_line")
    private String typeLine;
    private String watermark;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<String> getColorIndicator() {
        return colorIndicator;
    }

    public void setColorIndicator(List<String> colorIndicator) {
        this.colorIndicator = colorIndicator;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
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

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getOracleText() {
        return oracleText;
    }

    public void setOracleText(String oracleText) {
        this.oracleText = oracleText;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
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

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }
}
