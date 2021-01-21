import javax.swing.Action
import org.slf4j.LoggerFactory
import com.tagtraum.beatunes.action.standard.LibraryBatchAction
import com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
import com.tagtraum.audiokern.AudioSong
import org.jaudiotagger.audio.mp3.MP3AudioHeader
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.AudioHeader
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.tag.FieldKey
import java.nio.file.Path

// Import the name of the audio encoder info and write it to
// field 'custom1'.
// The corresponding menu item can be found in the 'Tools' menu.
// This beaTlet demonstrates how to cirectly access audio tags using
// the JAudioTagger library, which is shipped with beaTunes.
class EncodedToCustom extends LibraryBatchAction {

    // Inner class that implements the
    // com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    // interface. Its process method is called for each song.
    class CopyEncoder implements EachSongProcessor {

        static log = LoggerFactory.getLogger("EncodedToCustom.groovy")

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
                    // use the JAudioTagger library to parse the file
                    AudioFile audioFile = AudioFileIO.read(file.toFile());
                    // retrieve the header
                    AudioHeader audioHeader = audioFile.getAudioHeader()
                    // in case this is an mp3 file, copy the encoder name
                    // to field custom1
                    String encoder = null
                    if (audioHeader instanceof MP3AudioHeader) {
                        MP3AudioHeader mp3AudioHeader = (MP3AudioHeader)audioHeader
                        if (mp3AudioHeader.getEncoder() != null && !mp3AudioHeader.getEncoder().isEmpty()) {
                            encoder = mp3AudioHeader.getEncoder()
                        }
                    }
                    // if we didn't find the encoder name in the header, use the tag
                    if (encoder == null) {
                        encoder = audioFile.getTag().getFirst(FieldKey.ENCODER)
                    }
                    song.setCustom1(encoder)
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
            "<html>Copying encoder for <i>${song.getName()}</i> ...</html>"
        }

        // Title for progress dialog.
        def String getProgressDialogTitle() {
            "Copying 'encoded with' to 'custom1' ..."
        }
    }

    // Unique id
    def String getId() {
        "Groovy.EncodedToCustom"
    }

    // Is called by beaTunes as part of the lifecycle after instantiation.
    // At this point all other plugins are instantiated and registered.
    // We use this to set the menu item's (i.e. action's) name.
    def void init() {
        putValue(Action.NAME, "Encoded To Custom1")
    }

    // We need to ask the user, whether he really wants to do this.
    // How we ask is defined here.
    def String getConfirmationMessage() {
        "Do you really want to overwrite all custom1 fields in your library?"
    }

    // Factory method that creates the processor for each song.
    def EachSongProcessor createEachSongProcessor() {
        new CopyEncoder()
    }
}
