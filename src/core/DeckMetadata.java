package core;

public class DeckMetadata {
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeckName() {
		return deckName;
	}
	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}
	public String getCardBackUrl() {
		return cardBackUrl;
	}
	public void setCardBackUrl(String cardBackUrl) {
		this.cardBackUrl = cardBackUrl;
	}
	public String getHiddenCardUrl() {
		return hiddenCardUrl;
	}
	public void setHiddenCardUrl(String hiddenCardUrl) {
		this.hiddenCardUrl = hiddenCardUrl;
	}
	public String getCoolify() {
		return coolify;
	}
	public void setCoolify(String coolify) {
		this.coolify = coolify;
	}
	public String getArtify() {
		return artify;
	}
	public void setArtify(String artify) {
		this.artify = artify;
	}
	public String getCompression() {
		return compression;
	}
	public void setCompression(String compression) {
		this.compression = compression;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFriendlyDeckName(){
		String returnStr = this.deckName.replaceAll("<", "").replaceAll(">", "").replaceAll("\\[", "").replaceAll("\\]", "")
				.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll(":", " ").replaceAll("\\*", "").replaceAll(" +", " ");
		return returnStr;
	}
private String id = "dummy";
private String deckName;
private String cardBackUrl;
private String hiddenCardUrl;

private String coolify;
private String artify;
private String compression;
private String name;
public String toString(){
	StringBuilder sb = new StringBuilder();
	sb.append(id+"\n");
	sb.append(deckName+"\n");
	sb.append(cardBackUrl+"\n");
	sb.append(hiddenCardUrl+"\n");
	sb.append(coolify+"\n");
	sb.append(artify+"\n");
	sb.append(compression+"\n");
	return sb.toString();
}
}
