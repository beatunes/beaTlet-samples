// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html


import javax.swing.*
import java.awt.event.ActionEvent
import com.tagtraum.audiokern.*
import com.tagtraum.core.app.*
import com.tagtraum.beatunes.action.*
import com.tagtraum.beatunes.songtable.SongPropertyChangeListener

class ResetReplayGain extends BaseAction {

    def String getId() {
        "Groovy.ResetReplayGain"
    }

    def void init() {
        putValue(Action.NAME, "Reset ReplayGain")
    }

    /**
     * Install the action in both the Tools menu and the context
     * menu of the main table.
     */
    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.TOOL_MENU, AbsoluteActionLocation.LAST),
        new AbsoluteActionLocation(BeaTunesUIRegion.SONG_CONTEXT_MENU, AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        // get ids for the selected songs
        long[] ids = getSelectedSongIds()
        for (long id : ids) {
            // obtain the AudioSong object
            AudioSong song = getApplication().getMediaLibrary().getSong(id)
            // register a SongPropertyChangeListener, so that
            // any changes are displayed right away.
            song.addPropertyChangeListener(new SongPropertyChangeListener(getApplication().getMainWindow().getSongTable().getTable(), id))
            // do something with the object.
            // don't forget that you are on the EDT!
            // so whatever you do here, should be quick.
            // if not, start a new thread for your work.
            song.setAlbumReplayGain(null, false)
            song.setTrackReplayGain(null, true)
        }
    }
}
