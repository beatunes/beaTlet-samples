# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-getting-started.html
require 'java'

java_import javax.swing.Action

java_import com.tagtraum.core.app.ActionLocation
java_import com.tagtraum.core.app.AbsoluteActionLocation

java_import com.tagtraum.beatunes.action.BeaTunesUIRegion
java_import com.tagtraum.beatunes.action.BaseAction
java_import com.tagtraum.beatunes.songtable.SongPropertyChangeListener

class ResetReplayGain < BaseAction

    def getId
        "JRuby.ResetReplayGain"
    end

    def init()
        putValue(Action.NAME, "Reset ReplayGain")
    end

    # Install the action in both the Tools menu and the context
    # menu of the main table.
    def getActionLocations()
        # "to_java()" converts the Ruby array into a Java array of the given type
        [
            AbsoluteActionLocation.new(BeaTunesUIRegion::TOOL_MENU, AbsoluteActionLocation::LAST),
            AbsoluteActionLocation.new(BeaTunesUIRegion::SONG_CONTEXT_MENU, AbsoluteActionLocation::LAST)
        ].to_java(ActionLocation)
    end

    def actionPerformed(actionEvent)
        # get ids for the selected songs
        ids = getSelectedSongIds()
        for id in ids
            # obtain the AudioSong object
            # special cast to java.lang.Long necessary!!
            song = getApplication().getMediaLibrary().getSong(id.to_java(java.lang.Long))
            # register a SongPropertyChangeListener, so that
            # any changes are displayed right away.
            song.addPropertyChangeListener(
                SongPropertyChangeListener.new(getApplication().getMainWindow().getSongTable().getTable(), id)
            )
            # do something with the object.
            # don't forget that you are on the EDT!
            # so whatever you do here, should be quick.
            # if not, start a new thread for your work.
            song.setAlbumReplayGain(nil, false)
            song.setTrackReplayGain(nil, false)
        end
    end
end
