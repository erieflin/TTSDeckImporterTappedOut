package importObjects;

import java.io.File;

public class DoubleFacedCard extends Card
{
    private final File backCardImage;

    public DoubleFacedCard(CardParams params, File frontCardImage, File backCardImage)
    {
        super(params, frontCardImage);

        this.backCardImage = backCardImage;
        //TODO implement
    }
}
