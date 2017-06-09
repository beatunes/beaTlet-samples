// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-keytextrenderer.html

/*
 * These type vars basically act as imports for Java classes.
 */
var KeyTextRenderer = Java.type("com.tagtraum.beatunes.KeyTextRenderer");

var beatlet = new KeyTextRenderer() {

    /* Create a textual representation for a Key object. */
    toKeyString: function(key) {
        // key.ordinal() is a number starting with C Major = 0 and A Minor = 0,
        // then following the order in the Circle of Fifths.
        // Let's shift by 8 and make sure 0 is converted to 12.
        var i = (key.ordinal() + 8) % 12
        i = i == 0 ? 12 : i
        var ab = key.isMajor() ? "A" : "B"
        // create the final string
        return "" + i + ab;
    },

    /* Create a tooltip representation for a key object. */
    toToolTip: function(key) {
        return beatlet.toKeyString(key);
    },

    /* Short name of this renderer. To be used in the user interface. */
    getName: function() {
        return "Custom.js";
    }
}

// Put "beatlet" into the last line, so that it is returned to beaTunes
// when this script is eval'd.
beatlet;
