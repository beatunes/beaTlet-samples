# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-librarybatchaction.html
require 'java'

java_import javax.swing.Action
java_import org.slf4j.LoggerFactory
java_import com.tagtraum.beatunes.action.standard.LibraryBatchAction

# An action that allows to do something (print the name) for each song in the library.
# The corresponding menu item can be found in the 'Tools' menu.
class PrintEachNameInLibrary < LibraryBatchAction

    # Inner class that implements the
    # com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    # interface. Its process method is called for each song.
    class SongPrinter
        # include is the JRuby way of implementing interfaces
        include Java::com.tagtraum.beatunes.action.standard.LibraryBatchAction::EachSongProcessor

        @@log = LoggerFactory.getLogger("PrintEachNameInLibrary.rb")

        # Called once, before processing starts.
        def startProcessing(count)
            @@log.info "We are expecting to print #{count} names. Let's start!"
        end

        # Called for each song.
        def process(song, index)
            # print to beaTunes log file
            @@log.info "Song: #{song.getName}"
        end

        # Called once all songs were processed.
        def finishProcessing
            @@log.info "Done!"
        end

        # Message to be shown in progress dialog.
        def getProgressDialogMessage(song)
            "<html>Printing <i>#{song.getName}</i> ...</html>"
        end

        # Title for progress dialog.
        def getProgressDialogTitle
            "Printing Song Names ..."
        end

    end

    # Unique id
    def getId
        "JRuby.PrintEachNameInLibrary"
    end

    # Is called by beaTunes as part of the lifecycle after instantiation.
    # At this point all other plugins are instantiated and registered.
    # We use this to set the menu item's (i.e. action's) name.
    def init
        putValue(Action::NAME, "Print all Song Names")
    end

    # We need to ask the user, whether he really wants to do this.
    # How we ask is defined here.
    def getConfirmationMessage
        "Do you really want to print all the song names in this library to the log?"
    end

    # Factory method that creates the processor for each song.
    def createEachSongProcessor
        SongPrinter.new
    end

end
