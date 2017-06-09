# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-librarybatchaction.html
from javax.swing import Action
from org.slf4j import LoggerFactory
from com.tagtraum.beatunes.action.standard import LibraryBatchAction

# An action that allows to do something (print the name) for each song in the library.
# The corresponding menu item can be found in the 'Tools' menu.
class PrintEachNameInLibrary(LibraryBatchAction):

    # Inner class that implements the
    # com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
    # interface. Its process method is called for each song.
    class SongPrinter(LibraryBatchAction.EachSongProcessor):

        log = LoggerFactory.getLogger("PrintEachNameInLibrary.py")

        # Called once, before processing starts.
        def startProcessing(self, count):
            self.__class__.log.info("We are expecting to print %s names. Let's start!" % (count))

        # Called for each song.
        def process(self, song, index):
            # print to beaTunes log file
            self.__class__.log.info("Song: %s" % (song.getName()))

        # Called once all songs were processed.
        def finishProcessing(self):
            self.__class__.log.info("Done!")

        # Message to be shown in progress dialog.
        def getProgressDialogMessage(self, song):
            return "<html>Printing <i>%s</i> ...</html>" % (song.getName())

        # Title for progress dialog.
        def getProgressDialogTitle(self):
            return "Printing Song Names ..."

    # Unique id
    def getId(self):
        return "Jython.PrintEachNameInLibrary"

    # Is called by beaTunes as part of the lifecycle after instantiation.
    # At this point all other plugins are instantiated and registered.
    # We use this to set the menu item's (i.e. action's) name.
    def init(self):
        self.putValue(Action.NAME, "Print all Song Names")

    # We need to ask the user, whether he really wants to do this.
    # How we ask is defined here.
    def getConfirmationMessage(self):
        return "Do you really want to print all the song names in this library to the log?";

    # Factory method that creates the processor for each song.
    def createEachSongProcessor(self):
        return PrintEachNameInLibrary.SongPrinter();

