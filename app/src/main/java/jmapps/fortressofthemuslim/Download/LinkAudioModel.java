package jmapps.fortressofthemuslim.Download;

public class LinkAudioModel {

    private final String idPosition;
    private final String linkAudio;

    public LinkAudioModel(String idPosition, String linkAudio) {
        this.idPosition = idPosition;
        this.linkAudio = linkAudio;
    }

    public String getIdPosition() {
        return idPosition;
    }

    public String getLinkAudio() {
        return linkAudio;
    }
}