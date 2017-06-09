// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html
import org.slf4j.LoggerFactory
import javax.swing.*
import java.awt.Toolkit
import java.awt.event.*
import com.tagtraum.core.app.*
import com.tagtraum.audiokern.*
import com.tagtraum.beatunes.library.*
import com.tagtraum.beatunes.action.*
import com.tagtraum.beatunes.songtable.SongPropertyChangeListener

/*
 * Simple beaTlet for iTunes-based music libraries that allows
 * re-reading some metadata from the underlying file.
 * Note that some data may be deleted, if the data exists
 * only in the internal database, but is not embedded in the
 * file.
 */
class RefreshFromFileAction extends BaseAction {

    static log = LoggerFactory.getLogger("RefreshFromFileAction.groovy")

    def String getId() {
        "Groovy.RefreshFromFileAction"
    }

    def void init() {
        setEnabled(false)
        putValue(Action.NAME, "Refresh from File")
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))
        // enable this action, if one or more items are selected from the given tables
        bindListeners(BaseAction.SelectionCount.ONE_OR_MORE,
            BaseAction.EnabledView.SONG_TABLE, BaseAction.EnabledView.MATCH_TABLE)
    }

    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.FILE_MENU,
            AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        final library = getApplication().getMediaLibrary()
        getSelectedSongIds().each{ id ->
            log.warn "Attempting to refresh non-iTunes metadata from file for ${id}"

            try {
                // "AudioSong" is the interface we typically use to read and write song properties
                // It allows manipulation of *both* the database and the file at the same time.
                final AudioSong song = library.getSong(id)
                // "AudioMetaData" accesses *only* the underlying file.
                final AudioMetaData audioMetaData = song.getImplementation(AudioMetaData.class)
                if (audioMetaData != null) {
                    log.debug "Importing values from file ${song.getFile()} for ${song}"
                    // make sure we have the latest data
                    audioMetaData.refreshFromFile()
                    // make sure, changes are displayed in the UI
                    song.addPropertyChangeListener(new SongPropertyChangeListener((JTable)getFocusOwner(), song))

                    // Importing properties from song file
                    song.setTrackReplayGain(audioMetaData.getTrackReplayGain(), true)
                    song.setAlbumReplayGain(audioMetaData.getAlbumReplayGain(), false)
                    song.setLanguage(audioMetaData.getLanguage())
                    song.setKey(audioMetaData.getKey())
                    song.setKeyAlgorithm(audioMetaData.getKeyAlgorithm())
                    song.setTags(audioMetaData.getTags())
                    song.setSpectrum(audioMetaData.getSpectrum())
                    song.setColor(audioMetaData.getSpectrum() == null ? null : audioMetaData.getSpectrum().toColor())
                    song.setMood(audioMetaData.getMood())
                    song.setMoodAlgorithm(audioMetaData.getMoodAlgorithm())
                    song.setTuning(audioMetaData.getTuning())
                    song.setTuningAlgorithm(audioMetaData.getTuningAlgorithm())
                    if (audioMetaData.getSegments() != null) {
                        song.setSegments(audioMetaData.getSegments())
                        song.setSegmentsAlgorithm(audioMetaData.getSegmentsAlgorithm())
                    }
                    if (audioMetaData.getSimilarities() != null) {
                        song.setSimilarities(audioMetaData.getSimilarities())
                        song.setSimilaritiesAlgorithm(audioMetaData.getSimilaritiesAlgorithm())
                    }
                    if (song.getRating() == 0) {
                        final int fileRating = audioMetaData.getRating()
                        if (fileRating != 0) {
                            song.setRating(audioMetaData.getRating())
                        }
                    }
                    song.setBeatsPerMinuteAlgorithm(audioMetaData.getBeatsPerMinuteAlgorithm())
                    song.setTempo(audioMetaData.getTempo())
                } else {
                    log.warn "Failed to read AudioMetaData for ${song}"
                }
            } catch (Exception e) {
                log.error "Failed to refresh from file ${id}" e
            }
        }
    }
}
