import javax.swing.Action
import org.slf4j.LoggerFactory
import com.tagtraum.beatunes.action.standard.LibraryBatchAction
import com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
import com.tagtraum.beatunes.library.Song
import com.tagtraum.beatunes.library.MediaLibrary
import com.tagtraum.audiokern.AudioSong
import java.util.Date
import java.nio.file.Path
import java.nio.file.Files

// Resets the internally stored last modification dates for all files
// so that they are equal to the current last modification dates of the
// files as found on disk.
// This essentially prevents parsing in a folder-based collection during
// synchronization, and may therefore shorten synchronnization
// significantly
class AdjustLastModificationDate extends LibraryBatchAction {

    // Inner class that implements the
    // com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    // interface. Its process method is called for each song.
    class Adjuster implements EachSongProcessor {

        static log = LoggerFactory.getLogger("AdjustLastModificationDate.groovy")

        // Called once, before processing starts.
        def void startProcessing(int count) {
            log.info "We are expecting to process ${count} tracks. Let's start!"
        }

        // Called for each song.
        def void process(AudioSong song, int index) {
            // print to beaTunes log file
            log.info "Song: ${song.getName()}"
            try {
                // Get the audio file
                Path file = song.getFile()
                if (file != null) {
                    // Get the implementation for Song,
                    // which has setDateModified() exposed
                    Song s = song.getImplementation(Song.class);
                    if (s != null) {
                        // get last modification from the file
                        Date lm = new Date(Files.getLastModifiedTime(file).toMillis())
                        if (song.getDateModified() == null || lm.getTime() != song.getDateModified().getTime()) {
                            log.info "Adjusting last modification date from ${song.getDateModified()} to ${lm}"
                            // and store it in the database
                            s.setDateModified(lm)
                            getMediaLibrary().store(s)
                        }
                    }
                }
            } catch (Exception e) {
                // something went sideways, let's log it
                log.error("Something went sideways: ${e}", e)
            }
        }

        // Called once all songs were processed.
        def void finishProcessing() {
            log.info "Done!"
        }

        // Message to be shown in progress dialog.
        def String getProgressDialogMessage(AudioSong song) {
            "<html>Adjusting <i>${song.getName()}</i> ...</html>"
        }

        // Title for progress dialog.
        def String getProgressDialogTitle() {
            "Adjusting Last Modification Dates ..."
        }
    }

    // Unique id
    def String getId() {
        "Groovy.AdjustLastModificationDate"
    }

    // Is called by beaTunes as part of the lifecycle after instantiation.
    // At this point all other plugins are instantiated and registered.
    // We use this to set the menu item's (i.e. action's) name.
    def void init() {
        putValue(Action.NAME, "Adjust Last Modification Date")
    }

    // We need to ask the user, whether he really wants to do this.
    // How we ask is defined here.
    def String getConfirmationMessage() {
        "Do you really want to adjust the last modification dates for all files?"
    }

    def MediaLibrary getMediaLibrary() {
        return getApplication().getMediaLibrary()
    }

    // Factory method that creates the processor for each song.
    def EachSongProcessor createEachSongProcessor() {
        new Adjuster()
    }
}
