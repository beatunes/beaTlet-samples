// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-librarybatchaction.html
import javax.swing.Action
import org.slf4j.LoggerFactory
import com.tagtraum.beatunes.action.standard.LibraryBatchAction
import com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
import com.tagtraum.audiokern.AudioSong

// An action that allows to do something (remove leading/trailing whitespace)
// for each song in the library.
// The corresponding menu item can be found in the 'Tools' menu.
class RemoveWhitespace extends LibraryBatchAction {

    // Inner class that implements the
    // com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    // interface. Its process method is called for each song.
    class Trimmer implements EachSongProcessor {

        static log = LoggerFactory.getLogger("RemoveWhitespace.groovy")

        // Called once, before processing starts.
        def void startProcessing(int count) {
            log.info "We are expecting to trim ${count} names. Let's start!"
        }

        // Called for each song.
        def void process(AudioSong song, int index) {
            // print to beaTunes log file
            log.info "Trimming song: ${song.getName()}"
            if (song.getName() != null) song.setName(song.getName().trim())
            if (song.getArtist() != null) song.setArtist(song.getArtist().trim())
            if (song.getAlbum() != null) song.setAlbum(song.getAlbum().trim())
            if (song.getAlbumArtist() != null) song.setAlbumArtist(song.getAlbumArtist().trim())
            if (song.getComposer() != null) song.setComposer(song.getComposer().trim())
            if (song.getGenre() != null) song.setGenre(song.getGenre().trim())
            log.info "Trimming song: ${song.getName()}. Done."
        }

        // Called once all songs were processed.
        def void finishProcessing() {
            log.info "Done!"
        }

        // Message to be shown in progress dialog.
        def String getProgressDialogMessage(AudioSong song) {
            "<html>Trimming <i>${song.getName()}</i> ...</html>"
        }

        // Title for progress dialog.
        def String getProgressDialogTitle() {
            "Removing leading/trailing whitespace from  ..."
        }
    }

    // Unique id
    def String getId() {
        "Groovy.RemoveWhitespace"
    }

    // Is called by beaTunes as part of the lifecycle after instantiation.
    // At this point all other plugins are instantiated and registered.
    // We use this to set the menu item's (i.e. action's) name.
    def void init() {
        putValue(Action.NAME, "Remove Whitespace")
        putValue(Action.SHORT_DESCRIPTION, "Removes leading and trailing whitespace from name,"
            + "artist, album, album artist, genre and composer.")
    }

    // We need to ask the user, whether he really wants to do this.
    // How we ask is defined here.
    def String getConfirmationMessage() {
        return ("Do you really want to remove leading/trailing<br>"
            + "whitespace from all tracks in your library?<br><br>"
            + "Affected fields are name, artist, album,<br>"
            + "album artist, genre and composer.")
    }

    // Factory method that creates the processor for each song.
    def EachSongProcessor createEachSongProcessor() {
        new Trimmer()
    }
}
