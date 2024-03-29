package voxpetrae.musicmetadata.models;

import javafx.beans.property.*;
// import javafx.beans.Observable;
// import javafx.util.Callback;
/**
 * A generic model for listings and overviews.
 */
public class AlbumTrack {
    private final IntegerProperty trackNumber = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty albumArtist = new SimpleStringProperty();
    private final StringProperty composer = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();
    private final StringProperty year = new SimpleStringProperty();
    private final StringProperty filePath = new SimpleStringProperty();
    private final BooleanProperty unsaved = new SimpleBooleanProperty();

    public AlbumTrack (int trackNumber, String title, String artist, String albumArtist, String composer, String genre, String year, String filePath, boolean unsaved){
        setTrackNumber(trackNumber);
        setTitle(title);
        setArtist(artist);
        setAlbumArtist(albumArtist);
        setComposer(composer);
        setGenre(genre);
        setYear(year);
        setFilePath(filePath);
        setUnsaved(unsaved);
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

    public boolean isUnsaved() {
        return unsaved.get();
    }

    public BooleanProperty unsavedProperty() {
        return unsaved;
    }

    public void setUnsaved(boolean unsaved) {
        this.unsaved.set(unsaved);
    }
    /* public static Callback<AlbumTrack, Observable[]> extractor() {
        return (AlbumTrack a) -> new Observable[]{a.titleProperty(), a.artistProperty(), a.albumArtistProperty(),
            a.composerProperty(), a.genreProperty(), a.yearProperty(), a.filePathProperty(), a.unsavedProperty()};
     } */
}