// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html

/*
 * These type vars basically act as imports for Java classes.
 */
var Action = Java.type("javax.swing.Action");
var ActionLocations = Java.type("com.tagtraum.core.app.ActionLocation[]");
var AbsoluteActionLocation = Java.type("com.tagtraum.core.app.AbsoluteActionLocation");
var BeaTunesUIRegion = Java.type("com.tagtraum.beatunes.action.BeaTunesUIRegion");
var BaseAction = Java.type("com.tagtraum.beatunes.action.BaseAction");
var SongPropertyChangeListener = Java.type("com.tagtraum.beatunes.songtable.SongPropertyChangeListener");

// BaseAction is an abstract class.
// This allows us to subclass it with "new".
// The resulting subclass instance is stored in the "beatlet" variable.
var beatlet = new BaseAction() {

    /*
     * Unique id.
     */
    getId: function() {
        return "Javascript.ResetReplayGain";
    },

    /*
     * Is called by beaTunes as part of the lifecycle after instantiation.
     * At this point all other plugins are instantiated and registered.
     * We use this to set the menu item's (i.e. the action's) name.
     */
    init: function() {
        beatletSuper.putValue(Action.NAME, "Reset ReplayGain");
    },

    /*
     * Define, where in the UI the Action should appear.
     * You can define multiple locations in an array. Here, we
     * request to be the last item in the Tool and the Context menu.
     * Note, that we use the Nashorn extension "Java.to", to
     * create a Java array.
     */
    getActionLocations: function() {
        return Java.to(
            [
                new AbsoluteActionLocation(BeaTunesUIRegion.TOOL_MENU, AbsoluteActionLocation.LAST),
                new AbsoluteActionLocation(BeaTunesUIRegion.SONG_CONTEXT_MENU, AbsoluteActionLocation.LAST),
            ], ActionLocations
        );
    },

    /*
     * React to a click on the menu item.
     */
    actionPerformed: function(actionEvent) {
        // get ids for the selected songs (this is an array of type long)
        var ids = beatletSuper.getSelectedSongIds()
        for (var i=0; i<ids.length; i++) {
            // obtain the AudioSong object
            var song = beatletSuper.getApplication().getMediaLibrary().getSong(ids[i]);
            // register a SongPropertyChangeListener, so that
            // any changes are displayed right away.
            song.addPropertyChangeListener(new SongPropertyChangeListener(beatletSuper.getApplication().getMainWindow().getSongTable().getTable(), ids[i]));
            // do something with the object.
            // don't forget that you are on the EDT!
            // so whatever you do here, should be quick.
            // if not, start a new thread for your work.
            song.setAlbumReplayGain(null, false);
            song.setTrackReplayGain(null, true);
        }
    }
}

// Find super class of beatlet, so that we can call methods on it
// in the "actionPerformed" function.
var beatletSuper = Java.super(beatlet);

// Put "beatlet" into the last line, so that it is returned
// to beaTunes when this script is eval'd.
beatlet;
