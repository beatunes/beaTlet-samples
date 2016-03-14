// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-songcontextview.html
// Only works in conjunction with AlbumArt.js

/*
 * These type vars basically act as imports for Java classes.
 */
var Action = Java.type("javax.swing.Action");
var SongContextComponentShowHideAction = Java.type("com.tagtraum.beatunes.action.standard.SongContextComponentShowHideAction");

// ApplicationComponent is an interface, which we can
// implement via "new" (much like an anonymous inner Java class).
// The resulting instance is stored in the "beatlet" variable.
var beatlet = new SongContextComponentShowHideAction() {

    /*
     * This id is referenced in the corresponding SongContextView.
     */
    getId: function() {
        return "javascript.albumart.showhide";
    },

    init: function() {
        beatletSuper.init();
        // register name
        beatletSuper.putValue(Action.NAME, "Show Album Art");
        // here we could also set a different icon with key Action.SMALL_ICON
    }
}

// Find super class of beatlet, so that we can call methods on it
// in the "actionPerformed" function.
var beatletSuper = Java.super(beatlet);

// Put "beatlet" into the last line, so that it is returned
// to beaTunes when this script is eval'd.
beatlet;
