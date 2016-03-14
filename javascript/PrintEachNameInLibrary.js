// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-librarybatchaction.html

/*
 * These type vars basically act as imports for Java classes.
 */
var Action = Java.type("javax.swing.Action");
var LoggerFactory = Java.type("org.slf4j.LoggerFactory");
var LibraryBatchAction = Java.type("com.tagtraum.beatunes.action.standard.LibraryBatchAction");
var EachSongProcessor = Java.type("com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor");
var AudioSong = Java.type("com.tagtraum.audiokern.AudioSong");

var beatlet = new LibraryBatchAction() {

    /*
     * Factory method that creates the EachSongProcessor.
     */
    createEachSongProcessor: function() {

        // Inner class that implements the
        // com.tagtraum.beatunes.action.standard.LibraryBatchAction.EachSongProcessor
        // interface. Its process method is called for each song.
        return new EachSongProcessor() {

            log: LoggerFactory.getLogger("PrintEachNameInLibrary.js"),

            /* Called once, before processing starts. */
            startProcessing: function(count) {
                this.log.info("We are expecting to print " + count + " names. Let's start!");
            },

            /* Called for each song. */
            process: function(song, index) {
                // print to beaTunes log file
                this.log.info("Song: " + song.getName());
            },

            /* Called once all songs were processed. */
            finishProcessing: function() {
                this.log.info("Done!");
            },

            /* Message to be shown in progress dialog. */
            getProgressDialogMessage: function(song) {
                return "<html>Printing <i>"+ song.getName() + "</i> ...</html>";
            },

            /* Title for progress dialog. */
            getProgressDialogTitle: function() {
                return "Printing Song Names ...";
            }
        }
    },

    // Unique id
    getId: function() {
        return "Javascript.PrintEachNameInLibrary"
    },

    /*
     * Is called by beaTunes as part of the lifecycle after instantiation.
     * At this point all other plugins are instantiated and registered.
     * We use this to set the menu item's (i.e. action's) name.
     */
    init: function() {
        beatletSuper.putValue(Action.NAME, "Print all Song Names");
    },

    /*
     * We need to ask the user, whether he really wants to do this.
     * How we ask is defined here.
     */
    getConfirmationMessage: function() {
        return "Do you really want to print all the song names in this library to the log?";
    }
}

// Find super class of beatlet, so that we can call methods on it
// e.g. in the "init" function.
var beatletSuper = Java.super(beatlet);

// Put "beatlet" into the last line, so that it is returned to beaTunes
// when this script is eval'd.
beatlet;
