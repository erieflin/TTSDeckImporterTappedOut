package exportObjects;

import core.JsonUtils;

import java.net.URI;

public class TTS_Card {

    public TTS_Card(){
        setName("Card");
        setTransform(JsonUtils.NewDeckPosObject(1, 1, 1, false, 1.0));
    }
    public TTS_Card(String nickName, int cardId){
        this();
        setNickname(""+nickName);
        setCardId(cardId);
    }
    private String Name;
    private String Nickname;
    private int CardId; //deck page of card+ card number out of 69 on that page
    private transient int pageId;
    private transient String cardName;
    private transient int qty;
    private TransformObj Transform;

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

    public int getCardId() {
        return CardId;
    }

    public void setCardId(int cardId) {
        CardId = cardId;
    }

    public TransformObj getTransform() {
        return Transform;
    }

    public void setTransform(TransformObj transform) {
        Transform = transform;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
