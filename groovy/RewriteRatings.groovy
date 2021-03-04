import javax.swing.Action
import org.slf4j.LoggerFactory
import com.tagtraum.beatunes.action.standard.LibraryBatchAction
import com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
import com.tagtraum.beatunes.library.Song
import com.tagtraum.audiokern.AudioSong
import com.tagtraum.beatunes.library.MediaLibrary


// Rewrites the rating we already have.
// Only makes sense, if "force overwriting" in the general prefs
// is turned on, as otherwise setting the value we just read has
// no effect.
class RewriteRatings extends LibraryBatchAction {

    // Inner class that implements the
    // com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    // interface. Its process method is called for each song.
    class Rewriter implements EachSongProcessor {

        static log = LoggerFactory.getLogger("RewriteRatings.groovy")

        // Called once, before processing starts.
        def void startProcessing(int count) {
            log.info "We are expecting to process ${count} tracks. Let's start!"
        }

        // Called for each song.
        def void process(AudioSong song, int index) {
            // print to beaTunes log file
            log.info "Song: ${song.getName()}"
            try {
                song.setRating(song.getRating())
                // ensure synchronous processing, in order
                // to not overload the processing queue
                getMediaLibrary().getTunesExecutor().submit { 42 }.get()
                // song.setAlbumRating(song.getAlbumRating())
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
            "<html>Rewriting ratings for <i>${song.getName()}</i> ...</html>"
        }

        // Title for progress dialog.
        def String getProgressDialogTitle() {
            "Rewrite Ratings ..."
        }
    }

    def MediaLibrary getMediaLibrary() {
        return getApplication().getMediaLibrary()
    }

    // Unique id
    def String getId() {
        "Groovy.RewriteRatings"
    }

    // Is called by beaTunes as part of the lifecycle after instantiation.
    // At this point all other plugins are instantiated and registered.
    // We use this to set the menu item's (i.e. action's) name.
    def void init() {
        putValue(Action.NAME, "Rewrite Ratings")
    }

    // We need to ask the user, whether he really wants to do this.
    // How we ask is defined here.
    def String getConfirmationMessage() {
        "Do you really want to rewrite ratings for all files (force overwriting should be enabled)?"
    }

    // Factory method that creates the processor for each song.
    def EachSongProcessor createEachSongProcessor() {
        new Rewriter()
    }
}
