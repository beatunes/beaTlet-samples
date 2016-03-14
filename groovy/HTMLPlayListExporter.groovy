// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-playlistexporter.html
import java.io.File
import com.tagtraum.core.ProgressListener
import com.tagtraum.beatunes.library.PlayListExporter 
import com.tagtraum.beatunes.library.PlayList

class HTMLPlayListExporter implements PlayListExporter {

    // Lowercase file extension (without the '.').
    def String getFileExtension() {
        "html"
    }

    // Very short description for this exporter.
    def String getDescription() {
        "HTML"
    }

    // Lets you provide an id for this exporter.
    // The id may be used for referring to an instance of this exporter
    // in persistent configuration files.
    def String getId() {
        "groovy.htmlplaylistexporter"
    }

    // Exports the given playlist to the given file.
    //
    // file to write to
    // playlist to export
    // progressListener that lets you report... well, progress
    def void export(File file, PlayList playList, ProgressListener progressListener) {
        file.withWriter { f ->
            f.writeLine("<html>\n<body>\n<h1>${playList.getName()}</h1>\n<ol>")
            playList.getSongs().each() { song ->
                f.writeLine("<li>${song.getName()}</li>")
            }
            f.writeLine("</ol>\n</body>\n</html>")
        }
    }
}

