package main.java.voxpetrae.musicmetadata.models;

import javafx.beans.property.*;

public class AlbumTrack {
    private final IntegerProperty trackNumber = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty albumArtist = new SimpleStringProperty();
    private final StringProperty composer = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();
    private final StringProperty year = new SimpleStringProperty();
    private final StringProperty filePath = new SimpleStringProperty();
    private final BooleanProperty updated = new SimpleBooleanProperty();

    public AlbumTrack (int trackNumber, String title, String artist, String albumArtist, String composer, String genre, String year, String filePath, boolean updated){
        setTrackNumber(trackNumber);
        setTitle(title);
        setArtist(artist);
        setAlbumArtist(albumArtist);
        setComposer(composer);
        setGenre(genre);
        setYear(year);
        setFilePath(filePath);
        setUpdated(updated);
    }
    public int getTrackNumber() {
        return trackNumber.get();
    }

    public IntegerProperty trackNumberProperty() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber.set(trackNumber);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getArtist() {
        return artist.get();
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public String getAlbumArtist() {
        return albumArtist.get();
    }

    public StringProperty albumArtistProperty() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist.set(albumArtist);
    }

    public String getComposer() {
        return composer.get();
    }

    public StringProperty composerProperty() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer.set(composer);
    }
    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }
    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public String getFilePath() {
        return filePath.get();
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public boolean isUpdated() {
        return updated.get();
    }

    public BooleanProperty updatedProperty() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated.set(updated);
    }
}