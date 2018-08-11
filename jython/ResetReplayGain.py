# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-getting-started.html
from javax.swing import Action
from com.tagtraum.core.app import AbsoluteActionLocation
from com.tagtraum.core.app import ActionLocation
from com.tagtraum.beatunes.action import BaseAction
from com.tagtraum.beatunes.action import BeaTunesUIRegion
from com.tagtraum.beatunes.songtable import SongPropertyChangeListener
# Needed for Java array creation
from jarray import array

class ResetReplayGain(BaseAction):

    def getId(self):
        return "Jython.ResetReplayGain"

    def init(self):
        self.putValue(Action.NAME, "Reset ReplayGain")

    # Install the action in both the Tools menu and the context
    # menu of the main table.
    def getActionLocations(self):
        # Java array creation via array([], type)
        return array([
        AbsoluteActionLocation(BeaTunesUIRegion.TOOL_MENU, AbsoluteActionLocation.LAST),
        AbsoluteActionLocation(BeaTunesUIRegion.SONG_CONTEXT_MENU, AbsoluteActionLocation.LAST)
        ], ActionLocation)

    def actionPerformed(self, actionEvent):
        # get ids for the selected songs
        ids = self.getSelectedSongIds()
        for id in ids:
            # obtain the AudioSong object
            # special cast to java.lang.Long necessary!!
            song = self.getApplication().getMediaLibrary().getSong(id)
            # register a SongPropertyChangeListener, so that
            # any changes are displayed right away.
            song.addPropertyChangeListener(
            SongPropertyChangeListener(self.getApplication().getMainWindow().getSongTable().getTable(), id)
            )
            # do something with the object.
            # don't forget that you are on the EDT!
            # so whatever you do here, should be quick.
            # if not, start a new thread for your work.
            song.setAlbumReplayGain(None, False)
            song.setTrackReplayGain(None, True)
