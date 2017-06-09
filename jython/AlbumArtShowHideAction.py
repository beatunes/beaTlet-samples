# Sample beaTlet for beaTunes 5.x
# Works only in conjunction with AlbumArt.py
# More info at https://www.beatunes.com/en/beatlet-songcontextview.html
from javax.swing import Action
from com.tagtraum.beatunes.action.standard import SongContextComponentShowHideAction

class AlbumArtShowHideAction(SongContextComponentShowHideAction):

    # This id is referenced in the corresponding SongContextView.
    def getId(self):
        return "jython.albumart.showhide"

    def init(self):
        SongContextComponentShowHideAction.init(self)
        # register name
        self.putValue(Action.NAME, "Show Album Art")
        # here we could also set a different icon with key Action.SMALL_ICON

