package exportObjects;

public class TTS_Card {
    private String Name;
    private String Nickname;
    private int CardId; //deck page of card+ card number out of 69 on that page
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
}
