// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html
import javax.swing.*
import java.awt.event.ActionEvent
import java.util.*;
import com.tagtraum.core.app.*
import com.tagtraum.beatunes.MessageDialog
import com.tagtraum.beatunes.action.*
import com.tagtraum.audiokern.PlayList;
import com.tagtraum.audiokern.AudioSong;
import com.tagtraum.beatunes.action.standard.NewPlayListAction;
import com.tagtraum.beatunes.BeaTunes;

/**
 * Creates a new playlist with songs from the current playlist,
 * but using only one song from any given (source-) artist.
 */
class UniqueArtistPLaylist extends NewPlayListAction {

    def String getId() {
        "Groovy.UniqueArtistPLaylist"
    }

    UniqueArtistPLaylist() {
        super(null, false)
    }

    def void init() {
        putValue(Action.NAME, "New Unique Artist PLaylist")
    }

    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.FILE_MENU, 0)]
    }

    def boolean fillPlayList(PlayList newPlayList, long[] selectedSongIds) {
        // get current playList
        PlayList currentPlaylist = getSelectedPlayList()

        // get ids for the first song by an artist
        List<Long> newSongIds = new ArrayList<>()
        Set<String> usedArtists = new HashSet<>()
        currentPlaylist.getSongsOrderedBy("artist").each{ song ->
            if (!usedArtists.contains(song.getArtist())) {
                usedArtists.add(song.getArtist())
                newSongIds.add(song.getId())
            }
        }

        // add songs in bulk
        newPlayList.addSongIds(newSongIds)
        return true
    }

}
