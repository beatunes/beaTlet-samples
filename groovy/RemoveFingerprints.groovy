// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-librarybatchaction.html
import javax.swing.Action
import org.slf4j.LoggerFactory
import com.tagtraum.beatunes.action.standard.LibraryBatchAction
import com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
import com.tagtraum.audiokern.AudioSong

// An action that allows to do something (remove fingerprints) for each song in
// the library. The corresponding menu item can be found in the 'Tools' menu.
class RemoveFingerprints extends LibraryBatchAction {

    // Inner class that implements the
    // com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    // interface. Its process method is called for each song.
    class Remover implements EachSongProcessor {

        static log = LoggerFactory.getLogger("RemoveFingerprints.groovy")

        // Called once, before processing starts.
        def void startProcessing(int count) {
            log.info "We are expecting to look at ${count} tracks. Let's start!"
        }

        // Called for each song.
        def void process(AudioSong song, int index) {
            log.info "Removing fingerprint from song: ${song.getName()}"
            song.setFingerprint(null)
        }

        // Called once all songs were processed.
        def void finishProcessing() {
            log.info "Done!"
        }

        // Message to be shown in progress dialog.
        def String getProgressDialogMessage(AudioSong song) {
            "<html>Removing fingerprint from <i>${song.getName()}</i> ...</html>"
        }

        // Title for progress dialog.
        def String getProgressDialogTitle() {
            "Removing Fingerprints ..."
        }
    }

    // Unique id
    def String getId() {
        "Groovy.RemoveFingerprints"
    }

    // Is called by beaTunes as part of the lifecycle after instantiation.
    // At this point all other plugins are instantiated and registered.
    // We use this to set the menu item's (i.e. action's) name.
    def void init() {
        putValue(Action.NAME, "Remove Fingerprints")
    }

    // We need to ask the user, whether he really wants to do this.
    // How we ask is defined here.
    def String getConfirmationMessage() {
        "Do you really want to remove fingerprints from all songs in this library?"
    }

    // Factory method that creates the processor for each song.
    def EachSongProcessor createEachSongProcessor() {
        new Remover()
    }
}
