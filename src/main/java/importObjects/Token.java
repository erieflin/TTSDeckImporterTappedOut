package importObjects;

import java.io.File;

public class Token implements BaseCard
{
   private String cardName;
   private String style;
   private int quantity = 1;
   private File cardImage;

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

}
