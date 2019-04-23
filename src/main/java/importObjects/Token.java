package importObjects;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class Token implements BaseCard
{
   private String cardName;
   private String style;
   private int quantity = 1;
   private File cardImage;
   private UUID tokenUUID = null;

   public Token(UUID id, String cardName, File cardImage){
       this(cardName,cardImage);
       this.tokenUUID = id;

   }

   public Token(String cardName, File cardImage){
       this.cardName = cardName;
       this.cardImage = cardImage;
   }

   public Token(String cardName, File cardImage, String style){
       this(cardName,cardImage);
       this.style = style;
   }

    public String getCardName() {
        return cardName;
    }

    public String getStyle() {
        return style;
    }

    public int getQuantity() {
        return quantity;
    }

    public File getCardImage() {
        return cardImage;
    }

    public UUID getTokenUUID() {
        return tokenUUID;
    }

    public void setTokenUUID(UUID tokenUUID) {
        this.tokenUUID = tokenUUID;
    }

    @Override
    public boolean equals(Object obj) {
       if(!(obj instanceof Token)){
           return false;
       }

       if(obj == this){
           return true;
       }


       Token inputToken = (Token) obj;
        // true if both null, false if only one is null, true if both non-null and one.equals(two)
        if(Objects.equals(this.getTokenUUID(), inputToken.getTokenUUID())){
            if(this.getTokenUUID()==null){ // if true both where null in previous check, so check name
                return this.getCardName().equalsIgnoreCase(inputToken.getCardName());
            }else{ // else return true;
                return true;
            }
        }
        // not equal, so return false
        return false;
    }
}
