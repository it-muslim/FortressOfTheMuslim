package jmapps.fortressofthemuslim.Model;

public class MainChaptersModel {

    private final String idPosition;
    private final String chapterName;

    public MainChaptersModel(String idPosition, String chapterName) {
        this.idPosition = idPosition;
        this.chapterName = chapterName;
    }

    public String getIdPosition() {
        return idPosition;
    }

    public String getChapterName() {
        return chapterName;
    }
}