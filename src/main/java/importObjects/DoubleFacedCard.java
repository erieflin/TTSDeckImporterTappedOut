package importObjects;

import java.io.File;

public class DoubleFacedCard extends Card
{

    private File backCardImage;

    public DoubleFacedCard(CardParams params, File frontCardImage, File backCardImage)
    {
        super(params, frontCardImage);

        this.backCardImage = backCardImage;
        //TODO implement
    }

    public File getBackCardImage() {
        return backCardImage;
    }

    public void setBackCardImage(File backCardImage) {
        this.backCardImage = backCardImage;
    }
}
