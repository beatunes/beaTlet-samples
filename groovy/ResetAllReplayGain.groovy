import javax.swing.Action
import org.slf4j.LoggerFactory
import com.tagtraum.beatunes.action.standard.LibraryBatchAction
import com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
import com.tagtraum.audiokern.AudioSong

// Resets ReplayGain values for *all* tracks in the library.
// The corresponding menu item can be found in the 'Tools' menu.
class ResetAllReplayGain extends LibraryBatchAction {

    // Inner class that implements the
    // com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    // interface. Its process method is called for each song.
    class Reset implements EachSongProcessor {

        static log = LoggerFactory.getLogger("ResetAllReplayGain.groovy")

        // Called once, before processing starts.
        def void startProcessing(int count) {
            log.info "We are expecting to process ${count} tracks. Let's start!"
        }

        // Called for each song.
        def void process(AudioSong song, int index) {
            // print to beaTunes log file
            log.info "Song: ${song.getName()}"
            song.setAlbumReplayGain(null, false)
            song.setTrackReplayGain(null, true)
        }

        // Called once all songs were processed.
        def void finishProcessing() {
            log.info "Done!"
        }

        // Message to be shown in progress dialog.
        def String getProgressDialogMessage(AudioSong song) {
            "<html>Resetting ReplayGain for <i>${song.getName()}</i> ...</html>"
        }

        // Title for progress dialog.
        def String getProgressDialogTitle() {
            "Resetting ReplayGain ..."
        }
    }

    // Unique id
    def String getId() {
        "Groovy.ResetAllReplayGain"
    }

    // Is called by beaTunes as part of the lifecycle after instantiation.
    // At this point all other plugins are instantiated and registered.
    // We use this to set the menu item's (i.e. action's) name.
    def void init() {
        putValue(Action.NAME, "Reset All ReplayGain")
    }

    // We need to ask the user, whether he really wants to do this.
    // How we ask is defined here.
    def String getConfirmationMessage() {
        "Do you really want to reset all ReplayGain values in your library?"
    }

    // Factory method that creates the processor for each song.
    def EachSongProcessor createEachSongProcessor() {
        new Reset()
    }
}
