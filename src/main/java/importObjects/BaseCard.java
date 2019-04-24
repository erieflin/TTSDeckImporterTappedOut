package importObjects;

import java.io.File;

public interface BaseCard {
    public String getCardName();
    public File getCardImage();
    public int getQuantity();
    public String getTTSPile();
}
