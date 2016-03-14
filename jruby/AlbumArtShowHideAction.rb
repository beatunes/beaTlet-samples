# Sample beaTlet for beaTunes 4.x
# More info at https://www.beatunes.com/en/beatlet-songcontextview.html
# Only works in conjuntion with AlbumArt.rb
require 'java'

java_import javax.swing.Action
java_import com.tagtraum.beatunes.action.standard.SongContextComponentShowHideAction

class AlbumArtShowHideAction < SongContextComponentShowHideAction

    # This id is referenced in the corresponding SongContextView.
    def getId()
        "jruby.albumart.showhide"
    end

    def init()
        super()
        # register name
        putValue(Action::NAME, "Show Album Art")
        # here we could also set a different icon with key Action::SMALL_ICON
    end
end

