// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-librarybatchaction.html
import javax.swing.Action
import org.slf4j.LoggerFactory
import com.tagtraum.beatunes.action.standard.LibraryBatchAction
import com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
import com.tagtraum.audiokern.AudioSong
import com.tagtraum.genres.GenreOntology
import com.tagtraum.genres.rdf.*
import java.util.Locale

// An action that imports genre values from regular tags.
// The corresponding menu item can be found in the 'Tools' menu.
class ImportGenreFromTags extends LibraryBatchAction {

    // Inner class that implements the
    // com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    // interface. Its process method is called for each song.
    class Importer implements EachSongProcessor {

        static log = LoggerFactory.getLogger("ImportGenreFromTags.groovy")

        // Called once, before processing starts.
        def void startProcessing(int count) {
            log.info "We are expecting to import genres for ${count} tracks. Let's start!"
        }

        // Called for each song.
        def void process(AudioSong song, int index) {
            // print to beaTunes log file
            if (song.getGenre() == null || song.getGenre().isEmpty()) {
                log.info "Processing song: ${song.getName()}"
                final ontology = getGenreOntology()
                final tags = song.getTags()
                tags.any{ tag ->
                    final genres = ontology.getGenres(tag)
                    if (!genres.isEmpty()) {
                        log.info "Found genre: ${tag}"
                        song.setGenre(tag)
                        return true
                    }
                }
                log.info "Processing song: ${song.getName()}. Done."
            } else {
                log.info "Skipping song: ${song.getName()}, because a genre is already set"
            }
        }

        // Called once all songs were processed.
        def void finishProcessing() {
            log.info "Done!"
        }

        // Message to be shown in progress dialog.
        def String getProgressDialogMessage(AudioSong song) {
            "<html>Importing genre tag for <i>${song.getName()}</i> ...</html>"
        }

        // Title for progress dialog.
        def String getProgressDialogTitle() {
            "Importing genre tag for ..."
        }
    }

    // Unique id
    def String getId() {
        "Groovy.ImportGenreFromTags"
    }

    // Is called by beaTunes as part of the lifecycle after instantiation.
    // At this point all other plugins are instantiated and registered.
    // We use this to set the menu item's (i.e. action's) name.
    def void init() {
        putValue(Action.NAME, "Import Genre From Tags")
        putValue(Action.SHORT_DESCRIPTION, "Imports a genre tag from the"
            + " regular tags, if available and no genre is set yet.")
    }

    // We need to ask the user, whether he really wants to do this.
    // How we ask is defined here.
    def String getConfirmationMessage() {
        return ("Do you really want to import genre tags from<br>"
            + "regular tags for all tracks in your library?<br><br>"
            + "Genres are only imported, if they are missing and<br>"
            + "an approriate tag is already set in tags.")
    }

    def GenreOntology getGenreOntology() {
        return getApplication().getPluginManager()
            .getImplementation(WikidataOntology.class)
    }

    // Factory method that creates the processor for each song.
    def EachSongProcessor createEachSongProcessor() {
        new Importer()
    }
}
