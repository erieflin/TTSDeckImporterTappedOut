package images;

import exportObjects.TTS_Card;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class StitchedImage {
    private String imagePath;
    private BufferedImage buffer;
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public BufferedImage getBuffer() {
        return buffer;
    }

    public void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }

}
