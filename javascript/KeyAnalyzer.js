// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-songpropertyanalyzer.html

/*
 * These type vars basically act as imports for Java classes.
 */
var SongPropertyAnalyzer = Java.type("com.tagtraum.audiokern.SongPropertyAnalyzer");
var AudioClip = Java.type("com.tagtraum.audiokern.AudioClip");
var Mono = Java.type("com.tagtraum.jipes.audio.Mono");

var beatlet = new SongPropertyAnalyzer() {

    /*
     * Unique id of this analyzer.
     * This is <em>essential</em> for SongPropertyAnalyzers
     * written in Javascript.
     */
    getId: function() {
        return "JavascriptKeyAnalyzer1.0.0";
    },

    /* Name of this analyzer. Please include a version number. */
    getName: function() {
        return "Javascript KeyAnalyzer 1.0.0";
    },

    /* How much audio do we need? */
    getRequiredClip: function(audioFileFormat) {
        // AudioClip takes start and stop times in ms
        return new AudioClip(0, 120000);
    },

    /* Create a new pipeline. See <a href="http://www.tagtraum.com/jipes/">Jipes</a> for details. */
    createPipeline: function() {
        // Mono <em>does not</em> compute a key. It's merely a stand-in for a real
        // com.tagtraum.jipes.SignalPipeline
        // The id of the processor that computes the key, must be "key".
        return new Mono();
    },

    /*
     * The property you want to change.
     * This corresponds to com.tagtraum.audiokern.AudioSong properties.
     */
    getPropertyName: function() {
        return "key";
    }
}

// Put "beatlet" into the last line, so that it is returned to beaTunes
// when this script is eval'd.
beatlet;
