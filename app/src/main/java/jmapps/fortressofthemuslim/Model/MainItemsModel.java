package jmapps.fortressofthemuslim.Model;

public class MainItemsModel {

    private final String idPosition;
    private final String contentArabic;
    private final String contentTranscription;
    private final String contentRussian;
    private final String contentSource;

    public MainItemsModel(String idPosition, String contentArabic, String contentTranscription,
                          String contentRussian, String contentSource) {
        this.idPosition = idPosition;
        this.contentArabic = contentArabic;
        this.contentTranscription = contentTranscription;
        this.contentRussian = contentRussian;
        this.contentSource = contentSource;
    }

    public String getIdPosition() {
        return idPosition;
    }

    public String getContentArabic() {
        return contentArabic;
    }

    public String getContentTranscription() {
        return contentTranscription;
    }

    public String getContentRussian() {
        return contentRussian;
    }

    public String getContentSource() {
        return contentSource;
    }
}