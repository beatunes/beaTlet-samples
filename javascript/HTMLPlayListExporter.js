// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-playlistexporter.html

/*
 * These type vars basically act as imports for Java classes.
 */
var PlayListExporter = Java.type("com.tagtraum.beatunes.library.PlayListExporter");
var FileWriter = Java.type("java.io.FileWriter");

var beatlet = new PlayListExporter() {

    /* Lowercase file extension (without the '.'). */
    getFileExtension: function() {
        return "html";
    },

    /*
     * Very short description for this exporter.
     * Starting with beaTunes 4.6, this method is optional.
     */
    getDescription: function() {
        return "HTML";
    },

    /*
     * Lets you provide an id for this exporter.
     * The id may be used for referring to an instance of this exporter
     * in persistent configuration files.
     */
    getId: function() {
        return "Javascript.HTMLPlayListExporter";
    },

    /*
     * Exports the given playlist to the given file.
     *
     * file to write to
     * playlist to export
     * progressListener that lets you report... well, progress
     */
    export: function(file, playList, progressListener) {
        var writer = new FileWriter(file.toString());
        try {
            writer.write("<html>\n<body>\n<h1>" + playList.getName() + "</h1>\n<ol>\n");
            playList.getSongs().forEach(function(song) {
                writer.write("<li>" + song.getName() + "</li>\n");
            });
            writer.write("</ol>\n</body>\n</html>\n")

        } finally {
            writer.close();
        }
    }
}

// Put "beatlet" into the last line, so that it is returned to beaTunes
// when this script is eval'd.
beatlet;
