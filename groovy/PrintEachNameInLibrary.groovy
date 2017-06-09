// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-librarybatchaction.html
import javax.swing.Action
import org.slf4j.LoggerFactory
import com.tagtraum.beatunes.action.standard.LibraryBatchAction
import com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
import com.tagtraum.audiokern.AudioSong

// An action that allows to do something (print the name) for each song in the library.
// The corresponding menu item can be found in the 'Tools' menu.
class PrintEachNameInLibrary extends LibraryBatchAction {

    // Inner class that implements the
    // com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    // interface. Its process method is called for each song.
    class SongPrinter implements EachSongProcessor {

        static log = LoggerFactory.getLogger("PrintEachNameInLibrary.groovy")

        // Called once, before processing starts.
        def void startProcessing(int count) {
            log.info "We are expecting to print ${count} names. Let's start!"
        }

        // Called for each song.
        def void process(AudioSong song, int index) {
            // print to beaTunes log file
            log.info "Song: ${song.getName()}"
        }

        // Called once all songs were processed.
        def void finishProcessing() {
            log.info "Done!"
        }

        // Message to be shown in progress dialog.
        def String getProgressDialogMessage(AudioSong song) {
            "<html>Printing <i>${song.getName()}</i> ...</html>"
        }

        // Title for progress dialog.
        def String getProgressDialogTitle() {
            "Printing Song Names ..."
        }
    }

    // Unique id
    def String getId() {
        "Groovy.PrintEachNameInLibrary"
    }

    // Is called by beaTunes as part of the lifecycle after instantiation.
    // At this point all other plugins are instantiated and registered.
    // We use this to set the menu item's (i.e. action's) name.
    def void init() {
        putValue(Action.NAME, "Print all Song Names")
    }

    // We need to ask the user, whether he really wants to do this.
    // How we ask is defined here.
    def String getConfirmationMessage() {
        "Do you really want to print all the song names in this library to the log?"
    }

    // Factory method that creates the processor for each song.
    def EachSongProcessor createEachSongProcessor() {
        new SongPrinter()
    }
}
