// Sample beaTlet for beaTunes 5.x
// Works only in conjunction with AlbumArt.groovy
// More info at https://www.beatunes.com/en/beatlet-songcontextview.html
import javax.swing.Action
import com.tagtraum.beatunes.action.standard.SongContextComponentShowHideAction

class AlbumArtShowHideAction extends SongContextComponentShowHideAction {

    // This id is referenced in the corresponding SongContextView.
    def String getId() {
        "groovy.albumart.showhide"
    }

    def void init() {
        super.init()
        // register name
        putValue(Action.NAME, "Show Album Art")
        // here we could also set a different icon with key Action.SMALL_ICON
    }
}

