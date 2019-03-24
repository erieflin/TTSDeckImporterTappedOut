package images;

import java.awt.image.BufferedImage;

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
