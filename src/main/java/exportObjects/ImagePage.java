package exportObjects;

/*
    Stored in a format
    {"<pageNum>": { ImagePage }}
    Due to this not being an array i am unsure how to best map this in gson
 */
public class ImagePage {
    private transient int pageNumber = 1;
    private String FaceURL;
    private String BackURL;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getFaceUrl() {
        return FaceURL;
    }

    public void setFaceUrl(String faceURL) {
        FaceURL = faceURL;
    }

    public String getBackUrl() {
        return BackURL;
    }

    public void setBackUrl(String backUrl) {
        BackURL = backUrl;
    }
}
